package NikitaLSG;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Interface {
    private String host;
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
        JTextField userField = new JTextField("e1141379");
        panel.add(userField);

        panel.add(new JLabel("Введите Пароль"));
        JPasswordField passwordField = new JPasswordField("e1141379");
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

                    JFrame newFrame = new JFrame("Новое окно");
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


                    addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selectedTable = tableList.getSelectedValue();
                            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
                            Object[] newRow = new Object[tableModel.getColumnCount()];
                            tableModel.addRow(newRow);

                            // Получаем значения для новой строки из пользовательского интерфейса
                            int lastRowIndex = tableModel.getRowCount() - 1;
                            for (int columnIndex = 1; columnIndex < tableModel.getColumnCount(); columnIndex++) {
                                Object cellValue = tableModel.getValueAt(lastRowIndex, columnIndex);
                                newRow[columnIndex] = cellValue;
                            }

                            // Выполняем SQL-запрос для добавления новой строки в базу данных
                            Connection connection = connectDB.getConnection();
                            try {
                                Statement statement = connection.createStatement();
                                StringBuilder queryBuilder = new StringBuilder();
                                queryBuilder.append("INSERT INTO ").append(selectedTable).append(" VALUES (DEFAULT, ");
                                for (int columnIndex = 1; columnIndex < tableModel.getColumnCount(); columnIndex++) {
                                    Object cellValue = newRow[columnIndex];
                                    if (cellValue instanceof String) {
                                        queryBuilder.append("'").append(cellValue).append("'");
                                    } else {
                                        queryBuilder.append(cellValue);
                                    }
                                    if (columnIndex < tableModel.getColumnCount() - 1) {
                                        queryBuilder.append(", ");
                                    }
                                }
                                queryBuilder.append(")");
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
                                    // Получаем данные из выбранной строки и отображаем их в других компонентах вашего интерфейса
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
                                                    ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "STUD." + selectedTable);
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
                            int selectedColumn = resultTable.getSelectedColumn();
                            if (selectedRow != -1 && selectedColumn != -1) {
                                String selectedTable = tableList.getSelectedValue();
                                String columnName = resultTable.getColumnName(selectedColumn);
                                Object valueToDelete = resultTable.getValueAt(selectedRow, selectedColumn);
                                if (selectedTable != null) {
                                    DeletingData deletingData = new DeletingData();
                                    deletingData.deleteData(selectedTable, columnName, valueToDelete);
                                    DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
                                    tableModel.removeRow(selectedRow);
                                }
                            }
                        }
                    });
                    changeButton.addActionListener(new ChangingData(tableList, resultTable));

                    panel2.revalidate();
                    panel2.repaint();

                    newFrame.setVisible(true);
                }
            }
        });

        frame.setVisible(true);
    }

    public void setVisible(boolean b) {
    }
    public static void main(String[] args) {
        Interface interfaceObj = new Interface("", "", "", "");
        interfaceObj.createUI();
    }
}
