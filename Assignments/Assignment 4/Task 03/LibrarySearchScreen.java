import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibrarySearchScreen extends JFrame {
    private final JTextField search = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All","Book","Journal","Thesis"});
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Title","Category","Author","Status"},0);
    private final JTable results = new JTable(model);
    private final JLabel status = new JLabel("Ready");

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8,8));
        JLabel title = new JLabel("Campus Library",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD,20f));
        JPanel top = new JPanel(new BorderLayout());
        top.add(title,BorderLayout.NORTH);
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton find = new JButton("Search");
        searchRow.add(new JLabel("Search")); searchRow.add(search);
        searchRow.add(category); searchRow.add(find);
        top.add(searchRow,BorderLayout.SOUTH);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev"), next = new JButton("Next"), reserve = new JButton("Reserve");
        bottom.add(prev); bottom.add(next); bottom.add(reserve); bottom.add(status);
        add(top,BorderLayout.NORTH);
        add(new JScrollPane(results),BorderLayout.CENTER);
        add(bottom,BorderLayout.SOUTH);
        find.addActionListener(e -> performSearch());
        search.addActionListener(e -> performSearch());
        prev.addActionListener(e -> status.setText("Previous page"));
        next.addActionListener(e -> status.setText("Next page"));
        reserve.addActionListener(e -> {
            int row = results.getSelectedRow();
            if (row < 0) status.setText("Select a result to reserve.");
            else status.setText("Reservation requested: " + model.getValueAt(row,0));
        });
        model.addRow(new Object[]{"Effective Java","Book","Joshua Bloch","Available"});
        model.addRow(new Object[]{"Java Magazine","Journal","Editorial Team","Available"});
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(720,460); setLocationRelativeTo(null);
    }

    private void performSearch() {
        String term = search.getText().trim().toLowerCase();
        String selected = (String) category.getSelectedItem();
        status.setText("Search: " + term + " | Category: " + selected);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}