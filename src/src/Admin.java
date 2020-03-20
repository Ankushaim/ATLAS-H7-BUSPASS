import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    String userId;
    String adminName = null;
    Connection con = JdbcConnect.connect();

    public Admin(String userId) {
        if (con != null) {
            String sql = "select user_name from user_info where login = ?";
            //String sql = "select user_name from user_info where login = '" + userId + "'";
            try {
                PreparedStatement pstSel = con.prepareStatement(sql);
                pstSel.setString(1, userId);
                ResultSet rs = pstSel.executeQuery();
                this.adminName = rs.getString("user_name");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        this.userId = userId;
        view_controller_admin();
    }

    static void printOptionsAdmin() {
        System.out.println();
        System.out.print("1. View Requests: " + "\t");
        System.out.print("2. Appoint/Change Bus Driver: "+ "\t"); // ankush
        System.out.println("3. Generate Report: "+ "\t");
        System.out.print("4. Add Remove Routes: " + "\t"); // Priyank
        System.out.print("5. Change Bus type on Routes: " + "\t"); // J needs to be done
        System.out.println("6. Assign Available Bus on Route: " + "\t"); // almost done J
        System.out.print("7. View Vehicle List of different Type: " + "\t"); // almost done J
        System.out.print("8. Register a Bus to Company: " + "\t"); // Done working
        System.out.println("9. To previous Menu: " + "\t"); // Done
        System.out.println("10. To Logout: " + "\t"); // Done
    }

    static void pressAnyKeyToContinue() {
        System.out.println("Press Enter/Return key to continue...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {System.out.println("Enter/Return Exception");}
    }

    void view_controller_admin() {
        System.out.println("Welcome " + adminName);
        AdminFactory calling_admin = new AdminFactory(userId);
        Scanner input;
        printOptionsAdmin();
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
                case 8:
                    calling_admin.registerBus(con);
                    pressAnyKeyToContinue();
                    printOptionsAdmin();
                    break;
                case 9:
                    flag = false;
                    break;
                case 10:
                    System.exit(0);
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
