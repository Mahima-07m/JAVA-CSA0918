interface Chargeable {
    double chargingFee(int units);
    default String status() { return "Charging supported"; }
    static boolean validUnits(int units) { return units >= 0; }
}
abstract class Vehicle {
    private final String registration;
    Vehicle(String registration) { this.registration = registration; }
    String getRegistration() { return registration; }
    abstract double parkingFee(int hours);
}
class ElectricCar extends Vehicle implements Chargeable {
    ElectricCar(String registration) { super(registration); }
    @Override double parkingFee(int hours) { return hours * 30.0; }
    @Override public double chargingFee(int units) {
        if (!Chargeable.validUnits(units)) throw new IllegalArgumentException();
        return units * 12.0;
    }
}
public class AbstractionDemo {
    public static void main(String[] args) {
        ElectricCar car = new ElectricCar("TN01AB1111");
        System.out.println("Parking: Rs " + car.parkingFee(2));
        System.out.println("Charging: Rs " + car.chargingFee(5));
        System.out.println(car.status());
    }
}