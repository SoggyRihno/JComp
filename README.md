# UIL Competition Server

A Java and Tomcat-based server application designed for UIL competitions, with SQLite for backend data management.

## Features

- **Admin Setup**: Configure competition settings and manage problems.
- **Team Login**: Teams can submit solutions, view scores, and download problem packets.
- **Real-Time Scoring**: Track scores and submissions live.

## Requirements

- Java Runtime Environment (JRE)
- Apache Tomcat Server

## Getting Started

### Clone the Repository

1. Open a terminal or command prompt.
2. Ensure Git is installed on your system. If not, [install Git](https://git-scm.com/).
3. Clone the repository from GitHub using the following command:
   ```
   git clone <repository-url>
   ```
   Replace `<repository-url>` with the URL of the repository.
4. Navigate to the cloned directory by running:
   ```
   cd <repository-name>
   ```
   Replace `<repository-name>` with the folder name of the cloned repository.

### Prepare the Project Files

1. Verify that all project files, including the preconfigured SQLite database, are present in the cloned repository.
2. If the project includes a `.war` file, locate it for deployment. If not, ensure the project structure supports Tomcat deployment.

### Deploy to Tomcat

1. Locate your Tomcat installation directory. If Tomcat is not installed, download it from the [Apache Tomcat website](https://tomcat.apache.org/) and follow the installation instructions.
2. Copy the `.war` file or the project folder into the `webapps` directory of your Tomcat installation.
   - For example:
     ```
     cp <path-to-war-file> <path-to-tomcat>/webapps/
     ```
3. Start the Tomcat server:
   - On Linux/Mac:
     ```
     <path-to-tomcat>/bin/startup.sh
     ```
   - On Windows:
     ```
     <path-to-tomcat>\bin\startup.bat
     ```
4. Open your browser and navigate to `http://localhost:8080/<application-name>` to access the application.
   - Replace `<application-name>` with the name of the deployed application.
5. Confirm that the application is running successfully.

## Admin Setup Page

1. Navigate to the setup page.
2. Configure the following settings:
   - **Number of Teams**: Set the total number of participating teams.
   - **Time Length**: Specify the duration of the competition.
   - **Add Problems**:
     - Provide a description of the problem (question text).
     - Include problem input.
     - Define the expected output for each problem.
3. Copy the provided admin password (default username: `admin`).
4. Click "Start Contest" to begin.

## Admin Login

1. Log in with the provided admin credentials (Username: admin).
2. Access the following features:
   - **Login details for each team**
   - **Scores**: Monitor team scores.
   - **Submissions Page**: Manage the contest by reviewing and modifying submission results.

## Team Login

1. Use the assigned team credentials (e.g., username: `team1`).
2. Navigate through the following sections:
   - **Home**:
     - View current score.
     - Check time left in the competition.
     - Monitor the status of problems.
   - **Leaderboard**: View team rankings in real-time.
   - **Submission**:
     - Submit solutions to problems.
     - Review past submissions.
   - **Downloads**: Download the problem packet.

