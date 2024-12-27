package me.nslot.jcomp.servlets.api.submission;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.wrappers.Problem;
import me.nslot.jcomp.wrappers.Submission;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/submission/answer")
public class ViewAnswerServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String loginID = req.getParameter("loginID");
        String submissionID = req.getParameter("submissionID");

        if (loginID != null && submissionID != null) {
            try {
                String username = SQLiteManager.getUsername(loginID);
                if (username != null){
                    Submission submission = SQLiteManager.getSubmissionByID(submissionID);
                    Problem problem = SQLiteManager.getProblemByID(submission.problemID());
                    res.getWriter().printf("SubmissionID : %s\n", submission.submissionID());
                    res.getWriter().printf("Username : %s\n", submission.username());
                    res.getWriter().printf("Problem Name : %s\n",problem.name());
                    res.getWriter().printf("Status : %s\n", submission.status() == 0 ? "Not Judged" : submission.status() == 1 ? "Pass" : "Fail");
                    res.getWriter().println("Answer : \n");
                    res.getWriter().println(submission.answer());

                    if (username.equals("admin")) {
                        res.getWriter().println("\n\n");
                        res.getWriter().println("Expected Answer : \n");
                        res.getWriter().println(problem.output());
                    }
                    return;
                }
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        res.sendRedirect(req.getContextPath() +"/admin/submissions.jsp?loginID=" + loginID);
    }
}