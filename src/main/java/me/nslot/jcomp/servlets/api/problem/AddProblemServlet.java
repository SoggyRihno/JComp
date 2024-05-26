package me.nslot.jcomp.servlets.api.problem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;


import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/problem/add")
public class AddProblemServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String value = req.getParameter("value");
        String problem = req.getParameter("problem");
        String input = req.getParameter("input");
        String output = req.getParameter("output");

        if (name != null && value != null && problem != null && input != null && output != null) {
            try {
                SQLiteManager.addProblem(name, value, problem, input, output);
                var p = SQLiteManager.getProblems();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/");
    }
}