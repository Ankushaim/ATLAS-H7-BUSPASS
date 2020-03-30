package com.h7.busPass;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {
	//This method to create JDBC connection object once and used by other methods wherever required. 
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + "h7-bus-pass-atlas.cshwoihh6dqb.us-east-1.rds.amazonaws.com" + ":" + "3306" + "/" + "H7", "admin", "atlas1234");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void closeCon(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}