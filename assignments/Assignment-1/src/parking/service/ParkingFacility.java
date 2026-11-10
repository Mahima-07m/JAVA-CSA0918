package parking.service;
import parking.model.*;
import java.time.*;
import java.util.*;
public class ParkingFacility {
    private final Vehicle[][] slots;
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();
    private final Map<String, VisitorPass> visitorPasses = new HashMap<>();
    private double totalCollection;
    public ParkingFacility(int[] slotsPerFloor) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0) throw new IllegalArgumentException("At least one floor is required");
        slots = new Vehicle[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] < 0) throw new IllegalArgumentException("Slot count cannot be negative");
            slots[f] = new Vehicle[slotsPerFloor[f]];
        }
    }
    public synchronized int[] allocate(Vehicle vehicle) {
        if (vehicle == null) throw new IllegalArgumentException("Vehicle is required");
        if (activeTickets.containsKey(vehicle.getRegNo())) throw new IllegalArgumentException("Vehicle is already parked");
        for (int f = 0; f < slots.length; f++) for (int s = 0; s < slots[f].length; s++) {
            if (slots[f][s] != null) continue;
            slots[f][s] = vehicle;
            return new int[]{f, s};
        }
        return null;
    }
    public synchronized ParkingTicket admit(Vehicle vehicle, LocalDateTime entry) {
        if (entry == null) throw new IllegalArgumentException("Entry time is required");
        int[] position = allocate(vehicle);
        if (position == null) return null;
        ParkingTicket ticket = new ParkingTicket(vehicle, position[0], position[1], entry);
        activeTickets.put(vehicle.getRegNo(), ticket);
        return ticket;
    }
    public synchronized boolean release(int floor, int slot) {
        if (!valid(floor, slot) || slots[floor][slot] == null) return false;
        Vehicle vehicle = slots[floor][slot]; slots[floor][slot] = null;
        activeTickets.remove(vehicle.getRegNo()); return true;
    }
    public synchronized double exit(String regNo, LocalDateTime exit) {
        ParkingTicket ticket = activeTickets.get(normalize(regNo));
        if (ticket == null) throw new IllegalArgumentException("No active ticket for registration number");
        if (exit == null || exit.isBefore(ticket.getEntryTime())) throw new IllegalArgumentException("Invalid exit time");
        long minutes = Duration.between(ticket.getEntryTime(), exit).toMinutes();
        if (minutes > Integer.MAX_VALUE) throw new IllegalArgumentException("Duration is too large");
        double fee = ticket.getVehicle() instanceof DeliveryVan van
            ? van.calculateFee(ticket.getEntryTime(), exit)
            : ticket.getVehicle().calculateFee((int) minutes);
        ticket.close(exit, fee);
        slots[ticket.getFloor()][ticket.getSlot()] = null;
        activeTickets.remove(ticket.getVehicle().getRegNo());
        totalCollection += fee; return fee;
    }
    public synchronized void issueVisitorPass(String id, VisitorPass pass) {
        if (id == null || id.isBlank() || pass == null) throw new IllegalArgumentException("Visitor ID and pass are required");
        visitorPasses.put(id, pass);
    }
    public synchronized boolean hasValidVisitorPass(String id, LocalDate date) {
        VisitorPass pass = visitorPasses.get(id); return pass != null && pass.isValidOn(date);
    }
    public synchronized int getTotalOccupancy() {
        int count = 0; for (Vehicle[] floor : slots) for (Vehicle v : floor) if (v != null) count++;
        return count;
    }
    public synchronized double getTotalCollection() { return totalCollection; }
    public int getFloorCount() { return slots.length; }
    public int getSlotCount(int floor) {
        if (floor < 0 || floor >= slots.length) throw new IndexOutOfBoundsException("Invalid floor");
        return slots[floor].length;
    }
    public synchronized ParkingTicket findTicket(String regNo) { return activeTickets.get(normalize(regNo)); }
    private boolean valid(int f, int s) { return f >= 0 && f < slots.length && s >= 0 && s < slots[f].length; }
    private static String normalize(String value) { return value == null ? "" : value.trim().toUpperCase(); }
}