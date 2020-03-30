package com.h7.busPass;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SQLUpdate extends SQLMain {
    String sql;

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

    boolean ExecuteUpdate(String tableName, Map<String, String> columnValueMappingForSet, Map<String, String> columnValueMappingForCondition) {
        sql = updateSQL(tableName, columnValueMappingForSet, columnValueMappingForCondition);

        try (
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}