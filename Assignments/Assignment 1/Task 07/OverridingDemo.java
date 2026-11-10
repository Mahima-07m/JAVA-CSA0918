import java.util.Objects;
class Vehicle {
    private final String registration;
    Vehicle(String registration) { this.registration = registration; }
    double calculateFee(int minutes) {
        return (minutes / 60 + (minutes % 60 == 0 ? 0 : 1)) * 20.0;
    }
    @Override public String toString() { return getClass().getSimpleName() + " " + registration; }
    @Override public boolean equals(Object obj) {
        return obj instanceof Vehicle v && registration.equals(v.registration);
    }
    @Override public int hashCode() { return Objects.hash(registration); }
}
class Car extends Vehicle {
    Car(String registration) { super(registration); }
    @Override double calculateFee(int minutes) {
        return (minutes / 60 + (minutes % 60 == 0 ? 0 : 1)) * 30.0;
    }
}
public class OverridingDemo {
    public static void main(String[] args) {
        Vehicle v = new Car("TN01AB1111");
        System.out.println(v);
        System.out.println("Fee: Rs " + v.calculateFee(75));
        System.out.println(v.equals(new Car("TN01AB1111")));
    }
}