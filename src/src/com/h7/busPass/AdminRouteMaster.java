package com.h7.busPass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AdminRouteMaster extends RouteMaster {
    Scanner input = new Scanner(System.in);

    //functionality for admin to add a new route in the DB
    boolean addNewRoute() throws SQLException {
        ArrayList<String> stops = new ArrayList<>();
        ArrayList<String> routes = viewAllRoutes();

        String[] directions = {"EAST", "WEST", "NORTH", "SOUTH"}; 
        System.out.println("Below are the active Routes" + "\n");

        String direction;
        //Admin is asked the direction in which he wants to create a route
        do {
            System.out.println("\n" + "Select direction of new route:  EAST , WEST , NORTH , SOUTH" + "\n");
            direction = input.next().toUpperCase();
        } while (!Arrays.asList(directions).contains(direction)); //validation of input
        
        //select query to provide stops which are unused. This ensures every route is unique
        String sql = "select stop, direction from stop_info where stop not in(select distinct stops from route_info) and direction='" + direction + "'"; //
        ResultSet rs = sqlRun.SqlSelectStatement(sql);

        while (rs.next()) {      //store the data of stops in array list            
            stops.add(rs.getString("stop"));                    
        }rs.close();
        
        int flag;  String[] stoplist;
        do {
            for (String s : stops) { //code to print all stops in selected direction
                System.out.print("- -" + s + "\t");
            }
            flag = 0;
            stoplist = null;
            System.out.println("\nWrite ' , ' seperated Stop names from available Stops" + "\n");
            stoplist = input.next().toUpperCase().split(","); //takes ',' separated input and converts to uppercase as standard

            for (String s : stoplist) {
                if (!stops.contains(s)) //validation of all stop names entered
                    flag = 1;
            }
            if (flag == 1) System.out.println("one or more stop names are invalid\n");
        } while (flag == 1);

        
        int s = routes.size() + 1; 
        String route = "R" + s;
        
        //creating object of SQLInsert to insert the new route provided by Admin
        SQLInsert si = new SQLInsert();
        HashMap<String, String> colValuesInsert = new HashMap<>();
        boolean isUploaded = false;
                
        for (String obj1 : stoplist) {
            colValuesInsert.clear();
            String tableName = "route_info";
            colValuesInsert.put("route", route);
            colValuesInsert.put("stops", obj1);
            colValuesInsert.put("direction", direction);
            isUploaded = si.ExecuteInsert(tableName, colValuesInsert);
        } 
        System.out.println("Added route :" + route);
        return isUploaded;
    }

    //functionality for admin to remove an unused route
    boolean removeRoute() {
    	ArrayList<String> routes = null;
    	String route;
        try {
			routes= viewAllRoutes(); //method call shows admin all routes so that he can select which one to delete
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

        do {
            System.out.println("Provide the route you would like to delete");
            route = input.next().toUpperCase(); 
        } while (!routes.contains(route)); //validation that correct route name is provided
        
        //query to check if the selected route is already in use by a user
        String sql = "SELECT status FROM user_info where status = 'APPROVED' AND stop IN (select stops from route_info where route = '" + route + "') ";

        ResultSet rs = sqlRun.SqlSelectStatement(sql);
                try {
					if (rs.next()) {
					    System.out.print("One or more stops in the route are being used by Users\n");
					    return false;
					}rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
               
          //query to check if a bus is assigned to the route which Admin wants to delete
          sql = "SELECT bus_id FROM bus_table where route = '" + route + "' ";

          rs = sqlRun.SqlSelectStatement(sql);
          try {
              if (rs.next()) {
                  System.out.print("Route cannot be deleted as it is assigned to Bus ID: " + rs.getString("bus_id") + "\n");
                  return false;
              }
              rs.close();
          } catch (SQLException e1) {
              e1.printStackTrace();
          }
         
          //if above two checks pass then delete is executed
        System.out.println("Route is not in use. Delete will be executed");

        SQLDelete sd = new SQLDelete();
        HashMap<String, String> columnValue = new HashMap<>();
        columnValue.put("route", " '" + route + "'");

        if (sd.executeDelete("route_info", columnValue)) {
            System.out.println("Deleted :" + route);
            return true;
        } else {
            return false;
        }
    }
}
