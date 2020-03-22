import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLUpdate {
    JdbcConnect jbc2 = new JdbcConnect();
    String sql;
    public static String updateSQL(String tableName, Map<String, String> columnValueMappingForSet, Map<String, String> columnValueMappingForCondition) {
        StringBuilder updateQueryBuilder = new StringBuilder();

        /**
         * Removing column that holds NULL value or Blank value...
         */
//	    if (!columnValueMappingForSet.isEmpty()) {
//	        for (Map.Entry<String, String> entry : columnValueMappingForSet.entrySet()) {
//	            if(entry.getValue() == null || entry.getValue().equals("")) {
//	                columnValueMappingForSet.remove(entry.getKey());
//	            }
//	        }
//	    }

        /**
         * Removing column that holds NULL value or Blank value...
         */
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

        // Returning the generated UPDATE SQL Query as a String...
        System.out.println(updateQueryBuilder);
        return updateQueryBuilder.toString();
    }

    public static void main(String[] args) {
        SQLUpdate sqlUpd = new SQLUpdate();
        HashMap<String, String> colValues = new HashMap<String, String>();
        HashMap<String, String> where = new HashMap<String, String>();
        String table = "bus_table";
        String stopname = "E1";
        String login = "janeshs";
        String direction = "EAST";

        colValues.put("stop", stopname);
        colValues.put("status", "PENDING");

        where.put("login", "'" + login + "'");
//		where.put("login"," SELECT stop from stop_info where direction= '"+direction+ "'");
//
//		String s = sqlUpd.updateSQL(table, colValues , where);
//		System.out.println(s);
        sqlUpd.ExecuteUpdate(table, colValues, where);


    }

    boolean ExecuteUpdate(String tableName, Map<String, String> columnValueMappingForSet, Map<String, String> columnValueMappingForCondition) {
        sql = this.updateSQL(tableName, columnValueMappingForSet, columnValueMappingForCondition);


        try (
                PreparedStatement pstmt = jbc2.connect().prepareStatement(sql)) {
            pstmt.executeUpdate();
            jbc2.connect().close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;

    }

}