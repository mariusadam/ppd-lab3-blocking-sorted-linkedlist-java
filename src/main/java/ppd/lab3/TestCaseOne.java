package ppd.lab3;

/**
 * @author Marius Adam
 */
public abstract class TestCaseOne<T extends Comparable<T>> extends TestCase<T> {
    public TestCaseOne(int firstThreadCount, int secondThreadCount, int thirdThreadCount) {
        super(firstThreadCount, secondThreadCount, thirdThreadCount);
    }

    abstract protected T getNewElement(int offset);
}
