import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

class FeeService {
    private static final String URL = "jdbc:mysql://localhost:3306/campus";
    private static final String USER = "root";
    private static final String PWD = "password";

    public void pay(String regNo, String amountText) throws Exception {
        if (regNo == null || !regNo.matches("[A-Za-z0-9]{5,20}"))
            throw new IllegalArgumentException("Enter a valid registration number.");
        double amount;
        try { amount = Double.parseDouble(amountText.trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Enter a valid amount."); }
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        try (Connection c = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement ps = c.prepareStatement("INSERT INTO fees(reg_no, amount) VALUES (?, ?)")) {
            ps.setString(1, regNo);
            ps.setDouble(2, amount);
            ps.executeUpdate();
        }
    }

    public String receipt(String regNo) throws SQLException {
        try (Connection c = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement ps = c.prepareStatement(
                 "SELECT reg_no, amount, paid_at FROM fees WHERE reg_no=? ORDER BY paid_at DESC LIMIT 1")) {
            ps.setString(1, regNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return "No receipt found.";
                return "Registration: " + rs.getString("reg_no") + "\nAmount: Rs " +
                       rs.getDouble("amount") + "\nPaid at: " + rs.getTimestamp("paid_at");
            }
        }
    }
}

public class FeeWindow extends JFrame {
    private final JTextField regNo = new JTextField(15);
    private final JTextField amount = new JTextField(10);
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new GridLayout(3, 2, 8, 8));
        add(new JLabel("Registration number")); add(regNo);
        add(new JLabel("Amount (INR)")); add(amount);
        JButton pay = new JButton("Pay"), clear = new JButton("Clear"), receipt = new JButton("Receipt");
        JPanel actions = new JPanel();
        actions.add(pay); actions.add(clear); actions.add(receipt);
        add(new JLabel("")); add(actions);
        pay.addActionListener(e -> payFee());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); });
        receipt.addActionListener(e -> showReceipt());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(FeeWindow.this, "Close payment window?",
                        "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) dispose();
            }
        });
        pack(); setLocationRelativeTo(null);
    }

    private void payFee() {
        try {
            service.pay(regNo.getText(), amount.getText());
            JOptionPane.showMessageDialog(this, "Payment successful.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Payment failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReceipt() {
        try { JOptionPane.showMessageDialog(this, service.receipt(regNo.getText())); }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Receipt error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
    }
}