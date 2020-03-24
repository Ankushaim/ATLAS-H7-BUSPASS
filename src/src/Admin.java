import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    String userId;
    String adminName = null;
    Connection con = JdbcConnect.connect();

    public Admin(String userId) {
        if (con != null) {
            String sql = "select user_name from user_info where login = ?";
            try {
                PreparedStatement pstSel = con.prepareStatement(sql);
                pstSel.setString(1, userId);
                ResultSet rs = pstSel.executeQuery();
                this.adminName = rs.getString("user_name");
            } catch (SQLException e) {System.out.println(e.getMessage());}
            finally {JdbcConnect.closeCon(con);}
        }
        this.userId = userId;
        view_controller_admin();
    }

    static void printOptionsAdmin() {
        System.out.println();
        System.out.print("1. View Requests: " + "\t");
        System.out.print("2. Generate Reports: " + "\t");
        System.out.println("3. Add New Route: " + "\t");
        System.out.print("4. Remove existing Route: " + "\t");
        System.out.print("5. Change Bus type on Routes: " + "\t");
        System.out.println("6. Assign Available Bus on Route: " + "\t");
        System.out.print("7. View Vehicle List of different Type: " + "\t");
        System.out.print("8. Register a Bus to Company: " + "\t");
        System.out.println("9. To previous Menu: " + "\t");
        System.out.println("10. To Logout: " + "\t");
    }

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    void view_controller_admin() {
        System.out.println("Welcome " + adminName);
        AdminFactory calling_admin = new AdminFactory();
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
                    choice = input.nextInt();
                    error = false;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input :-( only Integers allowed");
                    error = true;
                }
            } while (error);

            switch (choice) {
                case 1:
                    calling_admin.viewRequests();
                    pressAnyKeyToContinue();
                    printOptionsAdmin();
                    break;
//                case 3:
//                    calling_admin.addNewRoute();
//                    pressAnyKeyToContinue();
//                    printOptionsAdmin();
//                    break;
                case 4:
                    if (calling_admin.removeRoute())
                        System.out.println("Route Removed Successfully");
                    else
                        System.out.println("Route can not be removed please check if any bus/user active on route");
                    pressAnyKeyToContinue();
                    printOptionsAdmin();
                    break;
                case 5:
                    calling_admin.ChangeVehicleTypeofRoute();
                    pressAnyKeyToContinue();
                    printOptionsAdmin();
                    break;
                case 6:
                    calling_admin.assignBusToRoute();
                    pressAnyKeyToContinue();
                    printOptionsAdmin();
                    break;
                case 8:
                    calling_admin.registerBus();
                    pressAnyKeyToContinue();
                    printOptionsAdmin();
                    break;
                case 9:
                    flag = false;
                    break;
                case 10:
                    System.exit(0);
                default:
                    System.out.print("Select valid activity to perform");
                    printOptionsAdmin();
            }
        }
    }
}
