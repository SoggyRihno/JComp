<%@ page import="java.io.PrintWriter" %>
<%@ page import="me.nslot.jcomp.wrappers.Problem" %>
<%@ page import="me.nslot.jcomp.utils.SQLiteManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<%
    String adminPassword = SQLiteManager.getAdminPassword();
    int length = SQLiteManager.getLength();
    int hours = length /60;
    int minutes = length % 60;

    int team = SQLiteManager.getTeamNumber();
%>
<head>
    <title>Set Up</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/root.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/setup.css">

    <script>
        function start() {
            if (confirm("Are you sure you want to start the contest.\n This action is irreversible.")) {
                fetch("${pageContext.request.contextPath}/api/meta/start", {method: "POST"})
                    .then(() => window.location.replace("${pageContext.request.contextPath}"))
            }
        }

        function addProblem() {
            document.getElementById('name').value = ""
            document.getElementById('value').value = ""
            document.getElementById('problem').value = ""
            document.getElementById('input').value = ""
            document.getElementById('output').value = ""
            return false;
        }
    </script>
</head>
<body>
<div class="infoDiv">
    <h1>Setup</h1>
    <h2>Admin Password : <%=adminPassword%>
    </h2>

</div>

<div class="teamForm">
    <form action="${pageContext.request.contextPath}/api/meta/teams" method="POST">
        <label for="teams">Teams</label>
        <input type="number" name="teams" id="teams" value="<%=team%>">

        <input type="submit" value="Set Teams">
    </form>
</div>

<div class="lengthForm">
    <form action="${pageContext.request.contextPath}/api/meta/length" method="POST">

        <label for="value">Hours</label>
        <input type="number" name="hours" id="hours" value="<%=hours%>">

        <label for="value">Minutes</label>
        <input type="number" name="minutes" id="minutes" value="<%=minutes%>">
        <input type="submit" value="Set Length">

    </form>

</div>


<div class="problemForm">
    <form action="${pageContext.request.contextPath}/api/problem/add" method="POST">

        <div class="problemSmall">
            <label for="name">Problem Name</label>
            <input type="text" name="name" id="name" placeholder="Problem Name">

            <label for="value">Point Value</label>
            <input type="number" name="value" id="value" value="5">
        </div>

        <label for="problem">Problem</label>
        <textarea name="problem" id="problem"></textarea>

        <label for="input">Input</label>
        <textarea name="input" id="input"></textarea>

        <label for="output">Output</label>
        <textarea name="output" id="output"></textarea>

        <input type="submit" value="Add Problem" class="floating">
    </form>
</div>
<div class="tableDiv">
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Value</th>
            <th>UUID</th>
            <th>Problem</th>
            <th>Delete</th>
        </tr>
        </thead>
        <tbody>
        <%
            StringBuilder sb = new StringBuilder();
            for (Problem p : SQLiteManager.getProblems()) {
                sb.append("<tr>");
                sb.append(String.format("<td>%s</td>", p.name()));
                sb.append(String.format("<td>%s</td>", p.value()));
                sb.append(String.format("<td>%s</td>", p.UUID()));
                sb.append(String.format("<td><a href=\"/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/api/problem/view?problemID=%s\">View Problem</a>\n</td>", p.UUID()));
                sb.append(String.format("<td> <button onclick='(function(){if (confirm(\"Delete Problem\")) {fetch(\"/Gradle___me_nslot___JComp_1_0_SNAPSHOT_war/api/problem/delete?problemID=%s\", {method: \"POST\"}).then(() =>window.location.reload())}})()'> Delete Problem</button> </td>", p.UUID()));
                sb.append("</tr>");

            }
        %>
        <%=sb.toString()%>
        </tbody>
    </table>
</div>

<div class="startDiv">
    <button onclick="start()" class="floating">Start Contest</button>
</div>
</body>
</html>
