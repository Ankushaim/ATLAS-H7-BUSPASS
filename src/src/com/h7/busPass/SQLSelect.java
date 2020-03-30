package com.h7.busPass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSelect extends SQLMain {
	
	//SqlSelectStatement method is used to fetch data from database. Any method in the project can call this method to extract data from database.
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