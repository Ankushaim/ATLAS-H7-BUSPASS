import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {

    boolean checkCredentials(String log, String pass, String type) throws SQLException {

        String sql = "select login, password, type from user_info where login = '" + log + "' ";
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        if (!rs.isBeforeFirst()) {
            return false;
        }
        String inputName = rs.getString("login");
        String inputPassword = rs.getString("password");
        String userType = rs.getString("type");
        rs.close();

        final boolean b = inputName.equals(log) && inputPassword.equals(pass) && userType.equals(type);
        if (userType.equals("admin")) {
            if (b) {
                return true;
            }
        }
        if(userType.equals("user"))
        {
            return b;
        }

        return false;
    }

//    public static void main(String[] args) throws SQLException  {
//        Authentication auth = new Authentication();
//        String log = "janeshs";
//        String pass = "janesh";
//        String type = "user";
//        System.out.println(auth.checkCredentials(log, pass, type));
//
//
//
//
//    }
}