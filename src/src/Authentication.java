import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication {
    private String inputName;
    private String inputPassword;
    private String	userType;

    boolean checkCredentials(String log, String pass, String type) throws SQLException {

        String sql = "select login, password, type from user_info where login = '"+log+"' ";
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        if(!rs.isBeforeFirst())
        {
            return false;
        }
        inputName = rs.getString("login");
        inputPassword = rs.getString("password");
        userType = rs.getString("type");
        rs.close();

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