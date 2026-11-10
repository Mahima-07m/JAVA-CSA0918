import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ResultProcessor {
    static class Result {
        final String file;
        final boolean success;
        final int score;
        final String message;
        Result(String file, boolean success, int score, String message) {
            this.file = file; this.success = success; this.score = score; this.message = message;
        }
        @Override public String toString() {
            return file + ": " + (success ? "score=" + score : "FAILED - " + message);
        }
    }

    static Result process(Path file) {
        try {
            String value = Files.readString(file).trim();
            int score = Integer.parseInt(value);
            if (score < 0 || score > 100) throw new IllegalArgumentException("Score outside 0-100");
            return new Result(file.getFileName().toString(), true, score, "OK");
        } catch (IOException | NumberFormatException | IllegalArgumentException e) {
            return new Result(file.getFileName().toString(), false, 0, e.getMessage());
        }
    }

    static void run(List<Path> files, int poolSize) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        ConcurrentMap<String, Result> results = new ConcurrentHashMap<>();
        List<Future<Result>> futures = new ArrayList<>();
        long start = System.nanoTime();
        try {
            for (Path file : files) futures.add(pool.submit(() -> process(file)));
            for (Future<Result> future : futures) {
                Result result = future.get();
                results.put(result.file, result);
            }
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) pool.shutdownNow();
        }
        long elapsed = System.nanoTime() - start;
        long successes = results.values().stream().filter(r -> r.success).count();
        long failures = results.size() - successes;
        System.out.println("Threads: " + poolSize + ", success: " + successes
                + ", failures: " + failures + ", time(ms): " + elapsed / 1_000_000.0);
        results.values().stream().sorted(Comparator.comparing(r -> r.file)).forEach(System.out::println);
    }

    public static void main(String[] args) throws Exception {
        Path dir = Path.of("score-files");
        Files.createDirectories(dir);
        List<Path> files = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Path file = dir.resolve("student" + i + ".txt");
            Files.writeString(file, i == 4 ? "corrupt-score" : String.valueOf(50 + i));
            files.add(file);
        }
        files.add(dir.resolve("missing-student.txt"));
        for (int threads : new int[]{1, 2, 4, 8}) run(files, threads);
    }
}