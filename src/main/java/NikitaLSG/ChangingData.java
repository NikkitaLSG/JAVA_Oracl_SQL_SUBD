package NikitaLSG;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import NikitaLSG.ChangingData;

public class ChangingData implements ActionListener {
    private JList<String> tableList;
    private JTable resultTable;

    public ChangingData(JList<String> tableList, JTable resultTable) {
        this.tableList = tableList;
        this.resultTable = resultTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = resultTable.getSelectedRow();
        int selectedColumn = resultTable.getSelectedColumn();
        if (selectedRow != -1 && selectedColumn != -1) {
            String selectedTable = tableList.getSelectedValue();
            String columnName = resultTable.getColumnName(selectedColumn);
            Object valueToChange = resultTable.getValueAt(selectedRow, selectedColumn);
            if (selectedTable != null) {
                String newValue = JOptionPane.showInputDialog("Введите новое значение:");
                if (newValue != null && !newValue.isEmpty()) {
                    ChangingDataInDB changingDataInDB = new ChangingDataInDB();
                    changingDataInDB.changeData(selectedTable, columnName, valueToChange, newValue);
                    resultTable.setValueAt(newValue, selectedRow, selectedColumn);
                }
            }
        }
    }
}