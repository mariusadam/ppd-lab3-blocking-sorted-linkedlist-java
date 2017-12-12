package ppd.lab3;

import ppd.lab3.list.CoarseGrainedSyncedSortedLinkedList;
import ppd.lab3.list.FineGrainedSyncSortedLinkedList;
import ppd.lab3.list.SortedLinkedList;
import ppd.lab3.testcase.IntTestCase;
import ppd.lab3.testcase.TestCase;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marius Adam
 */
public class Main {
    private static final int TEST_REPETITIONS = 1000;
    private static final boolean LOG_ENABLED = false;

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        List<Factory<SortedLinkedList<Integer>>> listFactories = Stream.of(
                (Factory<SortedLinkedList<Integer>>) CoarseGrainedSyncedSortedLinkedList::new
                , FineGrainedSyncSortedLinkedList::new
        ).collect(Collectors.toList());

        List<Factory<TestCase<Integer>>> testFactories = Stream.of(
                (Factory<TestCase<Integer>>) () -> new IntTestCase(10, 5, 7)
                , () -> new IntTestCase(100, 50, 50)
                , () -> new IntTestCase(10000, 5000, 5000)
        ).collect(Collectors.toList());

        List<String> benchmarks = new ArrayList<>();

        testFactories.forEach(testCaseFactory -> listFactories.forEach(listFactory -> {
            TestCase<Integer> testCase = testCaseFactory.create();
            SortedLinkedList<Integer> instance = listFactory.create();
            log("main", testCase.toString(), instance.getClass().getSimpleName());

            long sum = 0;
            for (int i = 0; i < TEST_REPETITIONS; ++i) {
                long startTime = System.currentTimeMillis();
                testCaseFactory.create().test(listFactory.create());
                sum += System.currentTimeMillis() - startTime;
            }

            benchmarks.add(String.format(
                    "%s::%s => %s", testCase.toString(),
                    instance.getClass().getSimpleName(),
                    Double.toString((double) sum / TEST_REPETITIONS)
            ));
            benchmarks.forEach((s) -> log("main", s, "ms"));
        }));

        benchmarks.forEach((s) -> log("main", s, "ms"));
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
