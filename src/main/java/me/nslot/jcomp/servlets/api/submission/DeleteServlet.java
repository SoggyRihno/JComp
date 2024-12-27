package me.nslot.jcomp.servlets.api.submission;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.wrappers.Submission;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/api/submission/delete")
public class DeleteServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String loginID = req.getParameter("loginID");
        String submissionID = req.getParameter("submissionID");

        if (loginID != null && submissionID != null) {
            try {
                String username = SQLiteManager.getUsername(loginID);
                if (username != null && username.equals("admin"))
                    SQLiteManager.deleteSubmission(submissionID);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        res.sendRedirect(req.getContextPath() +"/admin/submissions.jsp?loginID=" + loginID);
    }
}