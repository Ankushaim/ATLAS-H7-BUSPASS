import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    Connection conn;
    private String inputName;
    private String inputPassword;
    private String userType;

    public Authentication(Connection conn) {
        this.conn = conn;
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = JdbcConnect.connect();
        Authentication auth = new Authentication(conn);
        String log = "admin/";
        String pass = "admin";
        String type = "admin";
        System.out.println(auth.checkCredentials(log, pass, type));


    }

    boolean checkCredentials(String log, String pass, String type) throws SQLException {

        String sql = "select login, password, type from user_info where login = '" + log + "' ";

        SQLSelect sqlRun = new SQLSelect(conn);
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        try {
            rs.next();
            inputName = rs.getString("login");
            inputPassword = rs.getString("password");
            userType = rs.getString("type");
        } catch (Exception e) {
            return false;
        }


        if (userType.equals("admin")) {
            return inputName.equals(log) && inputPassword.equals(pass) && userType.equals(type);

        }
        if (userType.equals("user")) {
            return inputName.equals(log) && inputPassword.equals(pass) && userType.equals(type);
        }
        return false;
    }
}