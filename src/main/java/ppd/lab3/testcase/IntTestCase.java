package ppd.lab3.testcase;

public class IntTestCase extends TestCase<Integer> {
    public IntTestCase(int firstThreadCount, int secondThreadCount, int thirdThreadCount) {
        super(firstThreadCount, secondThreadCount, thirdThreadCount);
    }

    @Override
    protected Integer getNewElement(int offset) {
        return offset;
    }
}
