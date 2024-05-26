package me.nslot.jcomp.servlets.api.problem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/problem/delete")
public class DeleteServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String problemID = req.getParameter("problemID");
            SQLiteManager.deleteProblem(problemID);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}