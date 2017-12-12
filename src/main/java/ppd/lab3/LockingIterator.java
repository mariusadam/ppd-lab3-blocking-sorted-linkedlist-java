package ppd.lab3;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;

public class LockingIterator<E> implements Iterator<E> {
    private NodeInterface<E> current;
    private Lock listLock;

    public LockingIterator(NodeInterface<E> current, Lock listLock) {
        listLock.lock();
        this.current = current;
        this.listLock = listLock;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = current != null;
        if (!hasNext) {
            listLock.unlock();
        }

        return hasNext;
    }

    @Override
    public E next() {
        E val = current.value();
        current = current.next();
        return val;
    }
}
