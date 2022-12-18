import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        public Item item;
        public Node next;
        public Node previous;
    }

    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        var oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;

        if (first.next != null) {
            first.next.previous = first;
        }

        if (last == null) {
            last = first;
        }

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        var oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (last.previous != null) {
            last.previous.next = last;
        }

        if (first == null) {
            first = last;
        }

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }

        var i = first.item;
        var n = first.next;
        first.next = null;
        first = n;
        if (first != null) {
            first.previous = null;
        } else {
            last = first;
        }

        size--;
        return i;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        var i = last.item;
        var p = last.previous;
        last.previous = null;
        last = p;
        if (last != null) {
            last.next = null;
        } else {
            first = last;
        }

        size--;

        return i;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        var d = new Deque<String>();

        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        d.addFirst("a");
        StdOut.println(d.size());
        StdOut.println(d.isEmpty());
        StdOut.println("------------------");
        for (var s : d) {
            StdOut.println(s);
        }

        StdOut.println("------------------");
        d.addLast("b");
        d.addFirst("c");
        d.addLast("d");
        d.addFirst("e");

        while (!d.isEmpty()) {
            StdOut.println(d.removeFirst());
        }

        StdOut.println("------------------");
        d.addLast("b");
        d.addFirst("c");
        d.addLast("d");
        d.addFirst("e");

        while (!d.isEmpty()) {
            StdOut.println(d.removeLast());
        }
    }
}
