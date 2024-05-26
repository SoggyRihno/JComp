<%@ page import="me.nslot.jcomp.utils.SQLiteManager" %>
<%@ page import="me.nslot.jcomp.utils.Utils" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<%
    String loginID = request.getParameter("loginID");
    String username = SQLiteManager.getUsername(loginID);

    if (loginID == null || username == null) {
        response.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/login.jsp");
        return;
    }

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

    String home = "/home.jsp?loginID=" + loginID;
    String submission = "/submission.jsp?loginID=" + loginID;
    String leaderboard = "/leaderboard.jsp?loginID=" + loginID;
    String downloads = "/downloads.jsp?loginID=" + loginID;
    String name = "Team " + username.substring(4);
    String position = positions.get(username);
    String score = String.valueOf(teamScores.get(username));
    String tie = ties.get(teamScores.get(username)).size() > 1 ? "(" + ties.get(teamScores.get(username)).size() + "-way tie)" : "";
%>

<head>
    <meta charset="UTF-8">
    <title>LeaderBoard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/leaderboard.css">
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
    <h1><%=name%> :  <%=score%> points</h1>
    <h1><%=position%> <%=tie%></h1>
    <h1></h1>
</div>
<div class="tableDiv">
    <table>
        <thead>
        <tr>
            <th>Team</th>
            <th>Position</th>
            <th>Score</th>
        </tr>
        </thead>
        <tbody>
        <%
            StringBuilder sb = new StringBuilder();
            for (String s : teamScores.keySet().stream().sorted((a,b) -> teamScores.get(b) - teamScores.get(a)).toList()) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s</td>", "Team " + s.substring(4)));
                sb.append(String.format("<td>%s</td>", positions.get(s)));
                sb.append(String.format("<td>%d</td>", teamScores.get(s)));
                sb.append("</tr>");
            }
        %>
        <%=sb.toString()%>
        </tbody>
    </table>
</div>
</body>
</html>