package me.nslot.jcomp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.utils.Utils;
import me.nslot.jcomp.wrappers.Session;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("")
public class WelcomeServlet extends HttpServlet {

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            if (!SQLiteManager.isDataBaseSetUp())
                SQLiteManager.createDatabase();
            if (!SQLiteManager.isContestRunning()) {
                res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/setup.jsp");
                return;
            }

            res.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/login.jsp");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}