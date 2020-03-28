import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestRouteMaster extends RouteMaster {

    public void seatsOccupiedInRoute(Connection conn) throws SQLException {
        ArrayList<String> routes = null;
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

            assert routes != null;

            for (String s : routes) {
                String denominatorSQL = "select sum(category_id) as denom from bus_table where route = '" + s + "'";
                SQLSelect sqlRun = new SQLSelect();
                ResultSet rs = sqlRun.SqlSelectStatement(denominatorSQL);
                double denominator = 0.0;

                while (rs.next())
                    denominator = rs.getDouble("denom");

                String NumeratorSQL = "select count(distinct login) as numer from pass_details a join bus_table b on a.bus_id = b.bus_id where a.route = '" + s + "'";
                SQLSelect sqlRun2 = new SQLSelect();
                ResultSet rs2 = sqlRun2.SqlSelectStatement(NumeratorSQL);
                double numerator = 0.0;

                while (rs2.next())
                    numerator = rs2.getDouble("numer");

                Double percentage = ((numerator / denominator) * 100);

                if (percentage.isNaN())
                    System.out.println(s + ": --> " + "No bus running on this route ");
                else
                    System.out.println(s + ": --> " + String.format("%.2f", percentage) + " seats occupied");
            }
        }
    }
}
