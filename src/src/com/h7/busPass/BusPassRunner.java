/**
 * This is the Main class which will run the application and will be responsible to call other methods
 * in project..
 *
 * @author (Ankush)
 * @version (Java 8)
 */

package com.h7.busPass;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BusPassRunner {
    /**
     * This method will wait or accept only enter/return key input. This method is used to hold the
     * screen to wait for user's input..
     */
    static void pressAnyKeyToContinue() {
        System.out.print("Press Enter/Return key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Main method, used to run the application..
     */
    public static void main(String[] args) throws SQLException {
        BusPassRunner runObj = new BusPassRunner();
        runObj.printOptionsMain();
        // Creating reference variable of profile class
        Profile pro;

        /*Creating two different objects for scanner to take two different input types from user as single
        object is causing problem and can not be used again*/
        Scanner credential_input = new Scanner(System.in);
        Scanner input;
        Authentication authCheck = new Authentication();
        String userId, password;
        boolean flag = true, error;

        while (flag) {
            int choice = 0;

            // this loop will check for invalid input. And will prompt the again to provide valid input only..
            do {
                try {
                    System.out.print("Input: ");
                    input = new Scanner(System.in);
                    choice = input.nextInt();
                    error = false;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input :-( only Integers allowed");
                    error = true;
                }
            } while (error);

            // switch case will allow user to select specific option as..
            switch (choice) {
                case 1:
                    System.out.println("Sign in to continue to ATS Portal");
                    System.out.print("UserId: ");
                    userId = credential_input.nextLine();
                    System.out.print("Password: ");
                    password = credential_input.nextLine();
                    System.out.println("Fetching details for: " + userId + "\n");

                    // condition to check if the provided credentials are valid or not
                    if (authCheck.checkCredentials(userId, password, "admin")) {
                        // Creating and assigning Admin class object to profile reference..
                        pro = new Admin(userId);
                        pro.viewController();
                    } else {
                        System.out.println("Invalid Credentials");
                    }
                    pressAnyKeyToContinue();
                    runObj.printOptionsMain();
                    break;
                case 2:
                    System.out.println("Sign in to continue to ATS Portal");
                    System.out.print("UserId: ");
                    userId = credential_input.nextLine();
                    System.out.print("Password: ");
                    password = credential_input.nextLine();
                    System.out.println("Fetching details for: " + userId + "\n");

                    // condition to check if the provided credentials are valid or not
                    if (authCheck.checkCredentials(userId, password, "user")) {
                        // Creating and assigning User class object to profile reference..
                        pro = new User(userId);
                        pro.viewController();
                    } else {
                        System.out.println("Invalid Credentials");
                    }
                    pressAnyKeyToContinue();
                    runObj.printOptionsMain();
                    break;
                case 3:
                    System.out.println("Fetching details.." + "\n");
                    // Creating and assigning Guest class object to profile reference../
                    pro = new Guest();
                    pro.viewController();
                    pressAnyKeyToContinue();
                    runObj.printOptionsMain();
                    break;
                case 4:
                    /*this case will terminate the program and responsible to close all the JDBC
                     * connection
                     */
                    System.out.println("Thanks for visiting");
                    credential_input.close();
                    Profile.conn.close();
                    SQLMain.conn.close();
                    flag = false;
                    break;
                default:
                    System.out.println("\n" + "Please provide valid input:");
                    System.out.println("1. Admin");
                    System.out.println("2. Registered");
                    System.out.println("3. Visitor");
                    System.out.println("4. To Logout");
            }
        }
    }

    /*
     This method prints the main menu options to the screen..
     */
    void printOptionsMain() {
        System.out.println("\n" + "Welcome to Amazon Transport Service Portal");
        System.out.println("To continue please select the below:");
        System.out.println("1. Admin");
        System.out.println("2. Registered User");
        System.out.println("3. Visitor");
        System.out.println("4. To Logout");
    }
}