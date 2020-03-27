import java.sql.*;
import java.util.Scanner;

public class AdminRouteMaster extends RouteMaster {
    Connection conn;
    Scanner input = new Scanner(System.in);

    public AdminRouteMaster(Connection conn) {
        this.conn = conn;
    }

    void addNewRoute() {
        System.out.println("Below are the active Routes" + "\n");
        viewAllRoutes(conn);
        System.out.println("\n" + "Select direction of new route:  EAST , WEST , NORTH , SOUTH" + "\n");
        String direction = input.next().toUpperCase();

        if (conn != null) // provide available stops
        {
            String sql = "select stop, direction from stop_info where stop not in(select distinct stops from route_info) and direction='" + direction + "'"; //
            try (
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)
            ) {
                System.out.println("Available stops at :" + direction);
                while (rs.next()) {
                    System.out.print("Stop Name -" + rs.getString("stop") + "\n");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Write ' , ' seperated Stop names from available Stops" + "\n");

        String[] stoplist;

        stoplist = input.next().toUpperCase().split(",");

        //sql statement to update user's selected route
        int s = routes.size() + 1; // error on this line...
        String route = "R" + s;

        for (String obj1 : stoplist) {
            String sql = "INSERT INTO route_info (route,stops,direction)"
                    + "VALUES ('" + route + "','" + obj1 + "','" + direction + "')";

            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Added route :" + route);
    }

    boolean removeRoute() {
        viewAllRoutes(conn);
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
