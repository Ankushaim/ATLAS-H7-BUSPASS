import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class RouteMaster {
    Connection conn = JdbcConnect.connect();
    ArrayList<String> routes = null;

    void viewAllRoutes() {

        if (conn != null) {
            String sql = "select distinct route from route_info";
            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                routes = new ArrayList<>();
                while (rs.next()) {
                    routes.add(rs.getString("route"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("\n" + "** Active Routes **");
            assert routes != null; // This is compiler suggestion need to check working
            for(String obj: routes) {
                sql = "select distinct stops from route_info where route='"+obj+"' ";
                try {
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery();
                    System.out.print("Route  " + obj + "--> Amazon Campus");
                    while(rs.next()) {
                        System.out.print("--"+rs.getString("stops"));
                    }
                    System.out.print("--End" + "\n");
                }catch (SQLException e) {System.out.println(e.getMessage());}
            }
        }
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
            try{
                PreparedStatement pstSel2 = con.prepareStatement(sqlQuery2);
                ResultSet rs2    = pstSel2.executeQuery();
                System.out.println("\n");
                while(rs2.next()) {
                    stops.add(rs2.getString("stop"));
                    System.out.print("Stop Name -" + rs2.getString("stop"));
                    System.out.println(":: Direction -" + rs2.getString("direction"));
                }
            }catch (SQLException e) { System.out.println(e.getMessage());}

            System.out.print("Enter Stop Name: ");
            String stopname;
            do {
                stopname = input.next().toUpperCase();
                if (!stops.contains(stopname))
                {
                    System.out.print("Invalid Input. Please select Valid stop name: ");
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

//    void viewAllStops() {
//        Connection con = JdbcConnect.connect();
//        if(con != null)
//        {
//            String sql = "select stop_name , area from stop_info order by area";
//            try {
//                PreparedStatement pstSel  = con.prepareStatement(sql);
//                ResultSet rs = pstSel.executeQuery();
//                while(rs.next()) {
//                    System.out.print("Stop Name -"+rs.getString("stop_name"));
//                    System.out.print(":: Area -"+rs.getString("area")+"\n");
//                }
//            }catch (SQLException e) { System.out.println(e.getMessage());}
//        }
//    }
}



