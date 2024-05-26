<%@ page import="me.nslot.jcomp.utils.SQLiteManager" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="me.nslot.jcomp.wrappers.Submission" %>
<%@ page import="java.util.List" %>
<%@ page import="me.nslot.jcomp.wrappers.User" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="me.nslot.jcomp.utils.Utils" %>
<%@ page import="me.nslot.jcomp.wrappers.Problem" %><%--
  Created by IntelliJ IDEA.
  User: tobys
  Date: 11/29/2023
  Time: 9:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String loginID = request.getParameter("loginID");
    String username = SQLiteManager.getUsername(loginID);

    if (loginID == null || username == null || !username.equals("admin")) {
        response.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/login.jsp");
        return;
    }
    LocalDateTime start = SQLiteManager.getStart();
    int length = SQLiteManager.getLength();
    long minutes = ChronoUnit.MINUTES.between(start, LocalDateTime.now());

    Map<String, Integer> teamScores = SQLiteManager.getTeamScores();

    Map<Integer, List<String>> ties = new HashMap<>();
    for (String s : teamScores.keySet()) {
        ties.compute(teamScores.get(s), (k, v) -> {
            v = v == null ? new ArrayList<>() : v;
            v.add(s);
            return v;
        });
    }
    Map<String, String> positions = new HashMap<>();
    int p = 1;
    for (Integer i : ties.keySet().stream().sorted((a, b) -> b - a).toList()) {
        for (String name : ties.get(i).stream().sorted().toList())
            positions.put(name, Utils.formattedPosition(p));
        p++;
    }

    List<Problem> problems = SQLiteManager.getProblems();


    String left = String.valueOf(length - minutes);

    String home = "/admin/home.jsp?loginID=" + loginID;
    String submission = "/admin/submissions.jsp?loginID=" + loginID;
    String leaderboard = "/admin/leaderboard.jsp?loginID=" + loginID;
    String downloads = "/admin/downloads.jsp?loginID=" + loginID;
%>

<head>
    <meta charset="UTF-8">
    <title>Admin Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/home.css">

    <script>
        window.addEventListener("load", ()=>{
            document.getElementById("url").innerHTML = window.location.protocol + "//" + window.location.host + "/";
        })
    </script>

</head>
<body>
<nav class="navBar">
    <a href="${pageContext.request.contextPath}<%=home%>">Home</a>
    <a href="${pageContext.request.contextPath}<%=submission%>">Submission</a>
</nav>

<div class="infoDiv">
    <h1>Admin Home</h1>
    <h1>Time Left : <%=left%> minutes</h1>
    <h2 id="url">URl</h2>
</div>
<div class="tableDiv">
    <table>
        <thead>
        <tr>
            <th>Team</th>
            <th>Password</th>
            <th>Position</th>
            <th>CompleteProblems</th>
            <th>Score</th>
            <th>Active</th>
        </tr>
        </thead>

        <tbody>
        <%
            StringBuilder sb = new StringBuilder();
            for (User user : SQLiteManager.getUsers()) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s</td>", user.username().substring(4)));
                sb.append(String.format("<td>%s</td>", user.password()));
                sb.append(String.format("<td>%s</td>", positions.get(user.username())));
                sb.append("<td>");
                List<Submission> submissions = SQLiteManager.getSubmissionByUser(user.username());
                for (Problem problem : problems) {
                    int status = 0;
                    for (Submission s : submissions) {
                        if (s.problemID().equals(problem.UUID())) {
                            status = s.status();
                            if (s.status() ==1 )
                                break;
                        }
                    }
                    sb.append(status == 0 ? "⬜" : status == 1 ? "✔" : "❌");
                }
                sb.append("</td>");
                sb.append(String.format("<td>%d</td>", teamScores.get(user.username())));
                sb.append(String.format("<td>%s</td>", SQLiteManager.isUserActive(user.username())));
                sb.append("</tr>");
            }
        %>
        <%=sb.toString()%>
        </tbody>
    </table>
</div>
</body>
</html>