package ppd.lab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Marius Adam
 */
public class IterateJob implements Runnable {
    private final SortedLinkedList list;
    private int iteration = 0;

    public IterateJob(SortedLinkedList list) {
        this.list = list;
    }

    @Override
    public synchronized void run() {
        synchronized (list) {
            List<Object> elems = new ArrayList<>();
            for (Object d : list) {
                if (elems.isEmpty()) {
                    Main.log("T4", "start iteration", String.valueOf(iteration));
                }
//            ppd.lab3.Main.log("T4", String.format("iteration = %d , elem = %s", iteration, d), "");
                elems.add(d);
            }
            Main.log("T4", Arrays.toString(elems.toArray()) + " after iteration", String.valueOf(iteration));
            iteration++;
        }
    }
}
