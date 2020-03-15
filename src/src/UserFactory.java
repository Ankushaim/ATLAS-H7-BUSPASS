import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UserFactory {
    String userName;
    String userId;
    String Name = null;

    UserFactory(String userId) {
        JdbcConnect jbc = new JdbcConnect();
        if (jbc.connect() != null) {
            String sql = "select user_name from user_info where login = '" + userId + "'";
            try (Statement stmt = jbc.connect().createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                Name = rs.getString("user_name");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        this.userName = Name;
        this.userId = userId;
        view_controller();
    }

    static void printOptions() {
        System.out.println();
        System.out.print("1. Edit or Change details: " + "\t");
        System.out.print("2. View all routes: "+ "\t");
        System.out.println("3. Show my route : "+ "\t");
        System.out.print("4. Request to cancel the Bus Pass: " + "\t");
        System.out.print("5. Request to suspend the pass: " + "\t");
        System.out.println("6. Request for new route: " + "\t");
        System.out.print("7. Print your pass: " + "\t");
        System.out.println("8. go to previous Menu: " + "\t");
    }

    static void pressAnyKeyToContinue()
    {
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }

    void view_controller() {
        System.out.println("Welcome " + Name);

        User calling_user = new User(userId);

        System.out.println("Select appropriate activity to perform");
        /*System.out.println("1. Edit or Change details: ");
        System.out.println("2. View all routes: ");
        System.out.println("3. Show my route : ");
        System.out.println("4. Request to cancel the Bus Pass: ");
        System.out.println("5. Request to suspend the pass: ");
        System.out.println("5. Request for new route: ");
        System.out.println("6. Print your pass: ");
        System.out.println("7. go to previous Menu: ");*/
        printOptions();
        boolean c = true;

        while (c) {
            System.out.print("Input: ");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    calling_user.updateUserDetails();
                    printOptions();
                    break;
                case 2:
                    calling_user.viewRoute();
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 3:
                    calling_user.viewStops();// To be decided..
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    c = false;
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
