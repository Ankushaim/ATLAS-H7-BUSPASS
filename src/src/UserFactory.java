import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserFactory {
    String userId;
    String userName;

    public UserFactory(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    RouteMaster calling_route = new RouteMaster();

    public void viewRoute() {
        calling_route.viewAllRoutes();
    }

    //To be decided..
    public void viewStops() {
        calling_route.viewAllStops();
    }

    static void printOptionsUserDetailsMethod() {
        System.out.println();
        System.out.println("Select Details to Update");
        System.out.print("1. Phone Number: " + "\t");
        System.out.print("2. Address: " + "\t");
        System.out.println("3. City: "+ "\t");
        System.out.print("4. Stop: " + "\t");
        System.out.print("5. Password: "+ "\t");
        System.out.println("6. go to previous Menu: ");
    }

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    void updateUserDetails() {
        Connection con = JdbcConnect.connect();
        HashMap<String, String> userDetails = new HashMap<>();
        printOptionsUserDetailsMethod();
        boolean flag = true;

        while(flag) {
            System.out.print("Input: ");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Phone Number: ");
                    Scanner phone_num = new Scanner(System.in);
                    userDetails.put("phone_num", Integer.toString(phone_num.nextInt()));
                    printOptionsUserDetailsMethod();
                    break;
                case 2:
                    System.out.print("Enter Address: ");
                    Scanner address = new Scanner(System.in);
                    userDetails.put("address", address.nextLine());
                    pressAnyKeyToContinue();
                    printOptionsUserDetailsMethod();
                    break;
                case 3:
                    System.out.print("Enter City: ");
                    Scanner city = new Scanner(System.in);
                    userDetails.put("city", city.nextLine());
                    pressAnyKeyToContinue();
                    printOptionsUserDetailsMethod();
                    break;
                case 4:
                    boolean check = false;
                    try {
                        check = new RouteMaster().selectStop(userId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (check) {
                        System.out.println("Please wait for Admin's approval");
                        System.out.println("As the stop is changed your pass is temprory deactivted for Admin approval.");
                    }
                    else {
                        System.out.println("Please try again :-(");
                    }
                    pressAnyKeyToContinue();
                    printOptionsUserDetailsMethod();
                    break;
                case 5:
                    System.out.print("Enter Password: ");
                    Scanner password = new Scanner(System.in);
                    userDetails.put("password", password.nextLine());
                    pressAnyKeyToContinue();
                    printOptionsUserDetailsMethod();
                    break;
                case 6:
                    flag = false;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.now();
                    userDetails.put("change_date", dtf.format(localDate));
                    break;
                default:
                    printOptionsUserDetailsMethod();
            }
        }

        for(Map.Entry<String, String> m:userDetails.entrySet()) {
            String sql = "UPDATE user_info SET " + m.getKey() + " = '" + m.getValue() + "' where login = '" + userId + "'";
            if (con != null) {
                try {
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void printMyPass() {
        Connection con = JdbcConnect.connect();
        String sqlQuery1 = "SELECT pass_id, bus_id, route from pass_details WHERE user_id = ?";
        String sqlQuery2 = "SELECT stop from user_info WHERE login = ?";
        try {
            PreparedStatement ps1 = con.prepareStatement(sqlQuery1);
            PreparedStatement ps2 = con.prepareStatement(sqlQuery2);
            ps1.setString(1, userId);
            ps2.setString(1, userId);

            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            if (rs1.isBeforeFirst()) {
                System.out.println("\n" + "\n" + "\t\t\t" + "ATS BUS PASS" + "\t\t\t" + "\n");
                System.out.println("PassId: " + rs1.getInt("pass_id") + "\t\t\t\t" + "BusId: " + rs1.getInt("bus_id"));
                System.out.println("Name: " + userName);
                System.out.println("Stop: " + rs2.getString("stop") + "\t\t\t\t" + "Route: " + rs1.getString("route"));
                System.out.println();
            }
            ps1.close();
            ps2.close();
            rs1.close();
            rs2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        new UserFactory("ankusha", "ankush").printMyPass();
//    }
}

