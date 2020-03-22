
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLInsert {
    JdbcConnect jbc2 = new JdbcConnect();
    String sql;
    boolean ExecuteInsert(String tableName, Map<String, String> columnValueMappingForSet)
    {
        sql = this.insertSQL(tableName, columnValueMappingForSet);

        try (
                PreparedStatement pstmt = jbc2.connect().prepareStatement(sql)) {
            pstmt.executeUpdate();
            jbc2.connect().close();
            return true;
        } catch (SQLException e) { System.out.println(e.getMessage());}
        return false;

    }


    public static String insertSQL(String tableName, Map<String, String> columnValueMappingForInsert) {
        StringBuilder insertSQLBuilder = new StringBuilder();

        /**
         * Removing column that holds NULL value or Blank value...
         */
        if (!columnValueMappingForInsert.isEmpty()) {
            for (Map.Entry<String, String> entry : columnValueMappingForInsert.entrySet()) {
                if(entry.getValue() == null || entry.getValue().equals("")) {
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

        // Returning the generated INSERT SQL Query as a String...
//	    System.out.println(insertSQLBuilder);
        return insertSQLBuilder.toString();
    }


    public static void main(String[] args) {
//		SQLInsert sqlIns = new SQLInsert();
//		HashMap<String, String> colValues = new HashMap<String, String>();
//		HashMap<String, String> where = new HashMap<String, String>();
//		String table = "bus_table";
//		colValues.put("user_id", "122");
//		colValues.put("dd", "122");
//		where.put("name", "rahul");
//		
//		boolean s = sqlIns.ExecuteInsert(table, colValues );
//		System.out.println(s);
//		
//		SQLInsert si = new SQLInsert();
//		HashMap<String, String> colValuesInsert = new HashMap<String, String>();
//		String tableName2 = "pass_details";	
//		colValuesInsert.put("bus_id", "1111");
//		colValuesInsert.put("route", "R1");
////		colValuesInsert.put("timing", "null");
//		colValuesInsert.put("user_id", "bhavikj");
//		System.out.println(colValuesInsert);
//
//		si.ExecuteInsert(tableName2, colValuesInsert);

    }

}