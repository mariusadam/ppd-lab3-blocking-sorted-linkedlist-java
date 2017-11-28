package ppd.lab3;

/**
 * @author Marius Adam
 */
public class TestCaseTwoInt extends TestCaseTwo<Integer> {
    public TestCaseTwoInt() {
        super(100, 50, 50);
    }

    @Override
    protected Integer getNewElement(int offset) {
        return offset;
    }
}
