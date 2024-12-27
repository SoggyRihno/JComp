package me.nslot.jcomp.servlets.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.utils.Utils;
import me.nslot.jcomp.wrappers.Session;
import me.nslot.jcomp.wrappers.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
       String username = req.getParameter("username");
       String password = req.getParameter("password");

        try {
            User user = SQLiteManager.getUser(username);
            if (user != null && user.password().equals(password)) {
                Session session = new Session(user.username(), UUID.randomUUID().toString(), String.valueOf(LocalDateTime.now()), user.admin());
                SQLiteManager.addSession(session);

                if (user.admin())
                    res.sendRedirect(req.getContextPath() + "/admin/home.jsp?loginID="+session.loginID());
                else
                    res.sendRedirect(req.getContextPath() +"/home.jsp?loginID="+session.loginID());
                return;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}