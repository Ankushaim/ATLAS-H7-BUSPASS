import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSelect {

    Connection conn;

    public SQLSelect(Connection conn) {
        this.conn = conn;
    }

    public ResultSet SqlSelectStatement(String sql) throws SQLException {

        ResultSet rs = null;
        if (conn != null) {
            String sqlToRun = sql;
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sqlToRun);
                return rs;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return rs;


    }
//
//public static void main(String[] args) throws SQLException, ClassNotFoundException  {
//
//    	SQLSelect ss = new SQLSelect();
//    	Connection conn = JdbcConnect.connect();
//
//    	String sql= "select login from user_info";
//    	ResultSet rs = ss.SqlSelectStatement(sql, conn);
//    	while(rs.next()) {
//    		System.out.println(rs.getString("login"));
//    	}
//    	System.out.println("j");
//    	String sql2= "select password from user_info";
//    	ResultSet rs2 = ss.SqlSelectStatement(sql2, conn);
//    	while(rs2.next()) {
//    		System.out.println(rs2.getString("password"));
//    	}
//
//
//
//}

}