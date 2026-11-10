import java.util.*;

public class PharmacyInventory {
    private static volatile int sink;
    private static long lookupTime(Map<String,Integer> map, int rounds) {
        long start=System.nanoTime();
        for(int i=0;i<rounds;i++) sink += map.get("MED" + (i % map.size()));
        return System.nanoTime()-start;
    }
    private static void testSize(int count) {
        Map<String,Integer> hash = new HashMap<>();
        Map<String,Integer> tree = new TreeMap<>();
        Map<String,Integer> linked = new LinkedHashMap<>();
        for(int i=0;i<count;i++) {
            String key=String.format("MED%06d",i);
            hash.put(key,i); tree.put(key,i); linked.put(key,i);
        }
        int rounds=100000;
        System.out.println("\\nMedicines: "+count);
        System.out.println("HashMap 100k lookups ns: "+lookupTime(hash,rounds));
        System.out.println("TreeMap 100k lookups ns: "+lookupTime(tree,rounds));
        System.out.println("LinkedHashMap 100k lookups ns: "+lookupTime(linked,rounds));
        System.out.println("Code-order first keys: "+new TreeMap<>(hash).keySet().stream().limit(5).toList());
        System.out.println("Insertion-order first keys: "+linked.keySet().stream().limit(5).toList());
    }
    public static void main(String[] args) {
        testSize(100); testSize(1000); testSize(10000);
        System.out.println("\\nChecksum: "+sink);
    }
}