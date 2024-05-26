<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="me.nslot.jcomp.utils.Utils" %>
<%@ page import="me.nslot.jcomp.utils.SQLiteManager" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="me.nslot.jcomp.wrappers.Submission" %>
<%@ page import="me.nslot.jcomp.wrappers.Problem" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<%
    String loginID = request.getParameter("loginID");
    String username = SQLiteManager.getUsername(loginID);

    if (loginID == null || username == null) {
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

    List<Submission> submissions = SQLiteManager.getSubmissionByUser(username);

    String left = String.valueOf(length - minutes);
    String home = "/home.jsp?loginID=" + loginID;
    String name = "Team " + username.substring(4);
    String position = positions.get(username);
    String score = String.valueOf(teamScores.get(username));

    String submission = "/submission.jsp?loginID=" + loginID;
    String leaderboard = "/leaderboard.jsp?loginID=" + loginID;
    String downloads = "/downloads.jsp?loginID=" + loginID;
    String tie = ties.get(teamScores.get(username)).size() > 1 ? "(" + ties.get(teamScores.get(username)).size() + "-way tie)" : "";

%>

<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">

</head>
<body>
<nav class="navBar">
    <a href="${pageContext.request.contextPath}<%=home%>">Home</a>
    <a href="${pageContext.request.contextPath}<%=submission%>">Submission</a>
    <a href="${pageContext.request.contextPath}<%=leaderboard%>">LeaderBoard</a>
    <a href="${pageContext.request.contextPath}<%=downloads%>">Downloads</a>
</nav>
<div class="infoDiv">
    <h1>Time Left : <%=left%> minutes</h1>
    <h2><%=name%></h2>
    <h2><%=position%> <%=tie%></h2>
</div>
<div class="tableDiv">
    <table>
        <thead>
        <tr>
            <th>Time</th>
            <th>Problem</th>
            <th>Status</th>
            <th>Code</th>
            <th>Answer</th>
        </tr>
        </thead>

        <tbody>
        <%
            StringBuilder sb = new StringBuilder();
            for (Submission s : submissions) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s:%s</td>", LocalDateTime.parse(s.time()).getHour(), LocalDateTime.parse(s.time()).getMinute()));
                sb.append(String.format("<td>%s</td>", SQLiteManager.getProblemByID(s.problemID()).name()));
                sb.append(String.format("<td>%s</td>", s.status() == 0 ? "Not Judged" : s.status() == 1 ? "Pass" : "Fail"));
                sb.append(String.format("<td><a  href=\"/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/api/submission/code?loginID=%s&submissionID=%s\">View Code</a>\n</td>", loginID, s.submissionID()));
                sb.append(String.format("<td><a  href=\"/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/api/submission/answer?loginID=%s&submissionID=%s\">View Answer</a>\n</td>", loginID, s.submissionID()));
                sb.append("</tr>");
            }
        %>
        <%=sb.toString()%>
        </tbody>
    </table>
</div>

</body>
</html>