package me.nslot.jcomp.servlets.api.meta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/meta/length")


public class ContestLengthServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String hours = req.getParameter("hours");
        String minutes = req.getParameter("minutes");

        if (hours != null && minutes != null){
            try {
                SQLiteManager.setLength(String.valueOf(Integer.parseInt(hours) * 60 + Integer.parseInt(minutes)));
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/");
    }
}