package ppd.lab3;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

/**
 * @author Marius Adam
 */
public class FineGrainedSyncSortedLinkedList<E extends Comparable<E>> implements SortedLinkedList<E> {
    private LockingNode<E> root;
    private Comparator<E> comparator;

    public FineGrainedSyncSortedLinkedList() {
        this(Comparable::compareTo);
    }

    public FineGrainedSyncSortedLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void add(E elem) {
        synchronized (this) {
            if (root == null) {
                root = new LockingNode<E>(elem);
                return;
            }
        }
        root.getLock().lock();
        Lock rootLock = root.getLock();
        if (this.comparator().compare(elem, root.value()) < 0) {
            root = new LockingNode<>(null, elem, this.root);
            rootLock.unlock();
        } else {
            LockingNode<E> current = root;
            while (current.next() != null && comparator().compare(elem, current.value()) > 0) {
                unlock(current);
                current = current.next();
                lock(current);
            }
            lock(current.next());
            LockingNode<E> next = current.next();
            current.setNext(new LockingNode<>(current, elem, next));
            unlock(current);
            unlock(next);
        }
    }

    @Override
    public Comparator<E> comparator() {
        return comparator;
    }

    @Override
    public boolean remove(E elem) {
        synchronized (this) {
            if (root == null) {
                return false;
            }
        }

        lock(root);
        LockingNode<E> current = root;
        while (current != null && !current.value().equals(elem)) {
            unlock(current);
            current = current.next();
            lock(current);
        }

        if (current == null) {
            return false;
        } else {
            lock(current.prev());
            lock(current.next() == null ? null : current.next().next());
            LockingNode<E> prev = current.prev();
            LockingNode<E> next = current.next() == null ? null : current.next().next();
            if (prev == null) {
                root.getLock().lock();
                Lock rootLock = root.getLock();
                root = next;
                rootLock.unlock();
            } else {
                prev.setNext(next);
            }
            unlock(prev);
            unlock(next);
            unlock(current);
            return true;
        }
    }

    private void lock(LockingNode<E> node) {
        if (node != null) {
            node.getLock().lock();
        }
    }

    private void unlock(LockingNode<E> node) {
        if (node != null) {
            node.getLock().unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private LockingNode<E> current = root;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E val = current.value();
                current = current.next();
                return val;
            }
        };
    }
}
