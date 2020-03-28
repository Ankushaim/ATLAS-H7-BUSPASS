import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class RouteMaster {
    
    ArrayList<String> routes = null;
    SQLSelect sqlRun = new SQLSelect();

    ArrayList<String> viewAllRoutes() throws SQLException {

        String sql = "select distinct route from route_info";
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        routes = new ArrayList<>();

        while (rs.next()) {
            routes.add(rs.getString("route"));
        }
        rs.close();

        System.out.println("\n" + "** Active Routes **");
        assert routes != null;

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

    boolean selectStop(String login) throws SQLException {
        Connection con = JdbcConnect.connect();
        Scanner input = new Scanner(System.in);
        ArrayList<String> stops = new ArrayList<>();
        ArrayList<String> directions = new ArrayList<>();

        System.out.println("\n" + "Enter the direction where you are looking for stop:  EAST , WEST , NORTH , SOUTH");
        System.out.print("Input: ");

        if (con != null) {
            String sqlQuery = "select distinct direction from stop_info;";
            PreparedStatement pstSel = con.prepareStatement(sqlQuery);
            ResultSet rs1 = pstSel.executeQuery();

            while (rs1.next()) {
                directions.add(rs1.getString("direction"));
            }
            String direction;

            do {
                direction = input.next().toUpperCase();
                if (!directions.contains(direction))
                {
                    System.out.print("Invalid Input. Please select Valid Direction: ");
                }
            } while (!directions.contains(direction));

            String sqlQuery2 = "select stop, direction from stop_info where direction ='"+direction+"'";
            System.out.println("\nDirection - " + direction +"\nStop Name");
            try{
                PreparedStatement pstSel2 = con.prepareStatement(sqlQuery2);
                ResultSet rs2    = pstSel2.executeQuery();

                while(rs2.next()) {
                    stops.add(rs2.getString("stop"));
                    System.out.print("- -" + rs2.getString("stop"));
                }
            }catch (SQLException e) { System.out.println(e.getMessage());}

            System.out.print("\nEnter Stop Name: ");
            String stopname;

            do {
                stopname = input.next().toUpperCase();
                if (!stops.contains(stopname))
                {
                    System.out.println("Invalid Input. Please select Valid stop name: ");
                }
            } while (!stops.contains(stopname));

            String sql = "UPDATE user_info SET stop ='"+stopname+"', status='PENDING' WHERE login ='"+login+"' and '"+stopname+"' in (SELECT stop from stop_info where direction= '"+direction+"')";
            try (
                    PreparedStatement pstUpdt = con.prepareStatement(sql)) {
                pstUpdt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Stop Request sent for :" + login + "\nApproval : PENDING");
            JdbcConnect.closeCon(con);
            return true;
        }
        return false;
    }
}



