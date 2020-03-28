import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRouteMaster extends RouteMaster {

    void viewMyRoute(String userId, Connection conn) {
        String sql = "select distinct route from pass_details where login = '" + userId + "' ";
        String route = null;

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                route = rs.getString("route");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sql = "select distinct stops from route_info where route='" + route + "' ";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.print("Route  " + userId + "--> Amazon Campus");
            while (rs.next()) {
                System.out.print("--" + rs.getString("stops"));
            }
            System.out.print("--End" + "\n");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
