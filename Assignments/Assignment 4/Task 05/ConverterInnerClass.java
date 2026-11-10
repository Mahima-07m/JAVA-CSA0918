import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ConverterInnerClass extends JFrame {
    private final JTextField amount=new JTextField(10); private final JLabel result=new JLabel("Result");
    private final JComboBox<String> currency=new JComboBox<>(new String[]{"USD","EUR"});
    public ConverterInnerClass(){
        super("Converter - Inner Class"); setLayout(new FlowLayout()); JButton b=new JButton("Convert");
        add(amount);add(currency);add(b);add(result); b.addActionListener(new ConvertHandler());amount.addActionListener(new ConvertHandler());
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});
        setDefaultCloseOperation(EXIT_ON_CLOSE);pack();
    }
    private class ConvertHandler implements ActionListener {
        public void actionPerformed(ActionEvent e){try{double n=Double.parseDouble(amount.getText());String c=(String)currency.getSelectedItem();
            String v=String.format("%.2f %s",n*(c.equals("USD")?.012:.011),c);result.setText(v);JOptionPane.showMessageDialog(ConverterInnerClass.this,v);
        }catch(NumberFormatException ex){result.setText("Invalid amount");}}
    }
    public static void main(String[] a){SwingUtilities.invokeLater(()->new ConverterInnerClass().setVisible(true));}
}