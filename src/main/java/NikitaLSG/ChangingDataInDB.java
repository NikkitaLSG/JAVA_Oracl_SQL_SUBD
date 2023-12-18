package NikitaLSG;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import NikitaLSG.ChangingData;

public class ChangingDataInDB {
    public void changeData(String tableName, String columnName, Object oldValue, String newValue) {
        Connection connection = ConnectDB.getConnection();
        String sql = "UPDATE " + "STUD."+ tableName + " SET " + columnName + " = ? WHERE " + columnName + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newValue);
            statement.setObject(2, oldValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}