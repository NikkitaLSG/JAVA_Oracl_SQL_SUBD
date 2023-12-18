package NikitaLSG;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddDataDialog extends JDialog {
    private JTextField dataField;
    private JButton addButton;
    private boolean dataAdded;
    private String[] data;

    public AddDataDialog(String selectedTable, Object[] rowData) {
        setTitle("Add Data");
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter data:");
        contentPane.add(label);

        dataField = new JTextField(20);
        contentPane.add(dataField);

        addButton = new JButton("Add");
        contentPane.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputData = dataField.getText();
                if (!inputData.isEmpty()) {
                    data = new String[rowData.length + 1];
                    for (int i = 0; i < rowData.length; i++) {
                        data[i] = rowData[i].toString();
                    }
                    data[rowData.length] = inputData;
                    dataAdded = true;
                }
                dispose();
            }
        });
    }

    public boolean isDataAdded() {
        return dataAdded;
    }

    public String[] getData() {
        return data;
    }
}
