package parking.model;
public interface Chargeable {
    double chargingFee(int kWh);
    default String chargingStatus() { return "Charging available"; }
    static boolean isValidEnergy(int kWh) { return kWh >= 0; }
}