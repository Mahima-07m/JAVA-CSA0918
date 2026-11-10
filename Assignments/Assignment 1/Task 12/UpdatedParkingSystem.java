import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
interface Chargeable { double chargingFee(int units); }
interface Pass { boolean isValid(LocalDate date); }
abstract class Vehicle {
    private final String registration;
    Vehicle(String registration) {
        if (registration == null || registration.isBlank()) throw new IllegalArgumentException("Invalid registration");
        this.registration=registration.trim().toUpperCase();
    }
    String registration() { return registration; }
    abstract double parkingFee(LocalDateTime in, LocalDateTime out);
}
class Car extends Vehicle {
    Car(String r) { super(r); }
    double parkingFee(LocalDateTime in, LocalDateTime out) {
        long m=Duration.between(in,out).toMinutes(); return (m/60+(m%60==0?0:1))*30.0;
    }
}
class EBike extends Vehicle implements Chargeable {
    EBike(String r) { super(r); }
    double parkingFee(LocalDateTime in, LocalDateTime out) { return 25.0; }
    public double chargingFee(int units) { if(units<0)throw new IllegalArgumentException(); return units*8.0; }
}
class DeliveryVan extends Vehicle {
    DeliveryVan(String r) { super(r); }
    double parkingFee(LocalDateTime in, LocalDateTime out) {
        double total=0; LocalDateTime t=in;
        while(t.isBefore(out)) {
            LocalTime x=t.toLocalTime();
            total += (!x.isBefore(LocalTime.of(9,0)) && x.isBefore(LocalTime.of(17,0)) ? 60.0 : 30.0)/60.0;
            t=t.plusMinutes(1);
        }
        return Math.ceil(total);
    }
}
class OneDayPass implements Pass {
    private final LocalDate date;
    OneDayPass(LocalDate date) { this.date=date; }
    public boolean isValid(LocalDate today) { return date.equals(today); }
}
class Ticket {
    private static int next=2000;
    final int number=next++;
    final Vehicle vehicle;
    final LocalDateTime entry;
    Ticket(Vehicle v,LocalDateTime t){vehicle=v;entry=t;}
}
class ParkingFacility {
    private final String[][] slots;
    private final Map<String,Ticket> active=new HashMap<>();
    ParkingFacility(int[] capacities) {
        slots=new String[capacities.length][];
        for(int i=0;i<capacities.length;i++) slots[i]=new String[capacities[i]];
    }
    Ticket enter(Vehicle v,LocalDateTime time) {
        if(active.containsKey(v.registration())) throw new IllegalArgumentException("Already parked");
        for(int f=0;f<slots.length;f++) for(int s=0;s<slots[f].length;s++)
            if(slots[f][s]==null) {
                slots[f][s]=v.registration();
                Ticket t=new Ticket(v,time); active.put(v.registration(),t); return t;
            }
        return null;
    }
    double exit(String reg,LocalDateTime time,Pass pass) {
        Ticket t=active.remove(reg.toUpperCase());
        if(t==null) throw new IllegalArgumentException("Ticket not found");
        for(int f=0;f<slots.length;f++) for(int s=0;s<slots[f].length;s++)
            if(reg.equalsIgnoreCase(slots[f][s])) slots[f][s]=null;
        if(pass!=null && pass.isValid(time.toLocalDate())) return 0.0;
        return t.vehicle.parkingFee(t.entry,time);
    }
}
public class UpdatedParkingSystem {
    public static void main(String[] args) {
        ParkingFacility p=new ParkingFacility(new int[]{20,30,25,40,35});
        LocalDateTime in=LocalDateTime.of(2026,11,10,8,30);
        p.enter(new EBike("TN01AA1111"),in);
        p.enter(new DeliveryVan("TN01BB2222"),in);
        System.out.println("E-bike fee: Rs "+p.exit("TN01AA1111",in.plusHours(2),null));
        System.out.println("Van fee: Rs "+p.exit("TN01BB2222",in.plusHours(2),null));
        OneDayPass pass=new OneDayPass(LocalDate.of(2026,11,10));
        System.out.println("One-day pass valid: "+pass.isValid(LocalDate.of(2026,11,10)));
    }
}