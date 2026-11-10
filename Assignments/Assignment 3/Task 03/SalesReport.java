import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class SalesReport {
    static int manualOpenResources;
    static int safeOpenResources;

    static void generateManual(List<Path> inputs, Path output) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(output);
            for (Path input : inputs) {
                BufferedReader reader = Files.newBufferedReader(input);
                manualOpenResources++;
                String line;
                while ((line = reader.readLine()) != null) writer.write(line + System.lineSeparator());
                reader.close();
                manualOpenResources--;
            }
        } finally {
            if (writer != null) writer.close();
        }
    }

    static void generateSafe(List<Path> inputs, Path output) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (Path input : inputs) {
                try (BufferedReader reader = Files.newBufferedReader(input)) {
                    String line;
                    while ((line = reader.readLine()) != null) writer.write(line + System.lineSeparator());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path dir = Path.of("sales-input");
        Files.createDirectories(dir);
        Path a = dir.resolve("store1.txt"), b = dir.resolve("store2.txt");
        Files.writeString(a, "Store 1: 12000\n");
        Files.writeString(b, "Store 2: 9500\n");
        List<Path> valid = List.of(a, b);
        generateManual(valid, Path.of("manual-report.txt"));
        generateSafe(valid, Path.of("safe-report.txt"));
        System.out.println("Manual report: " + Files.readString(Path.of("manual-report.txt")).replace("\n", " | "));
        System.out.println("Safe report: " + Files.readString(Path.of("safe-report.txt")).replace("\n", " | "));

        List<Path> failing = List.of(a, dir.resolve("missing-store.txt"), b);
        try {
            generateSafe(failing, Path.of("failure-report.txt"));
        } catch (IOException e) {
            System.out.println("Failed input: " + e.getMessage());
        }
        System.out.println("Try-with-resources closes opened readers and writer automatically.");
    }
}