import java.util.concurrent.atomic.AtomicInteger;

public class SeatBooking {
    private final boolean[] booked;
    private int bookingsMade;
    SeatBooking(int seats) { booked = new boolean[seats]; }

    // Atomic check-and-book operation.
    public synchronized boolean book(int seat) {
        if (seat < 0 || seat >= booked.length) return false;
        if (booked[seat]) return false;
        booked[seat] = true;
        bookingsMade++;
        return true;
    }

    public synchronized int getBookingsMade() { return bookingsMade; }

    public static void main(String[] args) throws InterruptedException {
        SeatBooking screen = new SeatBooking(10);
        AtomicInteger successfulAttempts = new AtomicInteger();
        Thread[] users = new Thread[100];
        for (int i = 0; i < users.length; i++) {
            final int seat = i % 10;
            users[i] = new Thread(() -> {
                if (screen.book(seat)) successfulAttempts.incrementAndGet();
            });
            users[i].start();
        }
        for (Thread t : users) t.join();
        System.out.println("Seats sold: " + screen.getBookingsMade());
        System.out.println("Successful bookings: " + successfulAttempts.get());
    }
}