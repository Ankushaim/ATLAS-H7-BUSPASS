import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserFactory {
    private String userId;
    Connection con = JdbcConnect.connect();
    public UserFactory(String userId) {
        this.userId = userId;
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

    static void pressAnyKeyToContinue(){
        System.out.println("Press Enter/Return key to continue...");
        try
        {
            System.in.read();
        } catch(Exception e) {System.out.println("Enter/Return Exception");}
    }

    void updateUserDetails() {
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
                        JdbcConnect.closeCon(con);
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
                    break;
                default:
                    printOptionsUserDetailsMethod();
            }
        }

        for(Map.Entry<String, String> m:userDetails.entrySet()) {
            String sql = "UPDATE user_info SET "+ m.getKey()+" = '"+m.getValue()+"' where login = '"+ userId+"'";
            if(con != null)
            {
                try {
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    pstmt.executeUpdate();
                } catch (SQLException e) {System.out.println(e.getMessage());}
                finally {JdbcConnect.closeCon(con);}
            }
        }
    }
}

