package me.nslot.jcomp.utils;


import me.nslot.jcomp.wrappers.Problem;
import me.nslot.jcomp.wrappers.Session;
import me.nslot.jcomp.wrappers.Submission;
import me.nslot.jcomp.wrappers.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SQLiteManager {
    private static final String URL = "jdbc:sqlite:database.sqlite3";
    private static final String IS_SET_UP = "SELECT setup FROM meta LIMIT 1";
    private static final String IS_CONTEST_SET_UP = "SELECT running FROM meta LIMIT 1";
    private static final String GET_SESSION = "SELECT * FROM session WHERE username = ? LIMIT 1";
    private static final String GET_PROBLEM_ID = "SELECT * FROM problems WHERE UUID = ? LIMIT 1";
    private static final String GET_PROBLEM_NAME = "SELECT * FROM problems WHERE name = ? LIMIT 1";
    private static final String GET_PROBLEMS = "SELECT * from problems";
    private static final String GET_SUBMISSIONS_BY_USER = "SELECT * from submissions WHERE username = ?";
    private static final String GET_SUBMISSIONS_BY_ID = "SELECT * from submissions WHERE submissionID = ? LIMIT 1";
    private static final String ADD_PROBLEM = "INSERT INTO problems VALUES (?,?,?,?,?,?)";
    private static final String DELETE_PROBLEM = "DELETE FROM problems WHERE UUID = ?";
    private static final String SET_LENGTH = "UPDATE meta SET length = ? WHERE running = 0";
    private static final String START_CONTEST = "UPDATE meta SET running = 1";
    private static final String GET_USER = "SELECT * FROM users WHERE username = ? LIMIT 1";
    private static final String GET_USERS = "SELECT * FROM users";
    private static final String GET_SUBMISSIONS = "SELECT * FROM submissions";
    private static final String GET_USERNAME_LOGIN_ID = "SELECT username FROM session WHERE loginID = ? LIMIT 1";
    private static final String DELETE_SESSION = "DELETE FROM session WHERE username = ?";
    private static final String ADD_SESSION = "INSERT INTO session VALUES (?,?,?,?)";
    private static final String SET_TEAMS = "UPDATE meta SET teams = ? WHERE running = 0";
    private static final String SET_START_TIME = "UPDATE meta SET startTime = ? WHERE running = 0";
    private static final String DELETE_USER = "DELETE FROM users WHERE username = ?";
    private static final String ADD_USER = "INSERT INTO users VALUES (?,?,?)";
    private static final String ADD_SUBMISSION = "INSERT INTO submissions VALUES (?,?,?,?,?,?,?)";
    private static final String GET_TEAM_NUMBER = "SELECT teams FROM meta LIMIT 1";
    private static final String GET_START = "SELECT startTime FROM meta LIMIT 1";
    private static final String GET_LENGTH = "SELECT length FROM meta LIMIT 1";
    private static final String SET_SUBMISSION_STATUS = "UPDATE submissions SET status = ? WHERE submissionID = ?";
    private static final String SET_SUBMISSION_ANSWER = "UPDATE submissions SET answer = ? WHERE submissionID = ?";
    private static final String DELETE_SUBMISSION = "DELETE FROM submissions WHERE submissionID = ?";


    private static final String ADMIN_PASSWORD = UUID.randomUUID().toString().substring(0, 11);


    private static final String[] INIT_DB = {
            "DROP TABLE IF EXISTS meta",
            "CREATE TABLE meta(startTime TEXT NOT NULL, length INTEGER NOT NULL, setUp INTEGER NOT NULL, running INTEGER NOT NULL, teams INTEGER NOT NULL);",
            "INSERT INTO meta VALUES ('', 120, 0, 0, 10)",
            "DROP TABLE IF EXISTS session",
            "CREATE TABLE session(username TEXT NOT NULL, loginID TEXT NOT NULL, creation TEXT NOT NULL, admin INTEGER NOT NULL);",
            "DROP TABLE IF EXISTS submissions",
            "CREATE TABLE submissions(username TEXT NOT NULL, problemID TEXT NOT NULL, status INT NOT NULL, code TEXT NOT NULL, answer TEXT, submissionID TEXT NOT NULL, time TEXT NOT NULL);",
            "DROP TABLE IF EXISTS users",
            "CREATE TABLE users(username TEXT NOT NULL, password TEXT NOT NULL, admin INTEGER NOT NULL)",
            "INSERT INTO users VALUES ('admin','" + ADMIN_PASSWORD + "', 1)",
            "DROP TABLE IF EXISTS problems",
            "CREATE TABLE problems(name TEXT NOT NULL, value INTEGER NOT NULL, question TEXT NOT NULL, input TEXT NOT NULL, output TEXT NOT NULL, uuid TEXT NOT NULL)"
    };

    private static Connection connection;
    private static boolean setUp = false;

    private static Connection getConnection() throws SQLException {
        if (connection == null) {
            DriverManager.setLoginTimeout(10);
            DriverManager.registerDriver(new org.sqlite.JDBC());
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    private static void execute(String sqlite, String... parameters) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(sqlite);
        for (int i = 0; i < parameters.length; i++)
            ps.setString(i + 1, parameters[i]);
        ps.execute();
        ps.close();
    }

    private static PreparedStatement prepareStatement(String sqlite, String... parameters) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(sqlite);
        for (int i = 0; i < parameters.length; i++)
            ps.setString(i + 1, parameters[i]);
        return ps;
    }

    public static boolean isDataBaseSetUp() throws SQLException {
        if (setUp) return true;
        PreparedStatement ps = prepareStatement(IS_SET_UP);
        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getInt(1) == 1) {
            setUp = true;
            rs.close();
            ps.close();
            return true;
        }
        rs.close();
        ps.close();
        return false;
    }

    public static boolean isContestRunning() throws SQLException {
        PreparedStatement ps = prepareStatement(IS_CONTEST_SET_UP);
        ResultSet rs = ps.executeQuery();
        boolean running = rs.getInt(1) == 1;
        rs.close();
        ps.close();
        return running;
    }

    public static void createDatabase() throws SQLException {
        DriverManager.setLoginTimeout(10);
        DriverManager.registerDriver(new org.sqlite.JDBC());
        DriverManager.getConnection(URL);
        for (String s : INIT_DB)
            execute(s);
        setUp = true;
    }


    public static Session getSession(String loginID) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_PROBLEMS, loginID);
        ResultSet rs = ps.executeQuery();
        if (!rs.next())
            return null;
        Session session = new Session(rs.getString(1), rs.getString(2), rs.getString(3), (rs.getInt(4) == 1));
        rs.close();
        ps.close();
        return session;
    }

    public static void addSession(Session session) throws SQLException {
        execute(DELETE_SESSION, session.username());
        execute(ADD_SESSION, session.username(), session.loginID(), session.creation(), String.valueOf(0));
    }

    public static List<Problem> getProblems() throws SQLException {
        PreparedStatement ps = prepareStatement(GET_PROBLEMS);
        ResultSet rs = ps.executeQuery();
        ArrayList<Problem> problems = new ArrayList<>();
        while (rs.next())
            problems.add(new Problem(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
        rs.close();
        ps.close();
        return problems;
    }

    public static Problem getProblemByID(String UUID) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_PROBLEM_ID, UUID);
        ResultSet rs = ps.executeQuery();
        Problem problem = null;
        if (rs.next()) {
            problem = new Problem(rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
        }
        rs.close();
        ps.close();
        return problem;
    }

    public static void deleteProblem(String UUID) throws SQLException {
        execute(DELETE_PROBLEM, UUID);
    }

    public static void addProblem(String name, String value, String problem, String input, String output) throws SQLException {
        execute(ADD_PROBLEM, name, value, problem, input, output, UUID.randomUUID().toString());
    }

    public static void setLength(String minutes) throws SQLException {
        execute(SET_LENGTH, minutes);
    }

    public static void setTeams(String teams) throws SQLException {
        execute(SET_TEAMS, teams);
    }

    public static void setStartTime() throws SQLException {
        execute(SET_START_TIME, LocalDateTime.now().toString());
    }

    public static void addUser(String username, String password) throws SQLException {
        execute(DELETE_USER, username);
        execute(ADD_USER, username, password, String.valueOf(0));
    }

    public static void startContest() throws SQLException {
        execute(START_CONTEST);
    }

    public static User getUser(String username) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_USER, username);
        ResultSet rs = ps.executeQuery();
        User user = null;
        if (rs.next())
            user = new User(rs.getString(1), rs.getString(2), rs.getInt(3) == 1);
        rs.close();
        ps.close();
        return user;
    }

    public static List<User> getUsers() throws SQLException {
        PreparedStatement ps = prepareStatement(GET_USERS);
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<>();
        while (rs.next())
            if (rs.getInt(3) == 0)
                users.add(new User(rs.getString(1), rs.getString(2), rs.getInt(3) == 1));
        rs.close();
        ps.close();
        return users;
    }

    public static String getUsername(String loginID) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_USERNAME_LOGIN_ID, loginID);
        ResultSet rs = ps.executeQuery();
        String username = null;
        if (rs.next())
            username = rs.getString(1);
        rs.close();
        ps.close();
        return username;
    }

    public static int getTeamNumber() throws SQLException {
        PreparedStatement ps = prepareStatement(GET_TEAM_NUMBER);
        ResultSet rs = ps.executeQuery();
        int number = 0;
        if (rs.next())
            number = rs.getInt(1);
        rs.close();
        ps.close();
        return number;
    }

    public static String getAdminPassword() {
        return ADMIN_PASSWORD;
    }

    public static LocalDateTime getStart() throws SQLException {
        PreparedStatement ps = prepareStatement(GET_START);
        ResultSet rs = ps.executeQuery();
        String start = null;
        if (rs.next())
            start = rs.getString(1);
        rs.close();
        ps.close();
        return LocalDateTime.parse(start == null ? "" : start);
    }

    public static int getLength() throws SQLException {
        PreparedStatement ps = prepareStatement(GET_LENGTH);
        ResultSet rs = ps.executeQuery();
        int length = 0;
        if (rs.next())
            length = rs.getInt(1);
        rs.close();
        ps.close();
        return length;
    }

    public static void addSubmission(Submission submission) throws SQLException {
        execute(ADD_SUBMISSION, submission.username(), submission.problemID(), String.valueOf(submission.status()), submission.code(), submission.answer(), submission.submissionID(), submission.time());
    }

    public static List<Submission> getSubmissionByUser(String username) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_SUBMISSIONS_BY_USER, username);
        ResultSet rs = ps.executeQuery();
        ArrayList<Submission> submissions = new ArrayList<>();
        while (rs.next())
            submissions.add(new Submission(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        rs.close();
        ps.close();
        return submissions;
    }

    public static Map<String, Integer> getTeamScores() throws SQLException {
        Map<String, Integer> scores = new HashMap<>();
        for (User user : getUsers())
            scores.put(user.username(), 0);
        PreparedStatement ps = prepareStatement(GET_SUBMISSIONS);
        ResultSet rs = ps.executeQuery();
        Map<String, Problem> problems = new HashMap<>();
        for (Problem p : getProblems())
            problems.put(p.UUID(), p);
        while (rs.next()) {
            scores.computeIfPresent(rs.getString(1), (k, v) ->
            {
                try {
                    v += rs.getInt(3) == 1 ? problems.get(rs.getString(2)).value() : 0;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return v;
            });
        }
        rs.close();
        ps.close();
        return scores;
    }

    public static boolean isUserActive(String username) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_SESSION, username);
        ResultSet rs = ps.executeQuery();
        boolean active = rs.next();
        rs.close();
        ps.close();
        return active;
    }


    public static List<Submission> getSubmissions() throws SQLException {
        PreparedStatement ps = prepareStatement(GET_SUBMISSIONS);
        ResultSet rs = ps.executeQuery();
        ArrayList<Submission> submissions = new ArrayList<>();
        while (rs.next())
            submissions.add(new Submission(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        rs.close();
        ps.close();
        return submissions;
    }

    public static void setSubmissionPass(String submissionID) throws SQLException {
        execute(SET_SUBMISSION_STATUS, "1", submissionID);
    }

    public static void setSubmissionFail(String submissionID) throws SQLException {
        execute(SET_SUBMISSION_STATUS, "2", submissionID);
    }

    public static void deleteSubmission(String submissionID) throws SQLException {
        execute(DELETE_SUBMISSION, submissionID);
    }

    public static void setSubmissionAnswer(String submissionID, String answer) throws SQLException {
        execute(SET_SUBMISSION_ANSWER, answer, submissionID);
    }

    public static Submission getSubmissionByID(String UUID) throws SQLException {
        PreparedStatement ps = prepareStatement(GET_SUBMISSIONS_BY_ID, UUID);
        ResultSet rs = ps.executeQuery();
        Submission submission = null;
        if (rs.next()) {
            submission = new Submission(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
        }
        rs.close();
        ps.close();
        return submission;
    }

}