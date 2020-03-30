package com.h7.busPass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class RouteMaster {
    /*abstract class which caters to Route services.
     * This class is extended by AdminRouteMaster, UserRouteMaster, GuestRouteMAster     *
     */

    ArrayList<String> routes = null;
    SQLSelect sqlRun = new SQLSelect(); //SQLselect class object declared in class to maintain single connection with the DB

    //method to display all routes from the DB which also returns an ArrayList with all routes. Shared by admin, guest
    ArrayList<String> viewAllRoutes() throws SQLException {

        String sql = "select distinct route from route_info"; //as data in route_info is de-normalised, we are selecting distinct route
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        routes = new ArrayList<>();

        while (rs.next()) {
            routes.add(rs.getString("route"));
        }
        rs.close();

        System.out.println("\n" + "** Active Routes **");
        assert routes != null;

        //running a for each loop on the JDBC resultset to display and save route information
        for (String obj : routes) {
            sql = "select distinct stops from route_info where route='" + obj + "' ";
            rs = sqlRun.SqlSelectStatement(sql);
            System.out.print("Route  " + obj + "--> Amazon Campus");

            while (rs.next()) {
                System.out.print("--" + rs.getString("stops"));
            }
            System.out.print("--End" + "\n");
            rs.close();
        }
        return routes;
    }

    /*shared functionality of guest and user to select their home stop
     * Approach:
     * 1. stops are divided in 4 directions east, west, north, south. Center being the employee's office.
     * 2. Users/guests will not be given the feature to suggest new routes, instead they can request for a stop and Admin takes an informed decision on route modification.
     * --
     * selectstop method is an interactive method which checks which direction the user wants a stop and gives right options to select from
     */
    boolean selectStop(String login) throws SQLException {
        Scanner input = new Scanner(System.in);
        ArrayList<String> stops = new ArrayList<>();
        ArrayList<String> directions = new ArrayList<>();

        System.out.println("\n" + "Enter the direction where you are looking for stop:  EAST , WEST , NORTH , SOUTH");
        System.out.print("Input: ");

        String sqlQuery = "select distinct direction from stop_info;";
        ResultSet rs1 = sqlRun.SqlSelectStatement(sqlQuery);

        while (rs1.next()) {
            directions.add(rs1.getString("direction"));
        }
        rs1.close();
        String direction;

        do {
            direction = input.next().toUpperCase(); //takes user input and validates
            if (!directions.contains(direction)) {
                System.out.print("Invalid Input. Please select Valid Direction: ");
            }
        } while (!directions.contains(direction));

        //query to get stops from direction which user has selected
        String sqlQuery2 = "select stop, direction from stop_info where direction ='" + direction + "'";
        System.out.println("\nDirection - " + direction + "\nStop Name");

        ResultSet rs2 = sqlRun.SqlSelectStatement(sqlQuery2);

        while (rs2.next()) {        //prints entire resulset of the stops
            stops.add(rs2.getString("stop"));
            System.out.print("- -" + rs2.getString("stop"));
        }
        rs2.close();

        System.out.print("\nEnter Stop Name: ");
        String stopname;

        do {
            stopname = input.next().toUpperCase(); //takes user input and validates
            if (!stops.contains(stopname)) {
                System.out.println("Invalid Input. Please select Valid stop name: ");
            }
        } while (!stops.contains(stopname));
        input.close();

        /*updates user's stop name in user_info table and changes his status to Pending for Admin to review the new request
         *update query being implemented--> "UPDATE user_info SET stop ='"+stopname+"', status='PENDING' WHERE login ='"+login+"' ";
         */
        SQLUpdate su = new SQLUpdate();
        HashMap<String, String> colValues = new HashMap<>();
        HashMap<String, String> where = new HashMap<>();

        boolean isUploaded = false;

        colValues.clear();
        String tableName = "user_info";
        colValues.put("stop", "'" + stopname + "'");
        colValues.put("status", "'" + "PENDING" + "'");
        where.put("login", "'" + login + "'");

        isUploaded = su.ExecuteUpdate(tableName, colValues, where);
        if (isUploaded) System.out.println("Stop Request sent for :" + login + "\nApproval : PENDING");
        else System.out.println("Request Failed");

        return isUploaded;
    }
}



