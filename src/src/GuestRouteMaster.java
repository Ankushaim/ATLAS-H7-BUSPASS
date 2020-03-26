import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestRouteMaster extends RouteMaster {

    public void seatsOccupiedInRoute() throws SQLException {
        Connection con = JdbcConnect.connect();
        ArrayList<String> routes = null;
        if (con != null) // check
        {
            String sql = "select distinct route from route_info";
            try {
                PreparedStatement stmt = con.prepareStatement(sql);
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
                SQLSelect sqlRun = new SQLSelect(conn);
                ResultSet rs = sqlRun.SqlSelectStatement(denominatorSQL);
                Double denominator = 0.0;
                while (rs.next())
                    denominator = rs.getDouble("denom");

                String NumeratorSQL = "select count(distinct login) as numer from pass_details a join bus_table b on a.bus_id = b.bus_id where a.route = '" + s + "'";
                SQLSelect sqlRun2 = new SQLSelect(conn);
                ResultSet rs2 = sqlRun2.SqlSelectStatement(NumeratorSQL);
                Double numerator = 0.0;
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
