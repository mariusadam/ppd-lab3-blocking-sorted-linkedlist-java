package ppd.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Marius Adam
 */
public abstract class TestCase<T extends Comparable<T>> {
    private int firstThreadCount;
    private int secondThreadCount;
    private int thirdThreadCount;

    public TestCase(int firstThreadCount, int secondThreadCount, int thirdThreadCount) {
        this.firstThreadCount = firstThreadCount;
        this.secondThreadCount = secondThreadCount;
        this.thirdThreadCount = thirdThreadCount;
    }

    protected void log(String threadName, String method, String argsAsString) {
        Main.log(threadName, method, argsAsString);
    }

    public void test(SortedLinkedList<T> linkedList) {
        List<Thread> threads = new ArrayList<>();
        Runnable iterateJob = new IterateJob(linkedList);

        threads.add(new Thread(() -> {
            for (int i = 0; i < firstThreadCount; i++) {
                T elem = getNewElement(i);
                log("T1", "before add", elem.toString());
                linkedList.add(elem);
                log("T1", "after add", elem.toString());
//                iterateJob.run();
            }
        }));

        threads.add(new Thread(() -> {
            for (int i = 0; i < secondThreadCount; i++) {
                T elem = getNewElement(i);
                log("T2", "before add", elem.toString());
                linkedList.add(elem);
                log("T2", "after add", elem.toString());
                // iterateJob.run();
            }
        }));

        threads.add(new Thread(() -> {
            for (int i = 0; i < thirdThreadCount; i++) {
                T elem = getNewElement(i);
                log("T3", "before delete", elem.toString());
                boolean removed = linkedList.remove(elem);
                log("T3", "after delete", String.format("%s - %s", elem.toString(), removed));
                // iterateJob.run();
            }
        }));


        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(iterateJob, 0, 100, TimeUnit.MICROSECONDS);

        threads.forEach(Thread::start);

        threads.forEach(this::join);
        executor.shutdownNow();
//        iterateJob.run();
    }

    abstract protected T getNewElement(int offset);

    protected void join(Thread t) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


