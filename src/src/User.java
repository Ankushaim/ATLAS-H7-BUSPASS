import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    String userId;
    String userName = null;
    Connection con = JdbcConnect.connect();
    User(String userId) {

        if (con != null) {
            String sql = "select user_name from user_info where login = ?";
            try {
                PreparedStatement pstSel = con.prepareStatement(sql);
                pstSel.setString(1,userId);
                ResultSet rs = pstSel.executeQuery();
                this.userName = rs.getString("user_name");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            finally {JdbcConnect.closeCon(con);}
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
        System.out.println("9. To logOut: ");
    }

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    void view_controller_user() {
        System.out.println("Welcome " + userName);
        UserFactory calling_user = new UserFactory(userId, userName);

        System.out.println("Select appropriate activity to perform");
        printOptionsUser();
        boolean flag = true;
        boolean error;
        while (flag) {

            int choice = 0;
            do {
                try {
                    System.out.print("Input: ");
                    Scanner input = new Scanner(System.in);
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
                case 7:
                    calling_user.printMyPass();
                    pressAnyKeyToContinue();
                    printOptionsUser();
                    break;
                case 8:
                    flag = false;
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.print("Select valid activity to perform");
                    printOptionsUser();
            }
        }
    }
}
