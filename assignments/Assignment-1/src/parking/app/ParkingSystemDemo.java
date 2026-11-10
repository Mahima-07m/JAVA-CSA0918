package parking.app;
import parking.model.*;
import parking.service.ParkingFacility;
import java.time.*;
public class ParkingSystemDemo {
    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(new int[]{2, 3, 2, 4, 3});
        LocalDateTime start = LocalDateTime.of(2026, 11, 10, 8, 0);
        ParkingTicket[] tickets = {
            facility.admit(new TwoWheeler("TN01AA1001"), start),
            facility.admit(new Car("TN01BB2002"), start),
            facility.admit(new ElectricCar("TN01CC3003"), start),
            facility.admit(new EBike("TN01DD4004"), start),
            facility.admit(new DeliveryVan("TN01EE5005"), start)
        };
        int count = 0; for (ParkingTicket t : tickets) if (t != null) count++;
        System.out.println("Allocated tickets: " + count);
        System.out.println("Occupied slots: " + facility.getTotalOccupancy());
        System.out.println("Two-wheeler fee: Rs " + facility.exit("TN01AA1001", start.plusMinutes(60)));
        System.out.println("Car fee: Rs " + facility.exit("TN01BB2002", start.plusMinutes(120)));
        System.out.println("Electric car fee: Rs " + facility.exit("TN01CC3003", start.plusMinutes(60)));
        System.out.println("E-bike flat fee: Rs " + facility.exit("TN01DD4004", start.plusMinutes(30)));
        System.out.println("Delivery van fee: Rs " + facility.exit("TN01EE5005", start.plusHours(2)));
        System.out.println("Total collection: Rs " + facility.getTotalCollection());
        facility.issueVisitorPass("VISITOR-01", new VisitorPass("Guest", LocalDate.of(2026, 11, 10), 50));
        System.out.println("Visitor pass valid: " + facility.hasValidVisitorPass("VISITOR-01", LocalDate.of(2026, 11, 10)));
    }
}