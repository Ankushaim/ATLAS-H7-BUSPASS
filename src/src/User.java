import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class User extends Profile {
    String userId;
    String userName = null;
    UserRouteMaster userRoute = new UserRouteMaster();

    User(String userId) {
        if (conn != null) {
            String sql = "select user_name from user_info where login = ?";
            try {
                PreparedStatement pstSel = conn.prepareStatement(sql);
                pstSel.setString(1, userId);
                ResultSet rs = pstSel.executeQuery();
                while (rs.next())
                    this.userName = rs.getString("user_name");
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
        System.out.print("1. Edit or Change details: " + "\t");//done
        System.out.print("2. View all routes: " + "\t"); //done
        System.out.println("3. Show my route : " + "\t"); //done
        System.out.print("4. Request to cancel the Bus Pass: " + "\t");
        System.out.print("5. Request to suspend the pass: " + "\t");
        System.out.println("6. Request for new route: " + "\t");
        System.out.print("7. Print your pass: " + "\t");
        System.out.println("8. go to previous Menu: " + "\t");
        System.out.println("9. To logOut: ");
    }

    void printOptionsUserDetailsMethod() {
        System.out.println();
        System.out.println("Select Details to Update");
        System.out.print("1. Phone Number: " + "\t");
        System.out.print("2. Address: " + "\t");
        System.out.println("3. City: " + "\t");
        System.out.print("4. Stop: " + "\t");
        System.out.print("5. Password: " + "\t");
        System.out.println("6. go to previous Menu: ");
    }

    void updateUserDetails() {
        HashMap<String, String> userDetails = new HashMap<>();
        printOptionsUserDetailsMethod();
        boolean flag = true;

        while (flag) {
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
                        check = userRoute.selectStop(userId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (check) {
                        System.out.println("Please wait for Admin's approval");
                        System.out.println("As the stop is changed your pass is temprory deactivted for Admin approval.");
                    } else {
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

        for (Map.Entry<String, String> m : userDetails.entrySet()) {
            String sql = "UPDATE user_info SET " + m.getKey() + " = '" + m.getValue() + "' where login = '" + userId + "'";
            if (conn != null) {
                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void printMyPass() {
        String sqlQuery1 = "SELECT pass_id, bus_id, route from pass_details WHERE login = ?";
        String sqlQuery2 = "SELECT stop, date(change_date) as change_date from user_info WHERE login = ? AND status = ?";
        try {
            PreparedStatement ps1 = conn.prepareStatement(sqlQuery1);
            PreparedStatement ps2 = conn.prepareStatement(sqlQuery2);
            ps1.setString(1, userId);
            ps2.setString(1, userId);
            ps2.setString(2, "APPROVED");

            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.isBeforeFirst() && rs1.isBeforeFirst()) {
                while (rs1.next() && rs2.next()) {
                    System.out.println("\n" + "\t\t\t" + "ATS BUS PASS" + "\t\t\t");
                    System.out.println("DATE: " + rs2.getString("change_date"));
                    System.out.println("Name: " + userName);
                    System.out.println("PassId: " + rs1.getInt("pass_id") + "\t\t\t\t" + "BusId: " + rs1.getInt("bus_id"));
                    System.out.println("Stop: " + rs2.getString("stop") + "\t\t\t\t" + "Route: " + rs1.getString("route"));
                    System.out.println();
                }
            } else {
                System.out.println("No Pass found Please contact Facility");
            }
            ps1.close();
            ps2.close();
            rs1.close();
            rs2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void cancelPass(String login) {
        ArrayList<String> sql = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();

        sql.add("DELETE from pass_details where login = '" + login + "'");
        sql.add("UPDATE user_info SET status = 'INACTIVE', change_date = '" + dtf.format(localDate) + "' WHERE login= '" + login + "' ");

        if (conn != null) {
            for (String obj : sql) {
                try (PreparedStatement pstmt = conn.prepareStatement(obj)) {
                    if (pstmt.executeUpdate() == 0) { //if update unsuccessful throw error
                        System.out.println("Operation failed" + "\n" + "User INACTIVE");
                        return;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Pass Canceled for :" + login + "\n");
    }

    void viewController() {
        System.out.println("\n" + "Welcome " + userName);
        printOptions();
        boolean flag = true;
        boolean error;
        while (flag) {
            int choice = 0;
            do {
                try {
                    System.out.print("Input: ");
                    Scanner input = new Scanner(System.in);
                    choice = input.nextInt();
                    error=false;
                } catch(InputMismatchException e) {
                    System.out.println("Invalid Input :-( only Integers allowed");
                    error=true;
                }
            } while(error);

            switch (choice) {
                case 1:
                    updateUserDetails();
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 2:
					try {
						userRoute.viewAllRoutes();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 3:
                    userRoute.viewMyRoute(userId, conn);
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 4:
                    cancelPass(userId);
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 7:
                    printMyPass();
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 8:
                    flag = false;
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.print("Select valid activity to perform");
                    printOptions();
            }
        }
    }
}
