class Vehicle {
    protected String registration;
    Vehicle(String registration) {
        this.registration = registration;
        System.out.println("Vehicle constructor");
    }
    double fee(int minutes) { return 0; }
}
class Car extends Vehicle {
    Car(String registration) {
        super(registration);
        System.out.println("Car constructor");
    }
    @Override double fee(int minutes) { return (minutes / 60 + (minutes % 60 == 0 ? 0 : 1)) * 30.0; }
}
class ElectricCar extends Car {
    ElectricCar(String registration) {
        super(registration);
        System.out.println("ElectricCar constructor");
    }
}
public class InheritanceDemo {
    public static void main(String[] args) {
        ElectricCar car = new ElectricCar("TN01AB1111");
        System.out.println(car.registration + " Fee: Rs " + car.fee(90));
    }
}