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
                    String answer = judgeSubmission(code, problem);
                    int status = answer.trim().equals(problem.output()) ? 1 : 2;

                    Submission submission
                            = new Submission(username, problemID, status, code, answer, UUID.randomUUID().toString(), LocalDateTime.now().toString());
                    SQLiteManager.addSubmission(submission);

                }
            } catch (SQLException e) {
                throw new ServletException(e);
            }
            res.sendRedirect(req.getContextPath() +"/submission.jsp?loginID=" + loginID);
        } else {
            res.sendRedirect(req.getContextPath() +"/login.jsp");
        }
    }

    private String judgeSubmission(String code, Problem problem) {
        File tempDir = null;
        try {
            tempDir = Files.createTempDirectory("submission-judging").toFile();

            File sourceFile = new File(tempDir, problem.name() + ".java");
            try (PrintWriter writer = new PrintWriter(sourceFile)) {
                writer.write(code);
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

            try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null)) {
                Iterable<? extends JavaFileObject> sources = fileManager.getJavaFileObjects(sourceFile);
                boolean success = compiler.getTask(null, fileManager, diagnostics, null, null, sources).call();

                if (!success) {
                    return "Compilation failed: " + diagnostics.getDiagnostics().toString();
                }
            }

            File inputFile = new File(tempDir, "input.txt");
            try (PrintWriter inputWriter = new PrintWriter(inputFile)) {
                inputWriter.write(problem.input());
            }

            ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
            try (PrintStream ps = new PrintStream(outputCapture, true, StandardCharsets.UTF_8)) {
                System.setOut(ps);

                URLClassLoader classLoader = new URLClassLoader(new URL[]{tempDir.toURI().toURL()});
                Class<?> cls = Class.forName(problem.name(), true, classLoader);

                Method mainMethod = cls.getDeclaredMethod("main", String[].class);
                mainMethod.invoke(null, (Object) new String[]{tempDir.getAbsolutePath()});
            }

            return outputCapture.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "Error during execution: " + e.getMessage();
        } finally {
            if (tempDir != null) {
                deleteDirectory(tempDir);
            }
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
}