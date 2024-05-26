package me.nslot.jcomp.servlets.api.problem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.wrappers.Problem;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/problem/view")
public class ViewServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String problemID = req.getParameter("problemID");
            Problem problem = SQLiteManager.getProblemByID(problemID);
            res.getWriter().printf("SubmissionID : %s\n", problem.UUID());
            res.getWriter().printf("Problem Name : %s\n", problem.name());
            res.getWriter().printf("Value : %s\n", problem.value());
            res.getWriter().printf("Problem : \n%s\n", problem.problem());
            res.getWriter().printf("Input : \n%s\n", problem.input());
            res.getWriter().printf("Output : \n%s\n", problem.output());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}