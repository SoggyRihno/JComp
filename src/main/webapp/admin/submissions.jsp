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
<%@ page import="me.nslot.jcomp.wrappers.Problem" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    String loginID = request.getParameter("loginID");
    String username = SQLiteManager.getUsername(loginID);

    if (loginID == null || username == null || !username.equals("admin")) {
        response.sendRedirect("/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/login.jsp");
        return;
    }

    String home = "/admin/home.jsp?loginID=" + loginID;
    String submission = "/admin/submissions.jsp?loginID=" + loginID;
%>



<head>
    <meta charset="UTF-8">
    <title>Submissions </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/styles/submissions.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">

</head>
<body>
<nav class="navBar">
    <a href="${pageContext.request.contextPath}<%=home%>">Home</a>
    <a href="${pageContext.request.contextPath}<%=submission%>">Submissions</a>
</nav>

<div class="infoDiv">
    <h1>Submissions</h1>
</div>
<div class="submissions">
    <table>
        <thead>
        <tr>
            <th>Time</th>
            <th>Team</th>
            <th>Problem</th>
            <th>Status</th>
            <th>Code</th>
            <th>Answer</th>
            <th>Pass</th>
            <th>Fail</th>
            <th>Delete</th>
        </tr>
        </thead>

        <tbody>
        <%
            StringBuilder sb = new StringBuilder();
            for (Submission s : SQLiteManager.getSubmissions()) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s:%s</td>", LocalDateTime.parse(s.time()).getHour(), LocalDateTime.parse(s.time()).getMinute()));
                sb.append(String.format("<td>%s</td>", s.username()));
                sb.append(String.format("<td>%s</td>", SQLiteManager.getProblemByID(s.problemID()).name()));
                sb.append(String.format("<td>%s</td>", s.status() == 0 ? "Not Judged" : s.status() == 1 ? "Pass" : "Fail"));
                sb.append(String.format("<td><a href=\"../api/submission/code?loginID=%s&submissionID=%s\">View Code</a>\n</td>", loginID, s.submissionID()));
                sb.append(String.format("<td><a href=\"../api/submission/answer?loginID=%s&submissionID=%s\">View Answer</a>\n</td>", loginID, s.submissionID()));
                sb.append(String.format("<td> <button onclick='(function(){if (confirm(\"Set Submission to Pass\")) {fetch(\"../api/submission/pass?loginID=%s&submissionID=%s\", {method: \"POST\"}).then(() =>window.location.reload())}})()'> Pass </button> </td>", loginID, s.submissionID()));
                sb.append(String.format("<td> <button onclick='(function(){if (confirm(\"Set Submission to Fail\")) {fetch(\"../api/submission/fail?loginID=%s&submissionID=%s\", {method: \"POST\"}).then(() =>window.location.reload())}})()'> Fail </button> </td>", loginID, s.submissionID()));
                sb.append(String.format("<td> <button onclick='(function(){if (confirm(\"Delete Submission\")) {fetch(\"../api/submission/delete?loginID=%s&submissionID=%s\", {method: \"POST\"}).then(() =>window.location.reload())}})()'> Delete </button> </td>", loginID, s.submissionID()));
                sb.append("</tr>");
            }
        %>
        <%=sb.toString()%>

        </tbody>
    </table>
</div>
</body>
</html>