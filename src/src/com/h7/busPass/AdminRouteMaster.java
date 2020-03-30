package com.h7.busPass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AdminRouteMaster extends RouteMaster {
    Scanner input = new Scanner(System.in);

    boolean addNewRoute() throws SQLException {
        ArrayList<String> stops = new ArrayList<>();
        ArrayList<String> routes = viewAllRoutes();

        String[] directions = {"EAST", "WEST", "NORTH", "SOUTH"};
        System.out.println("Below are the active Routes" + "\n");

        String direction;

        do {
            System.out.println("\n" + "Select direction of new route:  EAST , WEST , NORTH , SOUTH" + "\n");
            direction = input.next().toUpperCase();
        } while (!Arrays.asList(directions).contains(direction));
        
        String sql = "select stop, direction from stop_info where stop not in(select distinct stops from route_info) and direction='" + direction + "'"; //
        ResultSet rs = sqlRun.SqlSelectStatement(sql);

        while (rs.next()) {                  
            stops.add(rs.getString("stop"));                    
        }rs.close();
        
        int flag;  String[] stoplist;
        do {
            for (String s : stops) {
                System.out.print("- -" + s + "\t");
            }
            flag = 0;
            stoplist = null;
            System.out.println("\nWrite ' , ' seperated Stop names from available Stops" + "\n");
            stoplist = input.next().toUpperCase().split(",");

            for (String s : stoplist) {
                if (!stops.contains(s))
                    flag = 1;
            }
            if (flag == 1) System.out.println("one or more stop names are invalid\n");
        } while (flag == 1);

        //sql statement to update user's selected route
        int s = routes.size() + 1; 
        String route = "R" + s;

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

    
    boolean removeRoute() {
    	ArrayList<String> routes = null;
    	String route;
        try {
			routes= viewAllRoutes();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

        do {
            System.out.println("Provide the route you would like to delete");
            route = input.next().toUpperCase();
        } while (!routes.contains(route));
        
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
