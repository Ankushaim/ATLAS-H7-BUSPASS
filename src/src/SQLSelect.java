import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSelect extends SQLMain {

    public ResultSet SqlSelectStatement(String sql) {
        ResultSet rs;

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                return rs;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}