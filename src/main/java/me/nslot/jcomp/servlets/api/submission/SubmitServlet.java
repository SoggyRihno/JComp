package me.nslot.jcomp.servlets.api.submission;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.wrappers.Problem;
import me.nslot.jcomp.wrappers.Submission;
import org.apache.commons.io.IOUtils;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@WebServlet("/api/submission/submit")
public class SubmitServlet extends HttpServlet {


    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String loginID = req.getParameter("loginID");
        String username = req.getParameter("username");
        String problemID = req.getParameter("problemID");
        String code = req.getParameter("code");

        if (loginID != null && username != null) {
            try {
                if (!problemID.equals("null")) {
                    Problem problem = SQLiteManager.getProblemByID(problemID);
                    String answer = judge(code, problem);
                    int status = answer.trim().equals(problem.output()) ? 1 : 2;

                    Submission submission
                            = new Submission(username, problemID, status, code, answer, UUID.randomUUID().toString(), LocalDateTime.now().toString());
                    SQLiteManager.addSubmission(submission);

                }
            } catch (SQLException e) {
                throw new ServletException(e);
            }
            res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/submission.jsp?loginID=" + loginID);
        } else {
            res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/login.jsp");
        }
    }

    private static String judge(String code, Problem problem) {
        try {
            File folder = Files.createTempDirectory("TempDir").toFile();
            File classFile = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 10.1\\webapps\\Gradle___me_nslot___JComp_1_0_SNAPSHOT_war\\WEB-INF\\classes\\me\\nslot\\jcomp",problem.name()+".class");
            if (classFile.exists())
                classFile.delete();

            File codeFile = new File(folder, problem.name() + ".java");
            PrintWriter pw = new PrintWriter(codeFile);
            pw.write(code);
            pw.close();

            File inputFile = new File(folder, "input.txt");
            pw = new PrintWriter(inputFile);
            pw.write(problem.input());
            pw.close();

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager manager = compiler.getStandardFileManager(
                    diagnostics, null, null);
            Iterable<? extends JavaFileObject> source = manager.getJavaFileObjects(codeFile);
            compiler.getTask(null, manager, null, null, null, source).call();

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
            System.out.flush();
            System.setOut(ps);

            URLClassLoader classLoader = new URLClassLoader(new URL[]{folder.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
            Class<?> c = Class.forName(problem.name(), true, classLoader);
            Method main = c.getDeclaredMethod("main", String[].class);
            Object[] args = new Object[1];
            args[0] = new String[]{folder.getAbsolutePath()+"\\"};
            main.invoke(null, args);

            System.setOut(System.out);
            ps.close();
            manager.close();
            classFile.delete();
            codeFile.delete();
            inputFile.delete();
            folder.delete();
            return baos.toString(StandardCharsets.UTF_8);
        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            return "Running Submission resulted in an exception";
        }
    }
}
