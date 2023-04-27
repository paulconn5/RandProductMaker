import java.awt.*;
import java.io.*;
import javax.swing.*;

public class RandProductMaker extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JTextField descField;
    private JTextField costField;
    private JTextField countField;
    private JButton addButton;
    private JButton clearButton;

    private RandomAccessFile randomFile;

    private static final int RECORD_LENGTH = 126;
    private int recordCount = 0;

    public RandProductMaker() {
        super("Random Product Maker");

        // Initialize GUI components
        idField = new JTextField(10);
        nameField = new JTextField(30);
        descField = new JTextField(60);
        costField = new JTextField(10);
        countField = new JTextField(10);
        countField.setEditable(false);
        addButton = new JButton("Add");
        clearButton = new JButton("Clear");
        addButton.addActionListener(e -> addRecord());
        clearButton.addActionListener(e -> clearFields());

        // Set up  layout
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);
        inputPanel.add(new JLabel("Cost:"));
        inputPanel.add(costField);
        inputPanel.add(new JLabel("Record Count:"));
        inputPanel.add(countField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        Container contentPane = getContentPane();
        contentPane.add(inputPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Set up file
        try {
            randomFile = new RandomAccessFile("products.dat", "rw");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(400,400);
        setVisible(true);
    }

    private void addRecord() {
        // Validate input
        String id = padString(idField.getText().trim(), 6);
        String name = padString(nameField.getText().trim(), 35);
        String desc = padString(descField.getText().trim(), 75);
        String cost = padString(costField.getText().trim(), 10);

        // Write record to file
        try {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(id + name + desc + cost);
            recordCount++;
            countField.setText(Integer.toString(recordCount));
            JOptionPane.showMessageDialog(this, "Record added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Pad the string
    private String padString(String s, int length) {
        if (s.length() >= length) {
            return s.substring(0, length);
        } else {
            StringBuilder sb = new StringBuilder(s);
            while (sb.length() < length) {
                sb.append(' ');
            }
            return sb.toString();
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descField.setText("");
        costField.setText("");
    }

    public static void main(String[] args) {
        new RandProductMaker();
    }
}

