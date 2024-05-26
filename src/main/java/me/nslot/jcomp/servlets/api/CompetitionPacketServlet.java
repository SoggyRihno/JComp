package me.nslot.jcomp.servlets.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.nslot.jcomp.utils.SQLiteManager;
import me.nslot.jcomp.wrappers.Problem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

@WebServlet("/api/packet.txt")
public class CompetitionPacketServlet extends HttpServlet {
    public static String packet = null;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            if (packet == null) {
                StringBuilder sb = new StringBuilder();
                List<Problem> problems = SQLiteManager.getProblems();
                problems.sort(Comparator.comparingInt(Problem::value));

                for (int i = 1; i <= problems.size(); i++) {
                    Problem problem = problems.get(i - 1);
                    sb.append(String.format("Problem #%d : %s (%d Points)%n", i, problem.name(), problem.value()));
                    sb.append(problem.problem()).append("\n\n");
                    sb.append("____________________________________________________________\n\n");
                }
                packet = sb.toString();
            }
            res.getWriter().println(packet);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}