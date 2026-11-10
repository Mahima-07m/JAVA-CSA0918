class ParkingTicket {
    private static int nextNumber = 1000;
    private final int ticketNumber;
    ParkingTicket() { ticketNumber = nextNumber++; }
    int getTicketNumber() { return ticketNumber; }
    static int getNextNumber() { return nextNumber; }
}
public class StaticMembers {
    public static void main(String[] args) {
        ParkingTicket a = new ParkingTicket();
        ParkingTicket b = new ParkingTicket();
        System.out.println(a.getTicketNumber());
        System.out.println(b.getTicketNumber());
        System.out.println("Next ticket: " + ParkingTicket.getNextNumber());
    }
}