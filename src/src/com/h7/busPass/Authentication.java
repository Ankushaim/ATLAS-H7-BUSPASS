package com.h7.busPass;
import java.sql.ResultSet;

public class Authentication {
    boolean checkCredentials(String log, String pass, String type) {

        String sql = "select login, password, type from user_info where login = '" + log + "' ";
        SQLSelect sqlRun = new SQLSelect();
        ResultSet rs = sqlRun.SqlSelectStatement(sql);
        String inputName;
        String inputPassword;
        String userType;
        try {
            rs.next();
            inputName = rs.getString("login");
            inputPassword = rs.getString("password");
            userType = rs.getString("type");
        } catch (Exception e) {
            return false;
        }

        final boolean b = inputName.equals(log) && inputPassword.equals(pass) && userType.equals(type);
        if (userType.equals("admin")) {
            return b;
        }

        if (userType.equals("user")) {
            return b;
        }
        return false;
    }
}