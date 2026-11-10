class Vehicle {
    String registrationNumber;
    String type;
    Vehicle(String registrationNumber, String type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }
}
class ParkingSlot {
    int floor, slotNumber;
    Vehicle vehicle;
    ParkingSlot(int floor, int slotNumber) {
        this.floor = floor; this.slotNumber = slotNumber;
    }
    boolean isFree() { return vehicle == null; }
}
class ParkingTicket {
    static int nextNumber = 1000;
    final int ticketNumber = nextNumber++;
    Vehicle vehicle;
    long entryTime;
    ParkingTicket(Vehicle vehicle, long entryTime) {
        this.vehicle = vehicle; this.entryTime = entryTime;
    }
}
public class ProblemObjects {
    public static void main(String[] args) {
        Vehicle v = new Vehicle("TN01AB1111", "Car");
        ParkingSlot s = new ParkingSlot(0, 1);
        s.vehicle = v;
        ParkingTicket t = new ParkingTicket(v, System.currentTimeMillis());
        System.out.println(v.registrationNumber + " " + v.type);
        System.out.println("Slot free: " + s.isFree());
        System.out.println("Ticket: " + t.ticketNumber);
    }
}