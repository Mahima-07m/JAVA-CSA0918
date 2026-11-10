import java.util.concurrent.atomic.AtomicInteger;

class ThreadStation extends Thread {
    private final int station;
    private final int scripts;
    private final AtomicInteger total;
    ThreadStation(int station, int scripts, AtomicInteger total) {
        super("Station-" + station);
        this.station = station; this.scripts = scripts; this.total = total;
    }
    @Override public void run() {
        try {
            Thread.sleep(100L * station);
            total.addAndGet(scripts);
            System.out.println(getName() + " scanned " + scripts);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
class RunnableStation implements Runnable {
    private final int station;
    RunnableStation(int station) { this.station = station; }
    @Override public void run() {
        System.out.println("Runnable station " + station + " scanning");
    }
}
public class ScannerStations {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger total = new AtomicInteger();
        Thread[] stations = new Thread[4];
        for (int i = 0; i < 4; i++) {
            stations[i] = new ThreadStation(i + 1, 25, total);
            stations[i].start();
        }
        for (Thread station : stations) station.join();
        System.out.println("Total scripts scanned: " + total.get());

        Thread demo = new Thread(new RunnableStation(5));
        System.out.println("Alive before start: " + demo.isAlive());
        demo.start();
        demo.join();
        System.out.println("Alive after join: " + demo.isAlive());
    }
}