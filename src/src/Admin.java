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
        System.out.print("2. Appoint/Change Bus Driver: "+ "\t");
        System.out.println("3. Generate Report: "+ "\t");
        System.out.print("4. Add Remove Routes: " + "\t");
        System.out.print("5. Change Bus type on Routes: " + "\t");
        System.out.println("6. Assign Available Bus on Route: " + "\t");
        System.out.print("7. View Vehicle List of different Type: " + "\t");
        System.out.print("8. Register a Bus to Company: " + "\t");
        System.out.println("9. To previous Menu: " + "\t");
        System.out.println("10. To Logout: " + "\t");
    }

    static void pressAnyKeyToContinue() {
        System.out.println("Press Enter/Return key to continue...");
        try
        {
            System.in.read();
        } catch(Exception e) {System.out.println("Enter/Return Exception");}
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
