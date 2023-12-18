package NikitaLSG;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeletingData {

    public void deleteData(int id, Connection conn) {
        String sql = "DELETE FROM mytable WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Запись успешно удалена!");
            } else {
                System.out.println("Запись не найдена.");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении данных: " + e.getMessage());
        }
    }

    public void deleteData(String selectedTable, String columnName, Object valueToDelete) {
        // Ваш код для удаления данных из базы данных
    }
}
