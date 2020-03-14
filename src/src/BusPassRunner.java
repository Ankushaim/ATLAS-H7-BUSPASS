import java.util.Scanner;

public class BusPassRunner {
    public static void main(String[] args) {
        BusPassRunner runObj = new BusPassRunner();
        System.out.println("Welcome to Amazon Transport Service Portal");
        System.out.println("To continue please select the below:");
        System.out.println("1. Admin");
        System.out.println("2. Registered User");
        System.out.println("3. Visitor");
        System.out.println("4. To Logout");
        System.out.print("Input:");
        while(true) {
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            String userId;
            String password;
            Authentication authCheck = new Authentication();

            switch (choice) {
                case 1:
                    System.out.println("Sign in to continue to ATS Portal");
                    userId = input.nextLine();
                    password = input.nextLine();
                    if (authCheck.checkCredentials(userId, password, "admin")) {
                        System.out.println("Welcome Admin");
                    }
                    else{
                        System.out.println("Invalid Credentials");
                        System.exit(0);
                    }
                    break;
                case 2:
                    System.out.println("Sign in to continue to ATS Portal");
                    userId = input.nextLine();
                    password = input.nextLine();
                    if (authCheck.checkCredentials(userId, password, "user")) {
                        System.out.println("Welcome User_Name");
                    }
                    else{
                        System.out.println("Invalid Credentials");
                        System.exit(0);
                    }
                    break;
                case 3:
                    System.out.println("Welcome Guest");
                    break;
                case 4:
                    System.out.println("Thanks for visiting");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please provide valid input:");
                    System.out.println("1. Admin");
                    System.out.println("2. Registered User");
                    System.out.println("3. Visitor");
                    System.out.println("4. To Logout");
                    System.out.print("Input:");
            }

        }
    }
}
