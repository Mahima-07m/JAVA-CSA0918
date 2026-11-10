import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ConverterAnonymousClass extends JFrame {
    private final JTextField amount=new JTextField(10); private final JLabel result=new JLabel("Result");
    private final JComboBox<String> currency=new JComboBox<>(new String[]{"USD","EUR"});
    public ConverterAnonymousClass(){
        super("Converter - Anonymous Class");setLayout(new FlowLayout());JButton b=new JButton("Convert");
        add(amount);add(currency);add(b);add(result);
        ActionListener handler=new ActionListener(){public void actionPerformed(ActionEvent e){try{double n=Double.parseDouble(amount.getText());String c=(String)currency.getSelectedItem();
            String v=String.format("%.2f %s",n*(c.equals("USD")?.012:.011),c);result.setText(v);JOptionPane.showMessageDialog(ConverterAnonymousClass.this,v);
        }catch(NumberFormatException ex){result.setText("Invalid amount");}}};
        b.addActionListener(handler);amount.addActionListener(handler);
        addWindowListener(new WindowAdapter(){@Override public void windowClosing(WindowEvent e){dispose();}});
        setDefaultCloseOperation(EXIT_ON_CLOSE);pack();
    }
    public static void main(String[] a){SwingUtilities.invokeLater(()->new ConverterAnonymousClass().setVisible(true));}
}