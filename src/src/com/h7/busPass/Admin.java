/*
  This is the Admin class which will be responsible to call other admin specific methods and
  print admin menu..
  @author (Ankush)
 * @version (Java 8)
 */

package com.h7.busPass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin extends Profile {
    String userId;
    String adminName;

    // constructor of Admin class
    public Admin(String userId) {
        if (conn != null) {
            String sql = "select user_name from user_info where login = ?";
            try {
                PreparedStatement pstSel = conn.prepareStatement(sql);
                pstSel.setString(1, userId);
                ResultSet rs = pstSel.executeQuery();
                while (rs.next())
                    this.adminName = rs.getString("user_name");// initializing class variable
                rs.close();
                pstSel.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        this.userId = userId;// initializing class variable
    }

    @Override
    void printOptions() {
        System.out.println();
        System.out.print("1. View Requests: " + "\t");
        System.out.print("2. Add New Route: " + "\t");
        System.out.println("3. Remove existing Route: " + "\t");
        System.out.print("4. Change Bus type on Routes: " + "\t");
        System.out.print("5. Assign Available Bus on Route: " + "\t");
        System.out.println("6. View Vehicle List of different Type: " + "\t");
        System.out.println("7. Register a Bus to Company: " + "\t");
        System.out.println("8. To Logout: " + "\t");
    }

    /*
     * This method will take route input from user and validate it..
     */
    String UserRouteValue() {
        ArrayList<String> routes = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        SQLSelect sel = new SQLSelect();
        String sql = "select distinct route from route_info where route is not null";

        try {
            ResultSet rs2 = sel.SqlSelectStatement(sql);
            while (rs2.next())
                routes.add(rs2.getString("route"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Available routes are:");

        for (String s : routes) {
            System.out.print(s + "\t");
        }

        String route;
        do {
            System.out.print("\n" + "Input: ");
            route = input.nextLine().toUpperCase();
        } while (!routes.contains(route));

        return route;
    }

    @Override
    void viewController() {
        System.out.println("\n" + "Welcome " + adminName);
        AdminRouteMaster callingAdminRoute = new AdminRouteMaster();
        ViewRequests viewRequest = new ViewRequests();
        BusMaster busMaster = new BusMaster();

        /*
         * This method prints the available notification(pass requests) to admin menu
         */
        viewRequest.Notifications();
        printOptions();

        Scanner input;
        String route;
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

            switch (choice) {
                case 1:
                    try {
                        viewRequest.PendingBusPassRequests();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();// This method will hold the output screen for user..
                    printOptions();
                    break;
                case 2:
                    try {
                        callingAdminRoute.addNewRoute();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 3:
                    if (callingAdminRoute.removeRoute())
                        System.out.println("Route Removed Successfully");
                    else
                        System.out.println("Route can not be removed please check if any bus/user active on route");
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 4:
                    route = UserRouteValue();
                    try {
                        busMaster.ChangeBusTypeOfRoute(route);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 5:
                    route = UserRouteValue();
                    try {
                        busMaster.AddBusInRoute(route);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 6:
                    try {
                        String sel = "select distinct category_id, count(distinct bus_id) as num from bus_table where route is not null group by 1";
                        busMaster.vehicleDifferentTypes(sel);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 7:
                    busMaster.registerBus();
                    pressAnyKeyToContinue();
                    printOptions();
                    break;
                case 8:
                    flag = false;
                    break;
                default:
                    System.out.print("Select valid activity to perform");
                    printOptions();
            }
        }
    }
}
