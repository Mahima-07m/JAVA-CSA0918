class Ticket {
    private final int number;
    private final String registration;
    Ticket() { this(0, "UNKNOWN"); }
    Ticket(int number) { this(number, "UNKNOWN"); }
    Ticket(int number, String registration) {
        this.number = number;
        this.registration = registration;
    }
    Ticket(Ticket other) { this(other.number, other.registration); }
    void display() { System.out.println(number + " " + registration); }
}
public class ConstructorsDemo {
    public static void main(String[] args) {
        Ticket first = new Ticket();
        Ticket second = new Ticket(1001);
        Ticket third = new Ticket(1002, "TN01AB1111");
        Ticket copy = new Ticket(third);
        first.display(); second.display(); third.display(); copy.display();
    }
}