package ppd.lab3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marius Adam
 */
public class Main {
    private static final int TEST_REPETITIONS = 10;
    private static final boolean LOG_ENABLED = false;

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        List<Function<Void, SortedLinkedList<Integer>>> instances = Stream.of(
                (Function<Void, SortedLinkedList<Integer>>) aVoid -> new CoarseGrainedSyncedSortedLinkedList<>(),
                aVoid -> new FineGrainedSyncSortedLinkedList<>()
        ).collect(Collectors.toList());

        List<TestCase<Integer>> tests = Stream.of(
                new TestCaseOneInt(),
                new TestCaseTwoInt()
        ).collect(Collectors.toList());

        Map<String, Double> benchmarks = new HashMap<>();

        tests.forEach(testCase -> instances.forEach(listFactory -> {
            SortedLinkedList<Integer> instance = listFactory.apply(null);
            log("main", testCase.getClass().getSimpleName(), instance.getClass().getSimpleName());

            long sum = 0;
            for (int i = 0; i < TEST_REPETITIONS; ++i) {
                long startTime = System.currentTimeMillis();
                testCase.test(listFactory.apply(null));
                sum += System.currentTimeMillis() - startTime;
            }

            benchmarks.put(
                    String.format("%s::%s", testCase.getClass().getSimpleName(), instance.getClass().getSimpleName()),
                    (double) sum / TEST_REPETITIONS
            );
        }));

        Thread.sleep(100);
        benchmarks.forEach((s, avg) -> log("main", s, avg.toString()));
    }

    public static void log(String threadName, String method, String argsAsString) {
        if (!LOG_ENABLED && !threadName.equals("main")) {
            return;
        }

        Date now = new Date();

        try (FileWriter fw = new FileWriter("List.log", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String line = String.format(
                    "%s: %s -> %s(%s)",
                    sdf.format(now),
                    threadName,
                    method,
                    argsAsString
            );
            out.println(line);
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
