import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SQLInsert extends SQLMain {

    String sql;

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

    boolean ExecuteInsert(String tableName, Map<String, String> columnValueMappingForSet) {
        sql = insertSQL(tableName, columnValueMappingForSet);

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