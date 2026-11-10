import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class Wallet {
    private final int id;
    private int balance;
    Wallet(int id, int balance) { this.id = id; this.balance = balance; }
    int getId() { return id; }
    synchronized int getBalance() { return balance; }
    synchronized void debit(int amount) { balance -= amount; }
    synchronized void credit(int amount) { balance += amount; }
}
class WalletTransfer {
    static void transfer(Wallet from, Wallet to, int amount) {
        Wallet first = from.getId() < to.getId() ? from : to;
        Wallet second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (from.getBalance() >= amount) {
                    from.debit(amount); to.credit(amount);
                }
            }
        }
    }
}
public class TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0, in = 0, out = 0;

    public synchronized void put(int token) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (count == 0) wait();
        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        notifyAll();
        return token;
    }

    public static void main(String[] args) throws InterruptedException {
        TokenCounter counter = new TokenCounter();
        int[] collected = new int[2];
        Thread cook = new Thread(() -> {
            try { for (int t = 1; t <= 20; t++) counter.put(t); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread[] students = new Thread[2];
        for (int s = 0; s < 2; s++) {
            final int id = s;
            students[s] = new Thread(() -> {
                try { for (int k = 0; k < 10; k++) collected[id] += counter.take(); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        cook.start(); for (Thread t : students) t.start();
        cook.join(); for (Thread t : students) t.join();
        System.out.println("Sum of tokens collected: " + (collected[0] + collected[1]) + " (expected 210)");

        Wallet a = new Wallet(1, 1000), b = new Wallet(2, 1000);
        Thread t1 = new Thread(() -> WalletTransfer.transfer(a, b, 100));
        Thread t2 = new Thread(() -> WalletTransfer.transfer(b, a, 50));
        t1.start(); t2.start(); t1.join(); t2.join();
        System.out.println("Wallet total: " + (a.getBalance() + b.getBalance()));
    }
}