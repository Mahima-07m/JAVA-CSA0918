import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ConverterFrameListener extends JFrame implements ActionListener {
    private final JTextField amount = new JTextField(10);
    private final JLabel result = new JLabel("Result");
    private final JComboBox<String> currency = new JComboBox<>(new String[]{"USD","EUR"});
    public ConverterFrameListener() {
        super("Converter - Frame Listener");
        setLayout(new FlowLayout());
        JButton button = new JButton("Convert");
        add(amount); add(currency); add(button); add(result);
        button.addActionListener(this); amount.addActionListener(this);
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});
        setDefaultCloseOperation(EXIT_ON_CLOSE); pack();
    }
    @Override public void actionPerformed(ActionEvent e) {
        try { double n=Double.parseDouble(amount.getText()); String c=(String)currency.getSelectedItem();
            String v=String.format("%.2f %s",n*(c.equals("USD")?.012:.011),c); result.setText(v); JOptionPane.showMessageDialog(this,v);
        } catch(NumberFormatException ex){result.setText("Invalid amount");}
    }
    public static void main(String[] a){SwingUtilities.invokeLater(()->new ConverterFrameListener().setVisible(true));}
}