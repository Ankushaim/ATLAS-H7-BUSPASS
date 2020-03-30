package com.h7.busPass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SQLDelete extends SQLMain {

    // deleteSQL is internally called by ExecuteInsert: To generate SQL Syntax
    public static String deleteSQL(String tableName, Map<String, String> columnValueMappingForCondition) {
        StringBuilder deleteSQLBuilder = new StringBuilder();

        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    columnValueMappingForCondition.remove(entry.getKey());
                }
            }
        }

        deleteSQLBuilder.append("DELETE FROM");
        deleteSQLBuilder.append(" ").append(tableName);
        deleteSQLBuilder.append(" WHERE");
        deleteSQLBuilder.append(" ");

        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                deleteSQLBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                deleteSQLBuilder.append(" AND ");
            }
        }

        deleteSQLBuilder = new StringBuilder(deleteSQLBuilder.subSequence(0, deleteSQLBuilder.length() - 5));
        return deleteSQLBuilder.toString();
    }


    /*executeDelete method is used by lots of other method to delete data in any table of database.
     * To achieve deletion in any table we used HASH MAP data structure. Where "Key" is the "column name" and "Value" is the "data" of the column.
     * 1. FIrstly, generate SQL syntax call  SQLDelete.
     * 2. Execute the generated SQL
     */
    public boolean executeDelete(String tableName, Map<String, String> columnValueMappingForCondition) {
        String sql;
        //1. FIrstly, generate SQL syntax. Call SQLDelete.
        sql = deleteSQL(tableName, columnValueMappingForCondition);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //2. Execute the generated SQL
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
