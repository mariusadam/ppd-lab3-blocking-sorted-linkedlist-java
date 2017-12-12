package ppd.lab3.list;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Iterator that is capable of iterating over a blocking sorted linked list
 * Note that this iterator is meant to be used only inside of foreach in order
 * to release the lock at the end of the iteration
 *
 * @param <E>
 */
public class BlockingLinkedListIterator<E> implements Iterator<E> {
    private NodeInterface<E> current;

    /**
     * The lock used to restrict threads access to list operations
     */
    private final Lock listLock;

    public BlockingLinkedListIterator(NodeInterface<E> start, Lock listLock) {
        listLock.lock();
        this.current = start;
        this.listLock = listLock;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = current != null;
        if (!hasNext) {
            // we finished iteration so the lock can be released
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
