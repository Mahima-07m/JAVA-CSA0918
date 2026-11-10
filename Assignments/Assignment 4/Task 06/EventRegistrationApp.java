import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class RegistrationService {
    private final String url="jdbc:mysql://localhost:3306/campus_events";
    private final String user="root", password="password";
    void register(String studentId,String studentName,String event) throws Exception {
        if(studentId==null || !studentId.matches("[A-Za-z0-9]{5,20}")) throw new IllegalArgumentException("Invalid student ID.");
        if(studentName==null || studentName.isBlank()) throw new IllegalArgumentException("Enter student name.");
        if(event==null || event.isBlank()) throw new IllegalArgumentException("Select an event.");
        try(Connection c=DriverManager.getConnection(url,user,password);
            PreparedStatement check=c.prepareStatement("SELECT 1 FROM registrations WHERE student_id=? AND event_name=?")) {
            check.setString(1,studentId);check.setString(2,event);
            try(ResultSet rs=check.executeQuery()){if(rs.next())throw new IllegalArgumentException("Student is already registered for this event.");}
            try(PreparedStatement insert=c.prepareStatement("INSERT INTO registrations(student_id,student_name,event_name) VALUES(?,?,?)")){
                insert.setString(1,studentId);insert.setString(2,studentName.trim());insert.setString(3,event);insert.executeUpdate();
            }
        }
    }
    DefaultTableModel load(String event,int limit,int offset) throws SQLException {
        DefaultTableModel model=new DefaultTableModel(new String[]{"Student ID","Student Name","Event"},0);
        String sql="SELECT student_id,student_name,event_name FROM registrations WHERE (?='All' OR event_name=?) ORDER BY student_id LIMIT ? OFFSET ?";
        try(Connection c=DriverManager.getConnection(url,user,password);PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,event);ps.setString(2,event);ps.setInt(3,limit);ps.setInt(4,offset);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())model.addRow(new Object[]{rs.getString(1),rs.getString(2),rs.getString(3)});}
        }
        return model;
    }
}

public class EventRegistrationApp extends JFrame {
    private final JTextField studentId=new JTextField(12), studentName=new JTextField(12);
    private final JComboBox<String> event=new JComboBox<>(new String[]{"Dance","Music","Drama","Sports","Coding"});
    private final JComboBox<String> filter=new JComboBox<>(new String[]{"All","Dance","Music","Drama","Sports","Coding"});
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"Student ID","Student Name","Event"},0);
    private final JTable table=new JTable(model);
    private final RegistrationService service=new RegistrationService();
    private final JLabel status=new JLabel("Ready");
    private int page=0; private static final int PAGE_SIZE=50;

    public EventRegistrationApp(){
        super("Campus Event Registration");
        JPanel form=new JPanel(new GridLayout(3,2,6,6));
        form.add(new JLabel("Student ID"));form.add(studentId);
        form.add(new JLabel("Student name"));form.add(studentName);
        form.add(new JLabel("Event"));form.add(event);
        JButton register=new JButton("Register"), refresh=new JButton("Refresh");
        JPanel controls=new JPanel();controls.add(register);controls.add(new JLabel("Filter"));controls.add(filter);controls.add(refresh);
        JMenuBar bar=new JMenuBar();JMenu file=new JMenu("File");JMenuItem exit=new JMenuItem("Exit");exit.addActionListener(e->dispose());file.add(exit);bar.add(file);setJMenuBar(bar);
        add(form,BorderLayout.NORTH);add(new JScrollPane(table),BorderLayout.CENTER);add(controls,BorderLayout.SOUTH);add(status,BorderLayout.PAGE_END);
        register.addActionListener(e->registerStudent());refresh.addActionListener(e->{page=0;loadPage();});
        filter.addActionListener(e->{page=0;loadPage();});
        setDefaultCloseOperation(EXIT_ON_CLOSE);setSize(760,480);setLocationRelativeTo(null);loadPage();
    }

    private void registerStudent(){
        String id=studentId.getText(),name=studentName.getText(),chosen=(String)event.getSelectedItem();
        status.setText("Registering...");
        new SwingWorker<Void,Void>(){
            protected Void doInBackground() throws Exception {service.register(id,name,chosen);return null;}
            protected void done(){try{get();status.setText("Registration successful.");loadPage();}
                catch(Exception ex){status.setText("Registration failed: "+ex.getCause().getMessage());}}
        }.execute();
    }
    private void loadPage(){
        String selected=(String)filter.getSelectedItem();status.setText("Loading...");
        new SwingWorker<DefaultTableModel,Void>(){
            protected DefaultTableModel doInBackground() throws Exception{return service.load(selected,PAGE_SIZE,page*PAGE_SIZE);}
            protected void done(){try{DefaultTableModel data=get();model.setRowCount(0);for(int i=0;i<data.getRowCount();i++)model.addRow(new Object[]{data.getValueAt(i,0),data.getValueAt(i,1),data.getValueAt(i,2)});status.setText("Loaded "+data.getRowCount()+" registrations.");}
                catch(Exception ex){status.setText("Load failed: "+ex.getMessage());}}
        }.execute();
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new EventRegistrationApp().setVisible(true));}
}