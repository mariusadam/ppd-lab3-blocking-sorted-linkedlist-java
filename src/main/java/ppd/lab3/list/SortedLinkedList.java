package ppd.lab3.list;

import java.util.*;

/**
 * @author Marius Adam
 */
public interface SortedLinkedList<E> extends Iterable<E> {
    void add(E elem);
    Comparator<E> comparator();
    boolean remove(E elem);
}
