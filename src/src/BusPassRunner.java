import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusPassRunner {
    static Connection conn = JdbcConnect.connect();

    void printOptionsMain() {
        System.out.println("\n" + "Welcome to Amazon Transport Service Portal");
        System.out.println("To continue please select the below:");
        System.out.println("1. Admin");
        System.out.println("2. Registered User");
        System.out.println("3. Visitor");
        System.out.println("4. To Logout");
    }

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void main(String[] args) throws SQLException {
        BusPassRunner runObj = new BusPassRunner();
        Authentication authCheck = new Authentication(conn);
        Scanner credential_input = new Scanner(System.in);
        Scanner input;
        runObj.printOptionsMain();

        String userId;
        String password;
        boolean flag = true;
        boolean error;
        while(flag) {
            int choice = 0;
            do {
                try {
                    System.out.print("Input: ");
                    input = new Scanner(System.in);
                    choice=input.nextInt();
                    error=false;
                } catch(InputMismatchException e) {
                    System.out.println("Invalid Input :-( only Integers allowed");
                    error=true;
                }
            } while(error);

            switch (choice) {
                case 1:
                    System.out.println("Sign in to continue to ATS Portal");
                    System.out.print("UserId: ");
                    userId = credential_input.nextLine();
                    System.out.print("Password: ");
                    password = credential_input.nextLine();
                    if (authCheck.checkCredentials(userId, password, "admin")) {
                        new Admin(userId, conn);
                    } else{
                        System.out.println("Invalid Credentials");
                        System.exit(0);
                    }
                    pressAnyKeyToContinue();
                    runObj.printOptionsMain();
                    break;
                case 2:
                    System.out.println("Sign in to continue to ATS Portal");
                    System.out.print("UserId: ");
                    userId = credential_input.nextLine();
                    System.out.print("Password: ");
                    password = credential_input.nextLine();
                    if (authCheck.checkCredentials(userId, password, "user")) {
                        new User(userId, conn);
                    } else{
                        System.out.println("Invalid Credentials");
                        System.exit(0);
                    }
                    pressAnyKeyToContinue();
                    runObj.printOptionsMain();
                    break;
                case 3:
                    new Guest(conn);
                    pressAnyKeyToContinue();
                    runObj.printOptionsMain();
                    break;
                case 4:
                    System.out.println("Thanks for visiting");
                    credential_input.close();
                    flag = false;
                    break;
                default:
                    System.out.println("\n"+ "Please provide valid input:");
                    System.out.println("1. Admin");
                    System.out.println("2. Registered");
                    System.out.println("3. Visitor");
                    System.out.println("4. To Logout");
            }
        }
    }
}