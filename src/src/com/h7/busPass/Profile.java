package com.h7.busPass;
import java.sql.Connection;
import java.util.Scanner;

public abstract class Profile {

    static Connection conn = JdbcConnect.connect();

    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    abstract void printOptions();

    abstract void viewController();
}
