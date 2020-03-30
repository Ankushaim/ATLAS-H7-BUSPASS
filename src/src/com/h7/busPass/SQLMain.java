/*
 * To use only one object for SQL statements like Update, Insert, Select and Delete.
 * SQLSelect, SQLUpdate, SQLInsert and SQLDelete clases are extending this class.
 * * @author (Janesh)
 * @version (Java 8)
 */

package com.h7.busPass;

import java.sql.Connection;

abstract class SQLMain {
    static Connection conn = JdbcConnect.connect();
}
