/*
  This is the Guest class which will be responsible to call other Guest specific methods and
  print Guest menu..
  @author (Ankush)
 * @version (Java 8)
 */

package com.h7.busPass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Guest extends Profile {

    @Override
    void printOptions() {
        System.out.print("\n" + "1. View All Routes: " + "\t");
        System.out.println("2. Percentage of seats occupied in each route: " + "\t");
        System.out.println("3. SignUp and Apply for Bus Pass: " + "\t");
        System.out.println("4. To Logout: " + "\t");
    }

    /*
     * This method will allow guest/visitor to signUp and apply for bus pass
     */
    void register() throws SQLException {
        /*AL is used to store all the signup specific details because of AL's growable nature, if
        any future changes to the signup parameters*/
        ArrayList<String> regDetails = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        String sqlQuery = "INSERT INTO user_info(user_name, login, password, phone_num, address, city, status, type, create_date, change_date) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstIns = conn.prepareStatement(sqlQuery);
        System.out.println("Create your ATS Account to continue to ATS");

        boolean flag = true;

        while (flag) {
            String name;
            do {
                System.out.print("Name: ");
                name = input.nextLine().toUpperCase();
            }
            while (name.length() == 0);

            String login;
            do {
                System.out.print("Login: ");
                login = input.nextLine();
            }
            while (login.length() == 0);

            String password;
            do {
                System.out.print("Password: ");
                password = input.nextLine();
            }
            while (password.length() == 0);

            if (new Authentication().checkCredentials(login, password, "user"))  //To check is login already registered..
            {
                System.out.println("User Already Exists");
                break;
            } else {
                regDetails.add(name);
                regDetails.add(login);
                regDetails.add(password);
            }

            String phNumber;
            do {
                System.out.print("Phone Number: ");
                phNumber = input.nextLine();
            }
            while (phNumber.length() == 0);
            regDetails.add(phNumber);

            String address;
            do {
                System.out.print("Address: ");
                address = input.nextLine();
            }
            while (address.length() == 0);
            regDetails.add(address);

            String city;
            do {
                System.out.print("City: ");
                city = input.nextLine().toUpperCase();
            }
            while (city.length() == 0);
            regDetails.add(city);

            regDetails.add("PENDING"); // to set application in pending..
            regDetails.add("user"); // to set profile as user only

            //To take system date and change it to required type for database
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.now();
            regDetails.add(dtf.format(localDate));
            regDetails.add(dtf.format(localDate));

            //Loops to extract details from AL and set it to prepared statement
            int i = 1;
            for (String c : regDetails
            ) {
                pstIns.setString(i, c);
                i++;
            }
            pstIns.executeUpdate();
            System.out.println("Registration Successful");

            // Allow use to select specific avaiable stop from database
            UserRouteMaster userRoute = new UserRouteMaster();
            if (userRoute.selectStop(login)) {
                System.out.println("Please wait for Admin's approval on your ATS pass request :-)");
            } else {
                System.out.println("Registration Successful. However bus pass application failed");
                System.out.println("To continue application go to login-> Edit or Change Details-> Change Stop");
            }
            flag = false;
        }
    }

    @Override
    void viewController() {
        System.out.println("\n" + "Welcome Guest");
        printOptions();
        GuestRouteMaster guestRoute = new GuestRouteMaster();
        boolean flag = true, error;

        while (flag) {
            int choice = 0;
            // this loop will check for invalid input. And will prompt the again to provide valid input only..
            do {
                try {
                    System.out.print("Input: ");
                    Scanner input = new Scanner(System.in);
                    choice = input.nextInt();
                    error = false;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid Input :-( only Integers allowed");
                    error = true;
                }
            } while (error);

            switch (choice) {
                case 1:
                    try {
                        guestRoute.viewAllRoutes();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 2:
                    try {
                        guestRoute.seatsOccupiedInRoute(conn);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 3:
                    try {
                        register();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 4:
                    flag = false;
                    break;
                default:
                    System.out.println("Select valid activity to perform");
                    printOptions();
            }
        }
    }
}
