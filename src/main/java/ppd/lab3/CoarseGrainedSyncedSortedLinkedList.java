package ppd.lab3;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Marius Adam
 */
public class CoarseGrainedSyncedSortedLinkedList<E extends Comparable<E>> implements SortedLinkedList<E> {
    private Node<E> root;
    private Comparator<E> comparator;
    private final Lock lock = new ReentrantLock();

    public CoarseGrainedSyncedSortedLinkedList() {
        this(Comparable::compareTo);
    }

    public CoarseGrainedSyncedSortedLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void add(E elem) {
        lock.lock();
        try {
            doAdd(elem);
        } finally {
            lock.unlock();
        }
    }

    private void doAdd(E elem) {
        if (this.root == null) {
            this.root = new Node<>(elem);
        } else if (this.comparator().compare(elem, this.root.value()) < 0) {
            this.root = new Node<>(null, elem, this.root);
        } else {
            Node<E> current = root;
            while (current.next() != null && comparator().compare(elem, current.value()) > 0) {
                current = current.next();
            }

            current.setNext(new Node<>(current, elem, current.next()));
        }
    }

    @Override
    public Comparator<E> comparator() {
        return comparator;
    }

    @Override
    public boolean remove(E elem) {
        lock.lock();
        try {
            return doRemove(elem);
        } finally {
            lock.unlock();
        }
    }

    private boolean doRemove(E elem) {
        Node<E> current = root;
        while (current != null && !current.value().equals(elem)) {
            current = current.next();
        }

        if (current == null) {
            return false;
        } else {
            Node<E> prev = current.prev();
            Node<E> next = current.next() == null ? null : current.next().next();
            if (prev == null) {
                root = next;
            } else {
                prev.setNext(next);
            }

            return true;
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.lock();

        return new Iterator<E>() {
            private Node<E> current = root;

            @Override
            public boolean hasNext() {
                boolean hasNext = current != null;
                if (!hasNext) {
                    lock.unlock();
                }

                return hasNext;
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
