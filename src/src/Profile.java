import java.util.Scanner;

public abstract class Profile {

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    abstract void printOptions();


}
