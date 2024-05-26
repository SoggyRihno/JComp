package me.nslot.jcomp.servlets.api.meta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/api/meta/start")
public class StartServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            SQLiteManager.setStartTime();
            SQLiteManager.startContest();
            int teams = SQLiteManager.getTeamNumber();
            for (int i = 1; i <= teams; i++)
                SQLiteManager.addUser("team" +i, UUID.randomUUID().toString().substring(0,11));
            res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}