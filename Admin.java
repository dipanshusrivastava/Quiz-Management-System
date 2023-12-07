package Admin;
import java.util.InputMismatchException;
import java.util.Scanner;

class Admin {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private static final int MAX_USERS = 10;
    private static final int MAX_QUIZZES = 10;
    private static final int MAX_QUESTIONS = 10;
    private static final int MAX_OPTIONS = 4;

    public static int userCount = 0;
    public static int quizCount = 0;

    private static String[] usernames = new String[MAX_USERS];
    private static String[] passwords = new String[MAX_USERS];

    public static String[] quizzes = new String[MAX_QUIZZES];
    public static String[][] questionsAndOptions = new String[MAX_QUIZZES * MAX_QUESTIONS][MAX_OPTIONS + 2];

    public void adminLogin(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();

        if (ADMIN_USERNAME.equals(adminUsername) && ADMIN_PASSWORD.equals(adminPassword)) {
            showAdminMenu(scanner);
            userCount++;
        } else {
            System.out.println("Invalid admin credentials. Please try again.");
        }
    }

    private static void showAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Create Quiz");
            System.out.println("2. Add Question");
            System.out.println("3. Delete Question");
            System.out.println("4. Update Question");
            System.out.println("5. Show Quiz");
            System.out.println("6. Show Results");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            try {
                int adminChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (adminChoice) {
                    case 1:
                        createQuiz(scanner);
                        break;
                    case 2:
                        addQuestion(scanner);
                        break;
                    case 3:
                        deleteQuestion(scanner);
                        break;
                    case 4:
                        updateQuestion(scanner);
                        break;
                    case 5:
                        showQuiz(scanner);
                        break;
                    case 6:
                        showResults(scanner);
                        break;
                    case 7:
                        System.out.println("Exiting Admin menu.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                showAdminMenu(scanner);

                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    public static void createQuiz(Scanner scanner) {
        if (quizCount < MAX_QUIZZES) {
            System.out.print("Enter quiz name: ");
            String quizName = scanner.nextLine();

            if (!containsQuiz(quizName)) {
                quizzes[quizCount] = quizName;
                quizCount++;
                System.out.println("Quiz created successfully.");
            } else {
                System.out.println("Quiz with the same name already exists.");
            }
        } else {
            System.out.println("Maximum number of quizzes reached.");
        }
    }

    public static boolean containsQuiz(String quizName) {
        for (int i = 0; i < quizCount; i++) {
            if (quizzes[i] != null && quizzes[i].equals(quizName)) {
                return true;
            }
        }
        return false;
    }

    private static void addQuestion(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();
    
        int quizIndex = findQuiz(quizName);
    
        if (quizIndex != -1) {
            try {
                System.out.print("Enter the number of questions you want to add: ");
                int numberOfQuestions = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after nextInt()
    
                for (int k = 0; k < numberOfQuestions; k++) {
                    for (int i = 0; i < MAX_QUESTIONS; i++) {
                        int questionIndex = quizIndex * MAX_QUESTIONS + i;
                        if (questionsAndOptions[questionIndex][0] == null) {
                            System.out.print("Enter question " + (i + 1) + ": ");
                            String question = scanner.nextLine();
                            questionsAndOptions[questionIndex][0] = question;
    
                            for (int j = 1; j <= MAX_OPTIONS; j++) {
                                System.out.print("Enter option " + j + ": ");
                                String option = scanner.nextLine();
                                questionsAndOptions[questionIndex][j] = option;
                            }
    
                            // Prompt user to specify the correct options
                            System.out.print("Enter the number(s) of the correct option(s) separated by commas (1-" + MAX_OPTIONS + "): ");
                            String[] correctOptionNumbers = scanner.nextLine().split(",");
    
                            // Validate correct option numbers
                            boolean validOptions = true;
                            for (String correctOptionNumber : correctOptionNumbers) {
                                int optionNum = Integer.parseInt(correctOptionNumber.trim());
                                if (optionNum < 1 || optionNum > MAX_OPTIONS) {
                                    validOptions = false;
                                    break;
                                }
                            }
    
                            if (validOptions) {
                                // Mark the correct options
                                for (String correctOptionNumber : correctOptionNumbers) {
                                    int optionNum = Integer.parseInt(correctOptionNumber.trim());
                                    questionsAndOptions[questionIndex][optionNum] = "Correct";
                                }
                                System.out.println("Question and options added to the quiz.");
                            } else {
                                System.out.println("Invalid correct option number(s). Please try again.");
                                // Clear the added question and options
                                questionsAndOptions[questionIndex] = new String[MAX_OPTIONS + 2];
                                i--; // Decrement i to re-enter the current question
                            }
    
                            break;
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        } else {
            System.out.println("Quiz not found.");
        }
    }
    

    public static int findQuiz(String quizName) {
        for (int i = 0; i < quizCount; i++) {
            if (quizzes[i] != null && quizzes[i].equals(quizName)) {
                return i;
            }
        }
        return -1; // Quiz not found
    }

    private static void deleteQuestion(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();

        int quizIndex = findQuiz(quizName);

        if (quizIndex != -1) {
            if (questionsAndOptions[quizIndex * MAX_QUESTIONS][0] != null) {
                System.out.println("Questions in the quiz:");
                for (int i = 0; i < MAX_QUESTIONS
                        && questionsAndOptions[quizIndex * MAX_QUESTIONS + i][0] != null; i++) {
                    System.out.println((i + 1) + ". " + questionsAndOptions[quizIndex * MAX_QUESTIONS + i][0]);
                }

                System.out.print("Enter the number(s) of the question(s) to delete (comma-separated): ");
                String input = scanner.nextLine();
                String[] deleteNumbers = input.split(",");

                for (String deleteNumber : deleteNumbers) {
                    int questionNumber = Integer.parseInt(deleteNumber.trim());

                    if (questionNumber > 0 && questionNumber <= MAX_QUESTIONS
                            && questionsAndOptions[quizIndex * MAX_QUESTIONS + questionNumber - 1][0] != null) {
                        for (int i = questionNumber - 1; i < MAX_QUESTIONS - 1; i++) {
                            for (int j = 0; j < MAX_OPTIONS + 2; j++) {
                                questionsAndOptions[quizIndex * MAX_QUESTIONS
                                        + i][j] = questionsAndOptions[quizIndex * MAX_QUESTIONS + i + 1][j];
                            }
                        }
                        questionsAndOptions[(quizIndex + 1) * MAX_QUESTIONS - 1] = new String[MAX_OPTIONS + 2];
                        System.out.println("Question(s) deleted successfully.");
                    } else {
                        System.out.println("Invalid question number: " + questionNumber);
                    }
                }
            } else {
                System.out.println("No questions found in the quiz.");
            }
        } else {
            System.out.println("Quiz not found. Please create the quiz first.");
        }
    }

    private static void updateQuestion(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();

        int quizIndex = findQuiz(quizName);

        if (quizIndex != -1) {
            System.out.print("Enter question number to update (1 to " + MAX_QUESTIONS + "): ");
            int questionNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            int questionIndex = (quizIndex * MAX_QUESTIONS) + (questionNumber - 1);

            if (questionsAndOptions[questionIndex][0] != null) {
                System.out.println("Current question: " + questionsAndOptions[questionIndex][0]);
                System.out.println("Current options:");

                for (int i = 1; i <= MAX_OPTIONS; i++) {
                    System.out.println(i + ". " + questionsAndOptions[questionIndex][i]);
                }

                System.out.print("Do you want to update the question? (yes/no): ");
                String updateQuestionOption = scanner.nextLine().toLowerCase();

                if (updateQuestionOption.equals("yes")) {
                    System.out.print("Enter the new question: ");
                    String newQuestion = scanner.nextLine();
                    questionsAndOptions[questionIndex][0] = newQuestion;
                }

                System.out.print("Do you want to update the options? (yes/no): ");
                String updateOptionsOption = scanner.nextLine().toLowerCase();

                if (updateOptionsOption.equals("yes")) {
                    for (int i = 1; i <= MAX_OPTIONS; i++) {
                        System.out.print("Enter option " + i + ": ");
                        String newOption = scanner.nextLine();
                        questionsAndOptions[questionIndex][i] = newOption;
                    }
                }

                System.out.println("Question and options updated successfully.");
            } else {
                System.out.println("Question not found.");
            }
        } else {
            System.out.println("Quiz not found.");
        }
    }

    private static void showQuiz(Scanner scanner) {
        System.out.print("Enter quiz name: ");
        String quizName = scanner.nextLine();

        int quizIndex = findQuiz(quizName);

        if (quizIndex != -1) {
            System.out.println("\nQuestions and Options for Quiz: " + quizName);
            for (int i = 0; i < MAX_QUESTIONS && questionsAndOptions[quizIndex * MAX_QUESTIONS + i][0] != null; i++) {
                System.out.println((i + 1) + ". " + questionsAndOptions[quizIndex * MAX_QUESTIONS + i][0]);
                for (int j = 1; j <= MAX_OPTIONS; j++) {
                    System.out.println("   Option " + j + ": " + questionsAndOptions[quizIndex * MAX_QUESTIONS + i][j]);
                }
            }
        } else {
            System.out.println("Quiz not found.");
        }
    }

    private static void showResults(Scanner scanner) {
        System.out.print("Enter student username: ");
        String studentUsername = scanner.nextLine();
    
        int userIndex = findUser(studentUsername);
    
        if (userIndex != -1) {
            if (studentScores[userIndex] != -1) {
                System.out.println("Student: " + studentUsername);
                System.out.println("Result: " + studentScores[userIndex] + " out of " + (MAX_QUESTIONS * 10));
            } else {
                System.out.println("Student " + studentUsername + " hasn't taken any quizzes yet.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }


    
}


