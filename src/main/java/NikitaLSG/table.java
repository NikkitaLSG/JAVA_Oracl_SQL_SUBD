package NikitaLSG;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class table extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public table(ResultSet resultSet) {
        setLayout(new BorderLayout());

        // Создание модели таблицы
        tableModel = new DefaultTableModel();
        try {
            // Получение метаданных (названия колонок) из ResultSet
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Добавление данных из ResultSet в модель таблицы
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Создание JTable на основе модели таблицы
        table = new JTable(tableModel);

        // Добавление JTable в JScrollPane для возможности прокрутки таблицы
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}