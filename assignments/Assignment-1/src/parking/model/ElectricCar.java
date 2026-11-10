package parking.model;
public class ElectricCar extends Car implements Chargeable {
    private static final double CHARGING_RATE_PER_KWH = 12.0;
    public ElectricCar(String regNo) { super(regNo); }
    @Override public double chargingFee(int kWh) {
        if (!Chargeable.isValidEnergy(kWh)) throw new IllegalArgumentException("Energy cannot be negative");
        return kWh * CHARGING_RATE_PER_KWH;
    }
}