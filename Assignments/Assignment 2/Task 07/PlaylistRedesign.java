import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlaylistRedesign {
    private static volatile int sink;
    private static long measureArrayList(int count) {
        List<Integer> queue=new ArrayList<>();
        for(int i=0;i<count;i++) queue.add(i);
        long start=System.nanoTime();
        while(!queue.isEmpty()) sink += queue.remove(0);
        return System.nanoTime()-start;
    }
    private static long measureArrayDeque(int count) {
        Deque<Integer> queue=new ArrayDeque<>();
        for(int i=0;i<count;i++) queue.addLast(i);
        long start=System.nanoTime();
        while(!queue.isEmpty()) sink += queue.removeFirst();
        return System.nanoTime()-start;
    }
    private static void compare(int count) {
        System.out.println("\\nQueue size: "+count);
        System.out.println("Original ArrayList remove-first ns: "+measureArrayList(count));
        System.out.println("Redesigned ArrayDeque remove-first ns: "+measureArrayDeque(count));
    }
    public static void main(String[] args) {
        compare(1000); compare(10000); compare(50000);
        Deque<String> upNext=new ArrayDeque<>();
        upNext.addLast("Track A"); upNext.addLast("Track B");
        upNext.addFirst("Priority Track");
        System.out.println("\\nNext track: "+upNext.removeFirst());
        System.out.println("Remaining queue: "+upNext);
        System.out.println("Checksum: "+sink);
    }
}