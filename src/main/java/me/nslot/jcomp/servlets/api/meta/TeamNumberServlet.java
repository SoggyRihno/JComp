package me.nslot.jcomp.servlets.api.meta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/meta/teams")
public class TeamNumberServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String teams = req.getParameter("teams");

        if (teams != null){
            try {
                Integer.parseInt(teams);
                SQLiteManager.setTeams(teams);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        res.sendRedirect(req.getContextPath());

    }
}