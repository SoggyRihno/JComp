<%@ page import="me.nslot.jcomp.utils.SQLiteManager" %>
<%@ page import="java.util.List" %>
<%@ page import="me.nslot.jcomp.wrappers.Problem" %>
<%@ page import="me.nslot.jcomp.wrappers.Submission" %>
<%@ page import="java.time.LocalDateTime" %>
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

    List<Problem> problems = SQLiteManager.getProblems();
    List<Submission> submissions = SQLiteManager.getSubmissionByUser(username);
%>

<head>
    <meta charset="UTF-8">
    <title>Submission</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/submission.css">
    <script>
        window.addEventListener("load", () => {
            document.getElementById("url").innerHTML = window.location.href
        })
    </script>
</head>
<body>

<nav class="navBar">
    <a href="${pageContext.request.contextPath}<%=home%>">Home</a>
    <a href="${pageContext.request.contextPath}<%=submission%>">Submission</a>
    <a href="${pageContext.request.contextPath}<%=leaderboard%>">LeaderBoard</a>
    <a href="${pageContext.request.contextPath}<%=downloads%>">Downloads</a>
</nav>
<div class="infoDiv">
    <h1>Submission</h1>
    <h2>Expected Class: Same as Problem Name</h2>
    <h2>Provided Input: args[0] + input.txt</h2>
    <h2>Expected Output: System.out.print()</h2>
</div>


<div class="uploadDiv">
    <form action="${pageContext.request.contextPath}/api/submission/submit" method="post">
        <input type="hidden" name="loginID" value="<%=loginID%>"/>
        <input type="hidden" name="username" value="<%=username%>"/>
        <div class="uploadControls">
            <select name="problemID">
                <option value="null" selected>Select Problem</option>
                <%

                    StringBuilder sb = new StringBuilder();
                    for (Problem p : problems) {
                        sb.append(String.format("<option value=\"%s\">%s</option>\n", p.UUID(), p.name()));
                    }

                %>
                <%=sb.toString()%>

            </select>
            <input type="submit" value="Submit">
        </div>
        <textarea name="code"></textarea>
    </form>
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
            sb = new StringBuilder();
            for (Submission s : submissions) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s:%s</td>", LocalDateTime.parse(s.time()).getHour(), LocalDateTime.parse(s.time()).getMinute()));
                sb.append(String.format("<td>%s</td>", SQLiteManager.getProblemByID(s.problemID()).name()));
                sb.append(String.format("<td>%s</td>", s.status() == 0 ? "Not Judged" : s.status() == 1 ? "Pass" : "Fail"));
                sb.append(String.format("<td><a href=\"/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/api/submission/code?loginID=%s&submissionID=%s\">View Code</a>\n</td>", loginID, s.submissionID()));
                sb.append(String.format("<td><a href=\"/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/api/submission/answer?loginID=%s&submissionID=%s\">View Answer</a>\n</td>", loginID, s.submissionID()));
                sb.append("</tr>");
            }
        %>
        <%=sb.toString()%>
        </tbody>
    </table>
</div>
</body>
</html>