import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    String userId;
    String userName = null;

    User(String userId) {
        JdbcConnect jbc = new JdbcConnect();
        if (jbc.connect() != null) {
            String sql = "select user_name from user_info where login = '" + userId + "'";
            try (Statement stmt = jbc.connect().createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                this.userName = rs.getString("user_name");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        this.userId = userId;
        view_controller_user();
    }

    static void printOptionsUser() {
        System.out.println();
        System.out.print("1. Edit or Change details: " + "\t");//done
        System.out.print("2. View all routes: "+ "\t"); //done
        System.out.println("3. Show my route : "+ "\t"); //done
        System.out.print("4. Request to cancel the Bus Pass: " + "\t");
        System.out.print("5. Request to suspend the pass: " + "\t");
        System.out.println("6. Request for new route: " + "\t");
        System.out.print("7. Print your pass: " + "\t");
        System.out.println("8. go to previous Menu: " + "\t");
    }

    static void pressAnyKeyToContinue()
    {
        System.out.println("Press Enter/Return key to continue...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {System.out.println("Enter/Return Exception");}
    }

    void view_controller_user() {
        System.out.println("Welcome " + userName);
        UserFactory calling_user = new UserFactory(userId);
        Scanner input;

        System.out.println("Select appropriate activity to perform");
        printOptionsUser();
        boolean flag = true;
        boolean error;
        while (flag) {

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
                    calling_user.updateUserDetails();
                    pressAnyKeyToContinue();
                    printOptionsUser();
                    break;
                case 2:
                    calling_user.viewRoute();
                    pressAnyKeyToContinue();
                    printOptionsUser();
                    break;
                case 3:
                    calling_user.viewStops();// To be decided..
                    pressAnyKeyToContinue();
                    printOptionsUser();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    flag = false;
                    break;
                case 9:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Select valid activity to perform");
                    System.out.println("1. Edit or Change details: ");
                    System.out.println("2. View all routes: ");
                    System.out.println("3. Show my route : ");
                    System.out.println("4. Request to cancel the Bus Pass: ");
                    System.out.println("5. Request to suspend the pass: ");
                    System.out.println("5. Request for new route: ");
                    System.out.println("6. Print your pass: ");
                    System.out.println("7. go to previous Menu: ");
            }

        }
    }
}
