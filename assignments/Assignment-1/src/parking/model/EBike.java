package parking.model;
public class EBike extends TwoWheeler implements Chargeable {
    private static final double FLAT_PARKING_FEE = 25.0;
    private static final double CHARGING_RATE_PER_KWH = 8.0;
    public EBike(String regNo) { super(regNo); }
    @Override public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Duration cannot be negative");
        return minutes == 0 ? 0.0 : FLAT_PARKING_FEE;
    }
    @Override public double chargingFee(int kWh) {
        if (!Chargeable.isValidEnergy(kWh)) throw new IllegalArgumentException("Energy cannot be negative");
        return kWh * CHARGING_RATE_PER_KWH;
    }
}