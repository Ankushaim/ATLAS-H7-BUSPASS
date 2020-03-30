package com.h7.busPass;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SQLUpdate extends SQLMain {
    String sql;

    // updateSQL is internally called by ExecuteInsert: To generate SQL Syntax
    public static String updateSQL(String tableName, Map<String, String> columnValueMappingForSet, Map<String, String> columnValueMappingForCondition) {
        StringBuilder updateQueryBuilder = new StringBuilder();

        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    columnValueMappingForCondition.remove(entry.getKey());
                }
            }
        }
        /* Making the UPDATE Query */
        updateQueryBuilder.append("UPDATE");
        updateQueryBuilder.append(" ").append(tableName);
        updateQueryBuilder.append(" SET");
        updateQueryBuilder.append(" ");

        if (!columnValueMappingForSet.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForSet.entrySet()) {
                updateQueryBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                updateQueryBuilder.append(",");
            }
        }
        updateQueryBuilder = new StringBuilder(updateQueryBuilder.subSequence(0, updateQueryBuilder.length() - 1));
        updateQueryBuilder.append(" WHERE");
        updateQueryBuilder.append(" ");

        if (!columnValueMappingForCondition.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
                updateQueryBuilder.append(entry.getKey()).append(" IN (").append(entry.getValue()).append(")");
                updateQueryBuilder.append(",");
            }
        }
        updateQueryBuilder = new StringBuilder(updateQueryBuilder.subSequence(0, updateQueryBuilder.length() - 1));
        return updateQueryBuilder.toString();
    }

    /*ExecuteUpdate method is used by lots of other method to update data in any table of database.
     * To achieve updation in any table we used HASH MAP data structure. Where "Key" is the "column name" and "Value" is the "data" of the column.
     * 1. FIrstly, generate SQL syntax call  SQLUpdate.
     * 2. Execute the generated SQL
     */
    boolean ExecuteUpdate(String tableName, Map<String, String> columnValueMappingForSet, Map<String, String> columnValueMappingForCondition) {
        //1. FIrstly, generate SQL syntax. Call  SQLUpdate.
        sql = updateSQL(tableName, columnValueMappingForSet, columnValueMappingForCondition);

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