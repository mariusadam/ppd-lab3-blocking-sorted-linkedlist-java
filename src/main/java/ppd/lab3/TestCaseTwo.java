package ppd.lab3;

/**
 * @author Marius Adam
 */
abstract public class TestCaseTwo<E extends Comparable<E>> extends TestCase<E> {
    public TestCaseTwo(int firstThreadCount, int secondThreadCount, int thirdThreadCount) {
        super(firstThreadCount, secondThreadCount, thirdThreadCount);
    }
}
