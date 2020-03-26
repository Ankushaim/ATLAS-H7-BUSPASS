import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {
    public static Connection connect() {
        // SQLite connection string
//        String url = "jdbc:sqlite:BusPass.db"; // hard code DB
        Connection conn = null;
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Where is your MySQL JDBC Driver?");
//            e.printStackTrace();
//        }
        if (conn == null) {
            try {
//            conn = DriverManager.getConnection(url);
                conn = DriverManager.getConnection("jdbc:mysql://" + "h7-bus-pass-atlas.cshwoihh6dqb.us-east-1.rds.amazonaws.com" + ":" + "3306" + "/" + "H7", "admin", "atlas1234");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }

    public static void  closeCon(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}