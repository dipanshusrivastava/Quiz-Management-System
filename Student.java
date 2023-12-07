package Student;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import Admin.*;

public class Student extends Admin {

    private static final String STUDENT_USERNAME = "student";
    private static final String STUDENT_PASSWORD = "student123";

    private static final int MAX_USERS = 10;
    private static final int MAX_QUIZZES = 10;
    private static final int MAX_QUESTIONS = 10;
    private static final int MAX_OPTIONS = 4;

    private static String[] usernames = new String[MAX_USERS];
    private static String[] passwords = new String[MAX_USERS];
    private static String[][] questionsAndOptions = new String[MAX_QUIZZES * MAX_QUESTIONS][MAX_OPTIONS + 2];

    private static int userCount = 0;
    private static int[] studentScores = new int[MAX_USERS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Student student = new Student();
        student.studentLogin(scanner);
        scanner.close();
    }

    public void studentLogin(Scanner scanner) {
        System.out.print("Enter student username: ");
        String enteredStudentUsername = scanner.nextLine();
        System.out.print("Enter student password: ");
        String enteredStudentPassword = scanner.nextLine();

        if (STUDENT_USERNAME.equals(enteredStudentUsername) && STUDENT_PASSWORD.equals(enteredStudentPassword)) {
            studentMenu(scanner, enteredStudentUsername);
        } else {
            System.out.println("Invalid student credentials. Please try again.");
        }
    }

    private void studentMenu(Scanner scanner, String studentUsername) {
        while (true) {
            System.out.println("Welcome, " + studentUsername + "!");
            System.out.println("Student Menu:");
            System.out.println("1. Take Quiz");
            System.out.println("2. View Result");
            System.out.println("3. Exit");

            try {
                System.out.print("Choose an option: ");
                int studentChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (studentChoice) {
                    case 1:
                        takeQuiz(studentUsername, scanner);
                        break;
                    case 2:
                        viewResult(studentUsername);
                        break;
                    case 3:
                        System.out.println("Exiting the student menu. Goodbye, " + studentUsername + "!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    public void takeQuiz(String studentUsername, Scanner scanner) {
        if (Admin.quizCount == 0) {
            System.out.println("No quizzes available at the moment. Please check back later.");
            return;
        }

        System.out.println("Available Quizzes:");
        for (int i = 0; i < Admin.quizCount; i++) {
            System.out.println((i + 1) + ". " + Admin.quizzes[i]);
        }

        System.out.print("Choose a quiz to take (enter the quiz number): ");
        int quizNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        int quizIndex = quizNumber - 1;

        if (quizIndex >= 0 && quizIndex < Admin.quizCount) {
            int score = 0;
            for (int i = 0; i < MAX_QUESTIONS
                    && Admin.questionsAndOptions[quizIndex * MAX_QUESTIONS + i][0] != null; i++) {
                System.out.println(
                        "Question " + (i + 1) + ": " + Admin.questionsAndOptions[quizIndex * MAX_QUESTIONS + i][0]);
                System.out.println("Options:");
                for (int j = 1; j <= MAX_OPTIONS; j++) {
                    System.out.println("   " + (char) ('A' + j - 1) + ". "
                            + Admin.questionsAndOptions[quizIndex * MAX_QUESTIONS + i][j]);
                }

                System.out.print("Your answer (enter the option letter): ");
                char studentAnswer = scanner.next().charAt(0);
                scanner.nextLine(); // Consume the newline character

                char correctAnswer = (char) ('A' + findCorrectOption(quizIndex, i));
                if (Character.toUpperCase(studentAnswer) == correctAnswer) {
                    System.out.println("Correct! +10 points");
                    score += 10;
                } else {
                    System.out.println("Incorrect.");
                }
            }

            System.out.println("Quiz completed. Your score: " + score + " out of " + (MAX_QUESTIONS * 10));

            int userIndex = findUser(studentUsername);
            studentScores[userIndex] = score;
        } else {
            System.out.println("Invalid quiz number. Please enter a valid quiz number.");
        }
    }

    private void viewResult(String studentUsername) {
        int userIndex = findUser(studentUsername);

        if (userIndex != -1) {
            if (studentScores[userIndex] != 0) {
                System.out.println("Your result: " + studentScores[userIndex] + " out of " + (MAX_QUESTIONS * 10));
            } else {
                System.out.println("You haven't taken any quizzes yet.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }

}
