import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AdminRouteMaster extends RouteMaster {
    Connection conn;
    Scanner input = new Scanner(System.in);

    public AdminRouteMaster(Connection conn) {
        this.conn = conn;
    }

    boolean addNewRoute() throws SQLException {
        ArrayList<String> stops = new ArrayList<>();
        String[] directions = {"EAST","WEST","NORTH","SOUTH"};
        
        System.out.println("Below are the active Routes" + "\n");
        ArrayList<String> routes = viewAllRoutes();
        
        String direction;
        do {
        System.out.println("\n" + "Select direction of new route:  EAST , WEST , NORTH , SOUTH" + "\n");
        direction = input.next().toUpperCase();
        }while(! Arrays.asList(directions).contains(direction));
        
        String sql = "select stop, direction from stop_info where stop not in(select distinct stops from route_info) and direction='" + direction + "'"; //
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        while (rs.next()) {                  
            stops.add(rs.getString("stop"));                    
        }rs.close();
        
        int flag;  String[] stoplist;
        do{ 
        	for(String s : stops) { System.out.print("- -" + s + "\t"); }
        	
        flag= 0; stoplist = null;
        System.out.println("\nWrite ' , ' seperated Stop names from available Stops" + "\n");
        stoplist = input.next().toUpperCase().split(",");
        
        for(String s : stoplist) { 
        	if(!stops.contains(s))	flag= 1;        	
        }
        if(flag==1) System.out.println("one or more stop names are invalid\n");
        
        }while(flag==1);

        //sql statement to update user's selected route
        int s = routes.size() + 1; 
        String route = "R" + s;

        SQLInsert si = new SQLInsert();
        HashMap<String, String> colValuesInsert = new HashMap<>();
        boolean isUploaded = false;
                
        for (String obj1 : stoplist) {
//             sql = "INSERT INTO route_info (route,stops,direction) VALUES ('" + route + "','" + obj1 + "','" + direction + "')";
        	 colValuesInsert.clear();;
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
        try {
			viewAllRoutes();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("Provide the route you would like to delete");
        String route = input.next().toUpperCase();

        String sql = "SELECT status FROM user_info where status = 'ACTIVE' AND stop IN (select stops from route_info where route = '" + route + "') ";

        if (conn != null) {
            try (
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
            ) {
                if (rs.next()) {
                    System.out.print("Route cannot be deleted as it is " + rs.getString("status") + "\n");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Route is not in use. Delete will be executed");
            }
        }

        sql = "DELETE FROM route_info WHERE route=  '" + route + "'";

        if (conn != null) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Deleted :" + route);
        return true;
    }
}
