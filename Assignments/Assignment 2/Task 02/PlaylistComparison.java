import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;

public class PlaylistComparison {
    private static volatile int sink;
    private static long time(String label, Runnable action) {
        long start = System.nanoTime(); action.run(); long elapsed = System.nanoTime() - start;
        sink += label.length(); return elapsed;
    }
    private static void benchmark(int size) {
        List<Integer> array = new ArrayList<>(), linked = new LinkedList<>();
        for (int i=0;i<size;i++) { array.add(i); linked.add(i); }
        int middle=size/2;
        System.out.println("\\nSize: " + size);
        System.out.println("ArrayList get middle ns: " + time("a", () -> sink += array.get(middle)));
        System.out.println("LinkedList get middle ns: " + time("l", () -> sink += linked.get(middle)));
        System.out.println("ArrayList add front ns: " + time("a", () -> array.add(0,-1)));
        System.out.println("LinkedList add front ns: " + time("l", () -> linked.add(0,-1)));
        System.out.println("ArrayList remove middle ns: " + time("a", () -> array.remove(array.size()/2)));
        System.out.println("LinkedList remove middle ns: " + time("l", () -> linked.remove(linked.size()/2)));
        System.out.println("ArrayList iteration ns: " + time("a", () -> { for(int x:array) sink += x; }));
        System.out.println("LinkedList iteration ns: " + time("l", () -> { for(int x:linked) sink += x; }));
    }
    public static void main(String[] args) {
        benchmark(1000); benchmark(10000); benchmark(50000);
        Deque<String> upNext = new ArrayDeque<>();
        upNext.addFirst("Urgent song"); upNext.addLast("Song B"); upNext.addLast("Song C");
        System.out.println("\\nUp Next: " + upNext);
        System.out.println("Playing: " + upNext.removeFirst());
        System.out.println("Remaining: " + upNext);
        System.out.println("Checksum: " + sink);
    }
}