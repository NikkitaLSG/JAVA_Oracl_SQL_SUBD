package NikitaLSG;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TimePrichData {
    private Connection connection;

    public TimePrichData() {
        this.connection = connection;
    }

    public void getTimePrich(String tableName) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT Время_ПРИХ FROM "+ "E1141379." + tableName;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String timePrich = resultSet.getString("Время_Прихода");
                System.out.println("Время прихода: " + timePrich);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTimePrich(String selectedTable, String timePrich, String timeUkhod) {
        try {
            Statement statement = connection.createStatement();
            String query = "UPDATE "+ "E1141379." + selectedTable +
                    " SET Время_Прихода = '" + timePrich + "', Время_Ухода = '" + timeUkhod + "'";
            statement.executeUpdate(query);

            System.out.println("Данные успешно сохранены.");

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}