package NikitaLSG;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class Interface {
    private final String host;
    private String service;
    private String user;
    private String password;
    private JList<String> tableList;
    private JTable resultTable;

    public Interface(String host, String service, String user, String password) {
        this.host = host;
        this.service = service;
        this.user = user;
        this.password = password;
    }
    public void createUI() {

        JFrame frame = new JFrame("СУБД");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        JLabel label1 = new JLabel("Вход в БД");
        panel.add(label1);

        panel.add(new JLabel("Введите Номер Хоста"));
        JTextField hostField = new JTextField("10.242.100.15");
        panel.add(hostField);

        panel.add(new JLabel("Введите Имя Service"));
        JTextField serviceField = new JTextField("dblab.miit.ru");
        panel.add(serviceField);

        panel.add(new JLabel("Введите Имя Пользователя"));
        JTextField userField = new JTextField("e1144610");
        panel.add(userField);

        panel.add(new JLabel("Введите Пароль"));
        JPasswordField passwordField = new JPasswordField("e1144610");
        panel.add(passwordField);

        JButton loginButton = new JButton("Войти");
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hostText = hostField.getText();
                String serviceText = serviceField.getText();
                String userText = userField.getText();
                String passwordText = new String(passwordField.getPassword());

                if (!hostText.isEmpty() && !serviceText.isEmpty() && !userText.isEmpty() && !passwordText.isEmpty()) {
                    System.out.println("Host: " + hostText);
                    System.out.println("Service: " + serviceText);
                    System.out.println("User: " + userText);
                    System.out.println("Password: " + passwordText);

                    ConnectDB connectDB = new ConnectDB(hostText, serviceText, userText, passwordText);

                    JFrame newFrame = new JFrame("Окно Выбора действия");
                    newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    newFrame.setSize(500, 500);

                    JPanel panel2 = new JPanel();
                    panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
                    newFrame.add(panel2);

                    JLabel label3 = new JLabel("Действия с БД");
                    panel2.add(label3);

                    JButton delButton = new JButton("Удалить");
                    panel2.add(delButton);

                    JButton changeButton = new JButton("Изменить");
                    panel2.add(changeButton);

                    JButton addButton = new JButton("Добавить");
                    panel2.add(addButton);

                    JLabel sqlInputLabel = new JLabel("SQL Команда:");
                    panel2.add(sqlInputLabel);

                    JTextArea sqlInputArea = new JTextArea(3, 30);
                    sqlInputArea.setLineWrap(true);

                    JScrollPane sqlInputScroll = new JScrollPane(sqlInputArea);
                    panel2.add(sqlInputScroll);

                    JButton executeButton = new JButton("Выполнить");
                    panel2.add(executeButton);

                    JTextArea sqlOutputArea = new JTextArea(10, 30);
                    sqlOutputArea.setEditable(false);
                    sqlOutputArea.setWrapStyleWord(true);

                    JScrollPane sqlOutputScroll = new JScrollPane(sqlOutputArea);
                    panel2.add(sqlOutputScroll);
                    frame.setVisible(true);
                    executeButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String sql = sqlInputArea.getText();
                                    Connection connection = null;
                                    Statement statement = null;
                                    try {
                                        connection = connectDB.getConnection();
                                        statement = connection.createStatement();
                                        if (sql.trim().toUpperCase().startsWith("SELECT")) {

                                            ResultSet resultSet = statement.executeQuery(sql);
                                            ResultSetMetaData metaData = resultSet.getMetaData();
                                            int columnCount = metaData.getColumnCount();
                                            sqlOutputArea.setText("");
                                            for (int i = 1; i <= columnCount; i++) {
                                                sqlOutputArea.append(metaData.getColumnLabel(i) + "\t");
                                            }
                                            sqlOutputArea.append("\n");


                                            while (resultSet.next()) {
                                                for (int i = 1; i <= columnCount; i++) {
                                                    sqlOutputArea.append(resultSet.getString(i) + "\t");
                                                }
                                                sqlOutputArea.append("\n");
                                            }
                                        } else {

                                            int result = statement.executeUpdate(sql);
                                            if (result > 0) {
                                                sqlOutputArea.setText("Запрос успешно выполнен. Затронуто строк: " + result);
                                            } else {
                                                sqlOutputArea.setText("Запрос успешно выполнен.");
                                            }
                                        }
                                    } catch (SQLException ex) {
                                        sqlOutputArea.setText("Ошибка SQL: " + ex.getMessage());
                                    } finally {

                                        try {
                                            if (statement != null) statement.close();
                                            if (connection != null) connection.close();
                                        } catch (SQLException ex) {
                                            sqlOutputArea.setText("Ошибка закрытия соединения: " + ex.getMessage());
                                        }
                                    }
                                }
                            });
                            addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selectedTable = tableList.getSelectedValue();
                            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
                            Object[] newRow = new Object[tableModel.getColumnCount()];
                            int lastRowIndex = tableModel.getRowCount();
                            for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
                                Object cellValue = tableModel.getValueAt(lastRowIndex - 1, columnIndex);
                                newRow[columnIndex] = cellValue;
                            }
                            Connection connection = connectDB.getConnection();
                            try {
                                Statement statement = connection.createStatement();
                                StringBuilder queryBuilder = new StringBuilder();
                                queryBuilder.append("INSERT INTO e1144610.\"СОТРУДНИк\" (\"ID_СОТРУДИКА\", \"ID_Время\", \"id_Обьяснительные\", \"id_Оклад\", \"Телефон\", \"ФИО\") VALUES (9, null, null, null, '79999999999', 'Матвеенко Тигран Великович')");
                                statement.executeUpdate(queryBuilder.toString());
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    JTable table = new JTable();
                    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent event) {
                            if (!event.getValueIsAdjusting()) {
                                int selectedRow = table.getSelectedRow();
                                if (selectedRow != -1) {
                                }
                            }
                        }
                    });
                    JButton selectTableButton = new JButton("Выбрать таблицу");
                    panel2.add(selectTableButton);
                    selectTableButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Connection connection = connectDB.getConnection();
                            try {
                                DatabaseMetaData metaData = connection.getMetaData();
                                ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
                                DefaultListModel<String> tableListModel = new DefaultListModel<>();
                                tableList = new JList<>(tableListModel);
                                JScrollPane tableScrollPane = new JScrollPane(tableList);
                                panel2.add(tableScrollPane);
                                tableList.addListSelectionListener(new ListSelectionListener() {
                                    @Override
                                    public void valueChanged(ListSelectionEvent event) {
                                        if (!event.getValueIsAdjusting()) {
                                            String selectedTable = tableList.getSelectedValue();
                                            if (selectedTable != null) {
                                                try (ResultSet columns = metaData.getColumns(null, null, selectedTable, null)) {
                                                    DefaultTableModel tableModel = new DefaultTableModel();
                                                    resultTable = new JTable(tableModel);
                                                    JScrollPane resultScrollPane = new JScrollPane(resultTable);
                                                    panel2.add(resultScrollPane);
                                                    while (columns.next()) {
                                                        String columnName = columns.getString("COLUMN_NAME");
                                                        tableModel.addColumn(columnName);
                                                    }
                                                    Statement statement = connection.createStatement();
                                                    ResultSet resultSet = statement.executeQuery(" SELECT * FROM " +"e1144610."+"\"" + selectedTable + "\"");
                                                    ResultSetMetaData rsMetaData = resultSet.getMetaData();
                                                    int columnCount = rsMetaData.getColumnCount();

                                                    while (resultSet.next()) {
                                                        Object[] rowData = new Object[columnCount];
                                                        for (int i = 1; i <= columnCount; i++) {
                                                            rowData[i - 1] = resultSet.getObject(i);
                                                        }
                                                        tableModel.addRow(rowData);
                                                    }
                                                    panel2.revalidate();
                                                    panel2.repaint();
                                                } catch (SQLException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                                while (tables.next()) {
                                    String tableName = tables.getString("TABLE_NAME");
                                    tableListModel.addElement(tableName);
                                }
                                panel2.revalidate();
                                panel2.repaint();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    delButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int selectedRow = resultTable.getSelectedRow();
                            if (selectedRow != -1) {
                                String selectedTable = tableList.getSelectedValue();
                                if (selectedTable != null) {
                                    Object primaryKeyValue = resultTable.getValueAt(selectedRow, 0);
                                    Connection connection = connectDB.getConnection();
                                    try {
                                        Statement statement = connection.createStatement();
                                        String deleteQuery = "DELETE FROM e1144610." +"\"" + selectedTable +"\"" + " WHERE " +"ID_СОТРУДИКА"+ " = " + primaryKeyValue;
                                        statement.executeUpdate(deleteQuery);
                                        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
                                        tableModel.removeRow(selectedRow);
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    changeButton.addActionListener(new ChangingData(tableList, resultTable));
                    JButton timeButton = new JButton("Время_ПРИХ");
                    panel2.add(timeButton);
                    timeButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String selectedTable = tableList.getSelectedValue();
                            if (selectedTable != null) {
                                JFrame timeFrame = new JFrame("Время прихода и ухода");
                                timeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                timeFrame.setSize(300, 200);
                                JPanel timePanel = new JPanel();
                                timeFrame.add(timePanel);

                                JLabel timePrichLabel = new JLabel("Время прихода:");
                                JTextField timePrichField = new JTextField(10);

                                JLabel timeUkhodLabel = new JLabel("Время ухода:");
                                JTextField timeUkhodField = new JTextField(10);

                                JButton increaseButton = new JButton("Увеличить время ухода");
                                timePanel.add(increaseButton);

                                JLabel idSotrudnikaLabel = new JLabel("ID_СОТРУДНИКА:");
                                JTextField idSotrudnikaField = new JTextField(10);

                                JLabel countLateLabel = new JLabel("КоличествоОпозданий:");
                                JTextField countLateField = new JTextField(10);

                                JButton countLateButton = new JButton("Посчитать опоздания");
                                timePanel.add(countLateButton);
                                timePanel.add(idSotrudnikaLabel);
                                timePanel.add(idSotrudnikaField);

                                timePanel.add(countLateLabel);
                                timePanel.add(countLateField);

                                timePanel.add(timePrichLabel);
                                timePanel.add(timePrichField);

                                timePanel.add(timeUkhodLabel);
                                timePanel.add(timeUkhodField);
                                countLateButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int countLate = Integer.parseInt(Objects.requireNonNull(countLateField.getText()));
                                        countLate++;
                                        countLateField.setText(String.valueOf(countLate));
                                    }
                                });
                                increaseButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                        int timeUkhod = Integer.parseInt(Objects.requireNonNull(timeUkhodField.getText()));
                                        timeUkhod++;
                                        timeUkhodField.setText(String.valueOf(timeUkhod));
                                    }
                                });
                                JButton saveButton = new JButton("Сохранить");
                                timePanel.add(saveButton);

                                saveButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                    }
                                });
                                class TimePrichData {
                                    public int saveTimePrich(String tableName, int idSotrudnika, int countLate, String timePrich, String timeUkhod) {
                                        //Генератор
                                        UUID uuid = UUID.randomUUID();
                                        int idVremya = Math.abs(uuid.hashCode());

                                        Connection connection = connectDB.getConnection();
                                        try {
                                            Statement statement = connection.createStatement();
                                            StringBuilder queryBuilder = new StringBuilder();
                                            queryBuilder.append("INSERT INTO e1144610.\"").append(tableName).append("\" (\"ID_СОТРУДНИКА\", \"ID_Время\", \"Время Прихода\", \"Колличевство_Выходов\", \"Время_Ухода\",) VALUES (")
                                                    .append(idVremya).append(", '").append(idSotrudnika).append("', ").append(countLate).append(", '").append(timePrich).append("', '").append(timeUkhod).append("')");
                                            statement.executeUpdate(queryBuilder.toString());
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                        return idVremya;
                                    }
                                }
                                    saveButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                int idSotrudnika = Integer.parseInt(idSotrudnikaField.getText());
                                                int countLate = Integer.parseInt(countLateField.getText());
                                                String timePrich = timePrichField.getText();
                                                String timeUkhod = timeUkhodField.getText();
                                                TimePrichData timePrichData = new TimePrichData();
                                                int idVremya = timePrichData.saveTimePrich(selectedTable, idSotrudnika, countLate, timePrich, timeUkhod);
                                                timeFrame.dispose();
                                            } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(timeFrame, "Введите корректные числовые значения.");
                                            }
                                        }
                                    });
                                timeFrame.setVisible(true);
                            }
                        }
                    });
                    panel2.revalidate();
                    panel2.repaint();
                    newFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Заполните все поля!");
                }
            }

        });
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        Interface ui = new Interface("host", "service", "user", "password");
        ui.createUI();
    }
}