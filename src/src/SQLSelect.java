import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSelect {
    Connection con = JdbcConnect.connect();

    public ResultSet SqlSelectStatement(String sql) throws SQLException	{
        ResultSet rs;

        if(con != null)
        {
            try
            {
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(sql);
                return rs;
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        assert con != null;
        con.close();
        return null;
    }
}