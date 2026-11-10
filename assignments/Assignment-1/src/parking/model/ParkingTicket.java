package parking.model;
import java.time.LocalDateTime;
public final class ParkingTicket {
    private static long nextTicketNumber = 1000;
    private final long ticketNumber;
    private final Vehicle vehicle;
    private final int floor, slot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;
    public ParkingTicket(Vehicle vehicle, int floor, int slot) { this(vehicle, floor, slot, LocalDateTime.now()); }
    public ParkingTicket(Vehicle vehicle, int floor, int slot, LocalDateTime entryTime) {
        if (vehicle == null || entryTime == null) throw new IllegalArgumentException("Vehicle and entry time are required");
        if (floor < 0 || slot < 0) throw new IllegalArgumentException("Invalid parking position");
        this.ticketNumber = nextTicketNumber++;
        this.vehicle = vehicle; this.floor = floor; this.slot = slot; this.entryTime = entryTime;
    }
    public long getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getFee() { return fee; }
    public void close(LocalDateTime time, double amount) {
        if (time == null || time.isBefore(entryTime)) throw new IllegalArgumentException("Invalid exit time");
        if (exitTime != null) throw new IllegalStateException("Ticket is already closed");
        if (amount < 0) throw new IllegalArgumentException("Fee cannot be negative");
        exitTime = time; fee = amount;
    }
}