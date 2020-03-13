import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class SqlLiteDemo {

    private  Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:BusPass.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public  void selectAll(){
        String sql = "SELECT bus_id, bus_driver FROM bus_table";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("bus_id") +  "\t" +
                        rs.getString("bus_driver") + "\t" );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public static void main(String[] args)throws Exception {

        SqlLiteDemo app = new SqlLiteDemo();
        app.selectAll();



    }
//		Connection c = null;
//		try {
//			Class.forName("org.sqlite.JDBC");
//			c=DriverManager.getConnection("jdbc:sqlite:BusPass.db");
//			System.out.println("SQL lite DB connected");
//
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//
//	}

}
