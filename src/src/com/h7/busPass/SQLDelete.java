package com.h7.busPass;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SQLDelete extends SQLMain {
	public static String deleteSQL(String tableName, Map<String, String> columnValueMappingForCondition) {
	    StringBuilder deleteSQLBuilder = new StringBuilder();

	    if (!columnValueMappingForCondition.isEmpty()) {
	        for (Map.Entry<String, String> entry : columnValueMappingForCondition.entrySet()) {
	            if(entry.getValue() == null || entry.getValue().equals("")) {
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

	public boolean executeDelete(String tableName, Map<String, String> columnValueMappingForCondition) {
		String sql;
		sql = deleteSQL(tableName, columnValueMappingForCondition);
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}
