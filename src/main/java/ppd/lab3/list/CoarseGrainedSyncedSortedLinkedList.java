package ppd.lab3.list;

import java.util.Comparator;
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
        if (root == null) {
            root = new Node<>(elem);
            return;
        }
        if (this.comparator().compare(elem, root.value()) < 0) {
            Node<E> oldRoot = root;
            root = new Node<>(elem);
            root.setNext(oldRoot);
        } else {
            Node<E> prev = root, current = root.next(), node = new Node<>(elem);
            while (current != null) {
                if (comparator().compare(current.value(), node.value()) >= 0) {
                    prev.setNext(node);
                    node.setNext(current);
                    return;
                }
                prev = current;
                current = current.next();
            }
            prev.setNext(node);
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
        if (root == null) {
            return false;
        }

        Node<E> prev = root, current = root.next();
        while (current != null) {
            if (comparator().compare(current.value(), elem) == 0) {
                prev.setNext(current.next());
                return true;
            }

            prev = current;
            current = current.next();
        }
        return false;
    }

    @Override
    public BlockingLinkedListIterator<E> iterator() {
        return new BlockingLinkedListIterator<E>(root, lock);
    }

    private static class Node<E> implements NodeInterface<E>{
        private E value;
        private Node<E> next;

        Node(E elem) {
            value = elem;
        }

        public E value() {
            return value;
        }

        public Node<E> next() {
            return next;
        }

        void setNext(Node<E> next) {
            this.next = next;
        }
    }
}
