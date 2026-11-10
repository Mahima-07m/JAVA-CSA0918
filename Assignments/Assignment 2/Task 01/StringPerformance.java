import java.util.ArrayList;
import java.util.List;

public class StringPerformance {
    private static volatile int sink;
    private static String withPlus(int fields) {
        String ticket = "";
        for (int i = 0; i < fields; i++) ticket += "FIELD" + i + ":VALUE" + i + "\\n";
        return ticket;
    }
    private static String withBuilder(int fields) {
        StringBuilder ticket = new StringBuilder();
        for (int i = 0; i < fields; i++) ticket.append("FIELD").append(i).append(":VALUE").append(i).append('\\n');
        return ticket.toString();
    }
    private static String withBuffer(int fields) {
        StringBuffer ticket = new StringBuffer();
        for (int i = 0; i < fields; i++) ticket.append("FIELD").append(i).append(":VALUE").append(i).append('\\n');
        return ticket.toString();
    }
    private static long measure(Runnable task, int repetitions) {
        for (int i = = 0; i < 3; i++) task.run();
        long start = System.nanoTime();
        for (int i = 0; i < repetitions; i++) task.run();
        return (System.nanoTime() - start) / repetitions;
    }
    private static void runWorkload(String name, int fields, int tickets) {
        System.out.println("\\n" + name + " (fields=" + fields + ", tickets=" + tickets + ")");
        System.out.println("String + avg ns/ticket: " + measure(() -> sink += withPlus(fields).length(), tickets));
        System.out.println("StringBuilder avg ns/ticket: " + measure(() -> sink += withBuilder(fields).length(), tickets));
        System.out.println("StringBuffer avg ns/ticket: " + measure(() -> sink += withBuffer(fields).length(), tickets));
    }
    public static void main(String[] args) {
        runWorkload("Short ticket", 5, 2000);
        runWorkload("Large ticket batch", 50, 1000);
        runWorkload("Very large ticket", 200, 300);
        System.out.println("\\nResult checksum: " + sink);
        StringBuilder sharedLog = new StringBuilder();
        sharedLog.append("Use StringBuilder for a ticket local to one thread.").append('\\n');
        StringBuffer sharedThreadSafeLog = new StringBuffer();
        sharedThreadSafeLog.append("Use StringBuffer when multiple threads append to the same buffer.").append('\\n');
        System.out.println(sharedLog.toString().trim());
        System.out.println(sharedThreadSafeLog.toString().trim());
    }
}