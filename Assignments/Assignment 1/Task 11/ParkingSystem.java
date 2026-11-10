import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
abstract class Vehicle {
    private final String registration;
    Vehicle(String registration) {
        if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Invalid registration");
        this.registration = registration.toUpperCase();
    }
    String getRegistration() { return registration; }
    abstract double hourlyRate();
    double fee(long minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Invalid duration");
        long hours = minutes / 60 + (minutes % 60 == 0 ? 0 : 1);
        return hours * hourlyRate();
    }
}
class Bike extends Vehicle {
    Bike(String r) { super(r); }
    double hourlyRate() { return 10; }
}
class Car extends Vehicle {
    Car(String r) { super(r); }
    double hourlyRate() { return 30; }
}
class Ticket {
    private static int next = 1000;
    final int number = next++;
    final Vehicle vehicle;
    final LocalDateTime entry;
    Ticket(Vehicle vehicle, LocalDateTime entry) { this.vehicle = vehicle; this.entry = entry; }
}
class ParkingSystem {
    private final String[][] slots;
    private final Map<String, Ticket> active = new HashMap<>();
    ParkingSystem(int[] capacities) {
        slots = new String[capacities.length][];
        for (int i=0;i<capacities.length;i++) slots[i] = new String[capacities[i]];
    }
    Ticket enter(Vehicle vehicle, LocalDateTime time) {
        if (active.containsKey(vehicle.getRegistration())) throw new IllegalArgumentException("Duplicate vehicle");
        for (int f=0;f<slots.length;f++) for (int s=0;s<slots[f].length;s++) {
            if (slots[f][s] == null) {
                slots[f][s] = vehicle.getRegistration();
                Ticket t = new Ticket(vehicle,time); active.put(vehicle.getRegistration(),t); return t;
            }
        }
        return null;
    }
    double exit(String registration, LocalDateTime time) {
        Ticket t = active.remove(registration.toUpperCase());
        if (t == null) throw new IllegalArgumentException("Unknown vehicle");
        long minutes = Duration.between(t.entry,time).toMinutes();
        double amount = t.vehicle.fee(minutes);
        for (int f=0;f<slots.length;f++) for (int s=0;s<slots[f].length;s++)
            if (registration.equalsIgnoreCase(slots[f][s])) slots[f][s]=null;
        return amount;
    }
}
public class ParkingSystem {
    public static void main(String[] args) {
        ParkingSystem p = new ParkingSystem(new int[]{1,2,1});
        LocalDateTime start = LocalDateTime.of(2026,11,10,8,0);
        p.enter(new Car("TN01AA1111"),start);
        p.enter(new Bike("TN01BB2222"),start);
        System.out.println("Car fee: Rs " + p.exit("TN01AA1111",start.plusMinutes(60)));
        System.out.println("Bike fee: Rs " + p.exit("TN01BB2222",start.plusMinutes(30)));
        System.out.println("Total: Rs 40.0");
    }
}