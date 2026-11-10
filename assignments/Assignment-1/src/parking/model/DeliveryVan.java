package parking.model;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Duration;
public class DeliveryVan extends Vehicle {
    private static final double CAR_RATE = 30.0;
    private static final LocalTime PEAK_START = LocalTime.of(9, 0);
    private static final LocalTime PEAK_END = LocalTime.of(17, 0);
    public DeliveryVan(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return CAR_RATE; }
    @Override public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        int hours = minutes / 60 + (minutes % 60 == 0 ? 0 : 1);
        return hours * CAR_RATE;
    }
    public double calculateFee(LocalTime entry, LocalTime exit) {
        if (entry == null || exit == null) throw new IllegalArgumentException("Times are required");
        long minutes = Duration.between(entry, exit).toMinutes();
        if (minutes < 0) minutes += 24 * 60;
        double total = 0;
        LocalTime cursor = entry;
        for (long i = 0; i < minutes; i++) {
            boolean peak = !cursor.isBefore(PEAK_START) && cursor.isBefore(PEAK_END);
            total += (peak ? 2.0 : 1.0) * CAR_RATE / 60.0;
            cursor = cursor.plusMinutes(1);
        }
        return Math.ceil(total);
    }
    public double calculateFee(LocalDateTime entry, LocalDateTime exit) {
        if (entry == null || exit == null || exit.isBefore(entry)) throw new IllegalArgumentException("Invalid parking interval");
        long minutes = Duration.between(entry, exit).toMinutes();
        double total = 0;
        LocalDateTime cursor = entry;
        for (long i = 0; i < minutes; i++) {
            LocalTime time = cursor.toLocalTime();
            boolean peak = !time.isBefore(PEAK_START) && time.isBefore(PEAK_END);
            total += (peak ? 2.0 : 1.0) * CAR_RATE / 60.0;
            cursor = cursor.plusMinutes(1);
        }
        return Math.ceil(total);
    }
}