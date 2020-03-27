import java.util.Scanner;

public class BasicUtility {

    static Scanner inp = new Scanner(System.in);

    //    static void pressAnyKeyToContinue() {
//        System.out.print("Press Enter/Return key to continue...");
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();
//    }
    static String regNumberCheck() {
        String regNumber = "";
        while (regNumber.length() != 4) {
            System.out.print("Please provide valid 4 digit Reg Number: ");
            regNumber = inp.nextLine();
        }
        return regNumber;
    }
}
