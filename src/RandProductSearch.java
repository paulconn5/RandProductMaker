import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class RandProductSearch extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultsArea;

    private RandomAccessFile randomFile;

    private static final int RECORD_LENGTH = 126;

    public RandProductSearch() {
        super("Random Product Search");

        // Initialize GUI components
        searchField = new JTextField(30);
        searchButton = new JButton("Search");
        resultsArea = new JTextArea(20, 50);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        // Set up  layout
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Search by Name:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);

        Container contentPane = getContentPane();
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Set up file
        try {
            randomFile = new RandomAccessFile("products.dat", "r");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Set up search button action
        searchButton.addActionListener(e -> searchRecords());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void searchRecords() {
        String searchName = searchField.getText().trim();
        if (searchName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search for", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> results = new ArrayList<String>();

        // Search records
        try {
            long fileLength = randomFile.length();
            long filePos = 0;
            while (filePos < fileLength) {
                randomFile.seek(filePos);
                byte[] recordBytes = new byte[RECORD_LENGTH];
                randomFile.read(recordBytes);
                String record = new String(recordBytes);
                String id = record.substring(0, 6).trim();
                String name = record.substring(6, 41).trim();
                String desc = record.substring(41, 116).trim();
                String cost = record.substring(116, 126).trim();
                if (name.contains(searchName)) {
                    results.add(id + " " + name + " " + desc + " $" + cost);
                }
                filePos += RECORD_LENGTH;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display results
        if (results.isEmpty()) {
            resultsArea.setText("No records found");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String result : results) {
                sb.append(result).append("\n");
            }
            resultsArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        new RandProductSearch();
    }
}
