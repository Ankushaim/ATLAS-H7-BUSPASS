package com.h7.busPass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SQLInsert extends SQLMain {

    String sql;

    // insertSQL is internally called by ExecuteInsert: To generate SQL Syntax
    public static String insertSQL(String tableName, Map<String, String> columnValueMappingForInsert) {
        StringBuilder insertSQLBuilder = new StringBuilder();

        if (!columnValueMappingForInsert.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForInsert.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    columnValueMappingForInsert.remove(entry.getKey());
                }
            }
        }
        /* Making the INSERT Query... */
        insertSQLBuilder.append("INSERT INTO");
        insertSQLBuilder.append(" ").append(tableName);
        insertSQLBuilder.append("(");

        if (!columnValueMappingForInsert.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForInsert.entrySet()) {
                insertSQLBuilder.append(entry.getKey());
                insertSQLBuilder.append(",");
            }
        }
        insertSQLBuilder = new StringBuilder(insertSQLBuilder.subSequence(0, insertSQLBuilder.length() - 1));
        insertSQLBuilder.append(")");
        insertSQLBuilder.append(" VALUES");
        insertSQLBuilder.append("(");

        if (!columnValueMappingForInsert.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForInsert.entrySet()) {
                insertSQLBuilder.append("'").append(entry.getValue()).append("'");
                insertSQLBuilder.append(",");
            }
        }
        insertSQLBuilder = new StringBuilder(insertSQLBuilder.subSequence(0, insertSQLBuilder.length() - 1));
        insertSQLBuilder.append(")");
        return insertSQLBuilder.toString();
    }

    /*ExecuteInsert method is used by lots of other method to insert data in any table of database.
     * To achieve insertion in any table we used HASH MAP data structure. Where "Key" is the "column name" and "Value" is the "data" of the column.
     * 1. FIrstly, generate SQL syntax call  insertSQL.
     * 2. Execute the generated SQL
     */
    boolean ExecuteInsert(String tableName, Map<String, String> columnValueMappingForSet) {
        //1. FIrstly, generate SQL syntax. Call insertSQL.
        sql = insertSQL(tableName, columnValueMappingForSet);

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //2. Execute the generated SQL
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}