import java.sql.SQLException;
import java.util.Scanner;

public class Guest {
    public Guest() {
        view_controller_guest();
    }

    static void printOptionsGuest() {
        System.out.println();
        System.out.print("1. View All Routes: " + "\t");
        System.out.println("2. Percentage of seats occupied in each route: "+ "\t");
        System.out.print("3. Raise a Request to add new Route: "+ "\t");
        System.out.println("4. SignUp Apply for Bus Pass: " + "\t");
        System.out.println("5. To previous Menu: " + "\t");
        System.out.println("6. To Logout: " + "\t");
    }

    static void pressAnyKeyToContinue() {
        System.out.println("Press Enter/Return key to continue...");
        try
        {
            System.in.read();
        }
        catch(Exception e)
        {}
    }

    void view_controller_guest() {
        System.out.println("Welcome");
        GuestFactory calling_route = new GuestFactory();
        printOptionsGuest();
        boolean flag = true;

        while (flag) {
            System.out.print("Input: ");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    calling_route.getRouteFromRouteMaster();
                    pressAnyKeyToContinue();
                    printOptionsGuest();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    try {
                        calling_route.register();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptionsGuest();
                    break;
                case 5:
                    flag = false;
                    break;
                case 6:
                    System.exit(0);
                default:
                    System.out.println();
                    System.out.println("Select valid activity to perform");
                    System.out.print("1. View All Routes: " + "\t");
                    System.out.println("2. Percentage of seats occupied in each route: "+ "\t");
                    System.out.print("3. Raise a Request to add new Route: "+ "\t");
                    System.out.println("4. SignUp Apply for Bus Pass: " + "\t");
                    System.out.println("5. To previous Menu: " + "\t");
                    System.out.println("6. To Logout: " + "\t");
            }
        }
    }
}
