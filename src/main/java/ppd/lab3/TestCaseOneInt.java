package ppd.lab3;

/**
 * @author Marius Adam
 */
public class TestCaseOneInt extends TestCaseOne<Integer> {
    public TestCaseOneInt() {
        super(10, 5, 7);
    }

    @Override
    protected Integer getNewElement(int offset) {
        return offset;
    }
}
