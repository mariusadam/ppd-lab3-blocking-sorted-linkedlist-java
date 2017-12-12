package ppd.lab3;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Marius Adam
 */
public class FineGrainedSyncSortedLinkedList<E extends Comparable<E>> implements SortedLinkedList<E> {
    private Node<E> root;
    private Comparator<E> comparator;
    private Lock listLock = new ReentrantLock();

    public FineGrainedSyncSortedLinkedList() {
        this(Comparable::compareTo);
    }

    public FineGrainedSyncSortedLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public void add(E elem) {
        listLock.lock();

        if (root == null) {
            root = new Node<>(elem);
            listLock.unlock();
            return;
        }
        if (this.comparator().compare(elem, root.value()) < 0) {
            Node<E> oldRoot = root;
            root = new Node<>(elem);
            root.setNext(oldRoot);
            listLock.unlock();
        } else {
            Node<E> prev = root, current = root.next(), node = new Node<E>(elem);
            prev.getLock().lock();
            if (current != null) {
                current.getLock().lock();
            }
            listLock.unlock();
            while (current != null) {
                if (comparator().compare(current.value(), node.value()) >= 0) {
                    prev.setNext(node);
                    node.setNext(current);
                    prev.getLock().unlock();
                    current.getLock().unlock();
                    return;
                }
                Node<E> oldPrev = prev;
                prev = current;
                current = current.next();
                if (current != null) {
                    current.getLock().lock();
                }
                oldPrev.getLock().unlock();
            }
            prev.getLock().unlock();
        }
    }

    @Override
    public Comparator<E> comparator() {
        return comparator;
    }

    @Override
    public boolean remove(E elem) {
        listLock.lock();
        if (root == null) {
            listLock.unlock();
            return false;
        }

        Node<E> prev = root, current = root.next();
        prev.getLock().lock();
        if (current != null) {
            current.getLock().lock();
        }

        listLock.unlock();

        while (current != null) {
            if (comparator().compare(current.value(), elem) == 0) {
                prev.setNext(current.next());
                prev.getLock().unlock();
                current.getLock().lock();
                return true;
            }

            Node<E> oldPrev = prev;
            prev = current;
            current = current.next();
            if (current != null) {
                current.getLock().lock();
            }
            oldPrev.getLock().unlock();
        }
        prev.getLock().unlock();
        return false;
    }

    @Override
    public LockingIterator<E> iterator() {
        return new LockingIterator<>(root, listLock);
    }

    private static class Node<E> implements NodeInterface<E>{
        private Lock lock;
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

        private void setNext(Node<E> next) {
            this.next = next;
        }

        Lock getLock() {
            if (lock == null) {
                lock = new ReentrantLock();
            }

            return lock;
        }
    }
}
