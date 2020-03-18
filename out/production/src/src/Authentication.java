import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication {
    private String inputName;
    private String inputPassword;
    private String	userType;

    boolean checkCredentials(String log, String pass, String type) {
        JdbcConnect jbc = new JdbcConnect();
        if(jbc.connect() != null)
        {
            String sql = "select login, password, type from user_info ";
            try(Statement stmt = jbc.connect().createStatement();
                ResultSet rs = stmt.executeQuery(sql))
            {
                while (rs.next())
                {
//					System.out.println(rs.getString("login") +  "\t" +
//					rs.getString("password") + "\t" + rs.getString("type")	);
                    inputName = rs.getString("login");
                    inputPassword = rs.getString("password");
                    userType = rs.getString("type");
                    if(userType.equals("admin"))
                    {
                        if (inputName.equals(log) && inputPassword.equals(pass) && userType.equals(type))
                        {
                            return true;
                        }
                    }
                    if(userType.equals("user"))
                    {
                        if (inputName.equals(log) && inputPassword.equals(pass) && userType.equals(type))
                        {
                            return true;
                        }
                    }
                }
            }catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }
}
