<%@ page import="me.nslot.jcomp.utils.SQLiteManager" %>
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

    String home = "/home.jsp?loginID=" + loginID;
    String submission = "/submission.jsp?loginID=" + loginID;
    String leaderboard = "/leaderboard.jsp?loginID=" + loginID;
    String downloads = "/downloads.jsp?loginID=" + loginID;
%>


<head>
    <meta charset="UTF-8">
    <title>Downloads</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/downloads.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">
</head>


<body>
<nav class="navBar">
    <a href="${pageContext.request.contextPath}<%=home%>">Home</a>
    <a href="${pageContext.request.contextPath}<%=submission%>">Submission</a>
    <a href="${pageContext.request.contextPath}<%=leaderboard%>">LeaderBoard</a>
    <a href="${pageContext.request.contextPath}<%=downloads%>">Downloads</a>
</nav>

<div class=infoDiv>
    <h1>Downloads</h1>
</div>
<div class="downloadsDiv">
    <div>
        <a href ="${pageContext.request.contextPath}/api/packet.txt" download>Competition Packet </a>
        <br>
        <i>competition.txt</i>
    </div>
</div>
</body>
</html>
