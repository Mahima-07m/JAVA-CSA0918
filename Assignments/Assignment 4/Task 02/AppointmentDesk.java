import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class AppointmentDesk extends JFrame {
    private final JTextField patientId = new JTextField(12);
    private final JComboBox<String> department = new JComboBox<>(new String[]{"Cardiology","Dermatology","Orthopaedics"});
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JComboBox<String> slot = new JComboBox<>(new String[]{"09:00","10:00","11:00","14:00","15:00"});
    private final JLabel status = new JLabel("Enter details to book.");
    private final Map<String,List<String>> doctors = new LinkedHashMap<>();
    private boolean bookingInProgress;

    public AppointmentDesk() {
        super("Hospital Appointment Desk");
        doctors.put("Cardiology", List.of("Dr. Kumar","Dr. Meera"));
        doctors.put("Dermatology", List.of("Dr. Priya","Dr. Arjun"));
        doctors.put("Orthopaedics", List.of("Dr. Ravi","Dr. Nisha"));
        setLayout(new BorderLayout(8,8));
        JPanel form = new JPanel(new GridLayout(4,2,8,8));
        form.add(new JLabel("Patient ID")); form.add(patientId);
        form.add(new JLabel("Department")); form.add(department);
        form.add(new JLabel("Doctor")); form.add(doctor);
        form.add(new JLabel("Time slot")); form.add(slot);
        JButton book = new JButton("Book appointment");
        add(form,BorderLayout.CENTER); add(book,BorderLayout.SOUTH); add(status,BorderLayout.NORTH);
        department.addActionListener(e -> refreshDoctors());
        book.addActionListener(e -> bookAppointment());
        refreshDoctors();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (bookingInProgress && JOptionPane.showConfirmDialog(AppointmentDesk.this,
                        "A booking is in progress. Close anyway?", "Confirm",
                        JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
                dispose();
            }
        });
        pack(); setLocationRelativeTo(null);
    }

    private void refreshDoctors() {
        doctor.removeAllItems();
        List<String> list = doctors.get((String) department.getSelectedItem());
        if (list != null) list.forEach(doctor::addItem);
    }

    private void bookAppointment() {
        String id = patientId.getText().trim();
        if (!id.matches("[A-Za-z0-9]{4,15}")) {
            status.setText("Error: enter a valid patient ID."); return;
        }
        if (doctor.getSelectedItem() == null || slot.getSelectedItem() == null) {
            status.setText("Error: select a doctor and time slot."); return;
        }
        bookingInProgress = true;
        status.setText("Appointment confirmed for " + id + " with " + doctor.getSelectedItem()
                + " at " + slot.getSelectedItem() + ".");
        bookingInProgress = false;
        JOptionPane.showMessageDialog(this,status.getText());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
    }
}