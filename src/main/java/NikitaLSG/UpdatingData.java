package NikitaLSG;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatingData {
    private Connection connection;

    public UpdatingData() {
        this.connection = connection;
    }

    public void updateData(String tableName, String columnName, Object oldValue, Object newValue) {
        try {
            String query = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + columnName + " = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, newValue);
            statement.setObject(2, oldValue);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
