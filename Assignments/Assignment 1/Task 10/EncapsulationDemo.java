class Vehicle {
    private String registration;
    private int durationMinutes;
    Vehicle(String registration) { setRegistration(registration); }
    public String getRegistration() { return registration; }
    public void setRegistration(String registration) {
        if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Invalid registration");
        this.registration = registration.trim().toUpperCase();
    }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        this.durationMinutes = durationMinutes;
    }
}
public class EncapsulationDemo {
    public static void main(String[] args) {
        Vehicle v = new Vehicle("tn01ab1111");
        v.setDurationMinutes(90);
        System.out.println(v.getRegistration());
        System.out.println(v.getDurationMinutes());
    }
}