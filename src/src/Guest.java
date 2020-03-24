import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Guest {
    public Guest() {
        view_controller_guest();
    }

    static void printOptionsGuest() {
        System.out.print("1. View All Routes: " + "\t");
        System.out.println("2. Percentage of seats occupied in each route: " + "\t");
        System.out.print("3. SignUp and Apply for Bus Pass: " + "\t");
        System.out.println("4. To previous Menu: " + "\t");
        System.out.println("5. To Logout: " + "\t");
    }

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    void view_controller_guest() {
        System.out.println("Welcome");
        GuestFactory calling_guest = new GuestFactory();
        printOptionsGuest();
        boolean flag = true;
        boolean error;
        while (flag) {
            int choice = 0;
            do {
                try {
                    System.out.print("Input: ");
                    Scanner input = new Scanner(System.in);
                    choice = input.nextInt();
                    error = false;
                }catch (InputMismatchException e)  {
                    System.out.println("Invalid Input :-( only Integers allowed");
                    error = true;
                }
            }while(error);

            switch (choice) {
                case 1:
                    calling_guest.getRouteFromRouteMaster();
                    pressAnyKeyToContinue();
                    printOptionsGuest();
                    break;
                case 2:
                    calling_guest.seatOccupancyCheck();
                    pressAnyKeyToContinue();
                    printOptionsGuest();
                    break;
                case 3:
                    try {
                        calling_guest.register();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptionsGuest();
                    break;
                case 4:
                    flag = false;
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Select valid activity to perform");
                    printOptionsGuest();
            }
        }
    }
}
