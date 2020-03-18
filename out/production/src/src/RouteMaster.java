import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RouteMaster {
    ArrayList<String> routes;
    JdbcConnect jbc = new JdbcConnect();
    String[] stops;

    void viewAllRoutes() {
        if(jbc.connect() != null) // check
        {
            String sql = "select distinct route from route_info"; //
            try(
                    Statement stmt  = jbc.connect().createStatement();
                    ResultSet rs    = stmt.executeQuery(sql)
            ){
                routes = new ArrayList<String>();
                while(rs.next()) {
                    routes.add(rs.getString("route"));
                }
            }catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }

            System.out.print("Active Routes"+"\n");
            for(String obj: routes) {
                sql = "select distinct stops from route_info where route='"+obj+"' "; //
                try(
                        Statement stmt  = jbc.connect().createStatement();
                        ResultSet rs    = stmt.executeQuery(sql)
                )
                {
                    System.out.print("Route  "+obj+"-Start");
                    while(rs.next()) {
                        System.out.print("--"+rs.getString("stops"));
                    }
                    System.out.print("  End"+"\n");
                }catch (SQLException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    void viewAllStops() {
        if(jbc.connect() != null) // check
        {
            String sql = "select stop_name , area from stop_info order by area"; //
            try(
                    Statement stmt  = jbc.connect().createStatement();
                    ResultSet rs    = stmt.executeQuery(sql)
            ){

                while(rs.next()) {
                    System.out.print("Stop Name -"+rs.getString("stop_name"));
                    System.out.print(":: Area -"+rs.getString("area")+"\n");
                }
            }catch (SQLException e) { System.out.println(e.getMessage());}
        }
    }

    boolean selectstop(String login) {
        Scanner s = new Scanner(System.in);
        String[] stops;
        System.out.println("\n"+"Enter the direction where you are looking for stop:  EAST , WEST , NORTH , SOUTH"+"\n");
        String direction = s.next().toUpperCase(); //converting the input to uppercase (case insesitive)

        if(jbc.connect() != null) // check
        {
            String sql = "select stop , direction from stop_info where direction ='"+direction+"' order by direction"; //
            try(
                    Statement stmt  = jbc.connect().createStatement();
                    ResultSet rs    = stmt.executeQuery(sql)
            ){
                if(rs.isBeforeFirst()==false) {
                    System.out.println("\n"+"Invalid Stop Name");
                    return false;
                }
                while(rs.next()) {
                    System.out.print("Stop Name -"+rs.getString("stop"));
                    System.out.print(":: Direction -"+rs.getString("direction")+"\n");
                }
            }catch (SQLException e) { System.out.println(e.getMessage());}}

        System.out.println("\n"+"Enter Stop Name: ");

        String stopname = s.next().toUpperCase();
        String sql = "UPDATE user_info SET stop ='"+stopname+"', status='PENDING' WHERE login ='"+login+"' and '"+stopname+"' in (SELECT stop from stop_info where direction= '"+direction+"')";

        if(jbc.connect() != null)
        {
            try (Connection conn = jbc.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                //int execute = pstmt.executeUpdate();
                if(pstmt.executeUpdate() == 0) {
                    System.out.println("\nIncorrect stop name entered");
                    return false;  }
            } catch (SQLException e) { System.out.println(e.getMessage());}	}
        System.out.println("Stop Request sent for :"+ login+"\nApproval : PENDING");
        return true;
    }

}



