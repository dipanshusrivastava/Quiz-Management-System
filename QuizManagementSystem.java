import java.util.InputMismatchException;
import java.util.Scanner;
import Student.*;
import Admin.*;

public class QuizManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Creating instances of Student and Admin
        Student student = new Student();
        Admin admin = new Admin();

        while (true) {
            System.out.println("Welcome to Quiz Management System!");
            System.out.println("1. Admin Login");
            System.out.println("2. Student Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            try {
                int userTypeChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (userTypeChoice) {
                    case 1:
                        admin.adminLogin(scanner);
                        break;
                    case 2:
                        student.studentLogin(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting the program. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

    }
}
