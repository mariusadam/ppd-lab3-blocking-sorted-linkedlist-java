package ppd.lab3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Marius Adam
 */
public class LockingNode<E> {
    private Lock lock;

    private E value;
    private LockingNode<E> next;
    private LockingNode<E> prev;

    LockingNode(LockingNode<E> prev, E value, LockingNode<E> next) {
        this.value = value;
        setNext(next);
        setPrev(prev);
    }

    LockingNode(E elem) {
        value = elem;
    }

    LockingNode<E> prev() {
        return prev;
    }

    E value() {
        return value;
    }

    LockingNode<E> next() {
        return next;
    }

    void setNext(LockingNode<E> next) {
        this.next = next;

        if (next != null && next.prev() != this) {
            next.setPrev(this);
        }
    }

    void setPrev(LockingNode<E> prev) {
        this.prev = prev;

        if (prev != null && prev.next() != this) {
            prev.setNext(this);
        }
    }

    public Lock getLock() {
        if (lock == null) {
            lock = new ReentrantLock();
        }

        return lock;
    }
}
