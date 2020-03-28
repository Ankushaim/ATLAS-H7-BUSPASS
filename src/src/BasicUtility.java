import java.util.Scanner;

public class BasicUtility {

    static Scanner inp = new Scanner(System.in);

    static String regNumberCheck() {
        String regNumber = "";

        while (regNumber.length() != 4) {
            System.out.print("Please provide valid 4 digit Reg Number: ");
            regNumber = inp.nextLine();
        }
        return regNumber;
    }
}
