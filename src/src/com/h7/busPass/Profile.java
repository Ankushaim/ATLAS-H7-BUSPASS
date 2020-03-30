/*
  This is the Parent class of Admin, Guest and User classes which contain commonly used method
  requirements..
  @author (Ankush)
 * @version (Java 8)
 */

package com.h7.busPass;

import java.sql.Connection;
import java.util.Scanner;

public abstract class Profile {

    static Connection conn = JdbcConnect.connect();

    /**
     * This method will wait or accept only enter/return key input. This method is used to hold the
     * screen to wait for user's input..
     */
    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    // most commonly used method. which will be implemented by child classes with slight variation
    abstract void printOptions();

    // most commonly used method. which will be implemented by child classes with slight variation
    abstract void viewController();
}
