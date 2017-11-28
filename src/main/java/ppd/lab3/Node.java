package ppd.lab3;

/**
 * @author Marius Adam
 */
class Node<E> {
    private E value;
    private Node<E> next;
    private Node<E> prev;

    Node(Node<E> prev, E value, Node<E> next) {
        this(value);

        setNext(next);
        setPrev(prev);
    }

    Node(E elem) {
        value = elem;
    }

    Node<E> prev() {
        return prev;
    }

    E value() {
        return value;
    }

    Node<E> next() {
        return next;
    }

    void setNext(Node<E> next) {
        this.next = next;

        if (next != null) {
            next.prev = this;
        }
    }

    void setPrev(Node<E> prev) {
        this.prev = prev;

        if (prev != null) {
            prev.next = this;
        }
    }
}
