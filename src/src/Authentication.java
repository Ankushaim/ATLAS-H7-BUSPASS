import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    Connection conn;
    private String inputName;
    private String inputPassword;
    private String userType;



    boolean checkCredentials(String log, String pass, String type) { //getting called by guest also

        //String sql = "select login, password, type from user_info where login = '" + log + "' ";
        String sql = "select login, password, type from user_info where login = ?";
//        try {
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            preparedStatement.setString(1, log);
//            ResultSet rs = preparedStatement.executeQuery();
//            if (rs.next()) {
//                inputName = rs.getString("login");
//                inputPassword = rs.getString("password");
//                userType = rs.getString("type");
//            }
//        } catch (SQLException e) {
//            return false;
//        }

        SQLSelect sqlRun = new SQLSelect();
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