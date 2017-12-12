package ppd.lab3;

public interface NodeInterface<E> {
    E value();
    NodeInterface<E> next();
}
