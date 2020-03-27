import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin extends Profile {
    String userId;
    String adminName = null;

    public Admin(String userId) {
        //this.conn = conn;
        if (conn != null) {
            String sql = "select user_name from user_info where login = ?";
            try {
                PreparedStatement pstSel = conn.prepareStatement(sql);
                pstSel.setString(1, userId);
                ResultSet rs = pstSel.executeQuery();
                while (rs.next())
                    this.adminName = rs.getString("user_name");
                rs.close();
                pstSel.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        this.userId = userId;
    }

    void printOptions() {
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

    String UserRouteValue() {
        Scanner input = new Scanner(System.in);
        SQLSelect sel = new SQLSelect();
        ArrayList<String> routes = new ArrayList<>();
        String sql = "select distinct route from route_info where route is not null";

        try {
            ResultSet rs2 = sel.SqlSelectStatement(sql);
            while (rs2.next())
                routes.add(rs2.getString("route"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Available routes are:");
        for (String s : routes) {
            System.out.print(s + "\t");
        }
        String route;
        do {
            System.out.print("\n" + "Input: ");
            route = input.nextLine().toUpperCase();
        } while (!routes.contains(route));
        return route;
    }

    void viewController() {
        System.out.println("\n" + "Welcome " + adminName);
        printOptions();
        BusMaster busMaster = new BusMaster(conn);
        AdminRouteMaster callingAdminRoute = new AdminRouteMaster(conn);
        ViewRequests viewRequest = new ViewRequests(conn);
        Scanner input;
        String route;
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
                    try {
                        viewRequest.PendingBusPassRequests();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 3:
                    callingAdminRoute.addNewRoute();
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 4:
                    if (callingAdminRoute.removeRoute())
                        System.out.println("Route Removed Successfully");
                    else
                        System.out.println("Route can not be removed please check if any bus/user active on route");
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 5:
                    route = UserRouteValue();
                    try {
                        busMaster.ChangeBusTypeOfRoute(route);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 6:
                    route = UserRouteValue();
                    try {
                        busMaster.AddBusInRoute(route);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 7:
                    try {
                        String sel = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is not null group by 1";
                        busMaster.vehicleDifferentTypes(sel);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 8:
                    busMaster.registerBus();
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 9:
                    flag = false;
                    break;
                case 10:
                    System.exit(0);
                default:
                    System.out.print("Select valid activity to perform");
                    printOptions();
            }
        }
    }
}
