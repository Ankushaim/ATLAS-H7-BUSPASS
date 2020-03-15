import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}

