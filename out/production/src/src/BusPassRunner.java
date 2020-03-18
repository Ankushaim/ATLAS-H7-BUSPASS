import java.util.InputMismatchException;
import java.util.Scanner;

public class BusPassRunner {

    void printOptionsMain() {
        System.out.println("\n" + "Welcome to Amazon Transport Service Portal");
        System.out.println("To continue please select the below:");
        System.out.println("1. Admin");
        System.out.println("2. Registered User");
        System.out.println("3. Visitor");
        System.out.println("4. To Logout");
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


    public static void main(String[] args) {
        BusPassRunner runObj = new BusPassRunner();
        Authentication authCheck = new Authentication();
        Scanner credential_input = new Scanner(System.in);
        Scanner input = null;
        runObj.printOptionsMain();

        String userId;
        String password;
        boolean flag = true;
        boolean error;
        while(flag) {
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
                case 1:
                    System.out.println("Sign in to continue to ATS Portal");
                    System.out.print("UserId: ");
                    userId = credential_input.nextLine();
                    System.out.print("Password: ");
                    password = credential_input.nextLine();
                    if (authCheck.checkCredentials(userId, password, "admin")) {
                        new Admin(userId);
                    }
                    else{
                        System.out.println("Invalid Credentials");
                        System.exit(0);
                    }
                    runObj.printOptionsMain();
                    pressAnyKeyToContinue();
                    break;
                case 2:
                    System.out.println("Sign in to continue to ATS Portal");
                    System.out.print("UserId: ");
                    userId = credential_input.nextLine();
                    System.out.print("Password: ");
                    password = credential_input.nextLine();
                    if (authCheck.checkCredentials(userId, password, "user")) {
                        new User(userId);
                    }
                    else{
                        System.out.println("Invalid Credentials");
                        System.exit(0);
                    }
                    runObj.printOptionsMain();
                    pressAnyKeyToContinue();
                    break;
                case 3:
                    new Guest();
                    runObj.printOptionsMain();
                    pressAnyKeyToContinue();
                    break;
                case 4:
                    System.out.println("Thanks for visiting");
                    input.close();
                    credential_input.close();
                    flag = false;
                    break;
                default:
                    System.out.println("\n"+ "Please provide valid input:");
                    System.out.println("1. Admin");
                    System.out.println("2. Registered UserFactory");
                    System.out.println("3. Visitor");
                    System.out.println("4. To Logout");
            }
        }
    }
}