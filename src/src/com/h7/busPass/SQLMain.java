package com.h7.busPass;
import java.sql.Connection;

abstract class SQLMain {
    static Connection conn = JdbcConnect.connect();
}
