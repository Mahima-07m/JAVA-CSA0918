import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

public class Converter extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD","EUR"});
    private final JLabel result = new JLabel(" ");
    private static double rate(String currency) { return currency.equals("USD") ? 0.012 : 0.011; }

    public Converter() {
        super("Currency Converter");
        setLayout(new FlowLayout());
        JButton convertButton = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target); add(convertButton); add(result);
        ActionListener convertAction = e -> convert();
        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack(); setLocationRelativeTo(null);
    }

    private void convert() {
        try {
            double inr = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(inr) || inr < 0) throw new NumberFormatException();
            String currency = (String) target.getSelectedItem();
            String value = String.format("%.2f %s", inr * rate(currency), currency);
            result.setText(value);
            JOptionPane.showMessageDialog(this, value, "Conversion result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Converter().setVisible(true));
    }
}