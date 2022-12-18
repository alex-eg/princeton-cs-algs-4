import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int size = 0;
    private int capacity = 1;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int newCap) {
        Item[] copy = (Item[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            copy[i] = q[i];
        }
        capacity = newCap;
        q = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == capacity) {
            resize(capacity * 2);
        }
        q[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        var n = StdRandom.uniformInt(size);
        var i = q[n];
        q[n] = q[size - 1];
        q[size - 1] = null;
        size--;
        if (size < capacity / 4) {
            resize(capacity / 2);
        }
        return i;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return q[StdRandom.uniformInt(size)];
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int[] inds;
        private Item[] qRef;
        private int current = 0;
        private int size;

        public RandomQueueIterator(Item[] q, int s) {
            qRef = q;
            size = s;
            var p = StdRandom.permutation(size);
            inds = new int[s];
            for (int i = 0; i < size; i++) {
                inds[i] = p[i];
            }
        }

        public boolean hasNext() {
            return current < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return qRef[inds[current++]];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator(q, size);
    }

    // unit testing (required)
    public static void main(String[] args) {
        var d = new RandomizedQueue<String>();

        d.enqueue("abc");
        d.enqueue("def");
        d.enqueue("zxc");
        d.enqueue("vbn");

        StdOut.println("All queue:");
        for (var s : d) {
            StdOut.println(s);
        }
        StdOut.println("------------------");
        StdOut.println("All queue:");
        for (var s : d) {
            StdOut.println(s);
        }
        StdOut.println("Dequeue:");
        StdOut.println(d.dequeue());
        StdOut.println("------------------");
        StdOut.println("All queue:");
        for (var s : d) {
            StdOut.println(s);
        }
        StdOut.println("Dequeue:");
        StdOut.println(d.dequeue());
        StdOut.println("------------------");
        StdOut.println("All queue:");
        for (var s : d) {
            StdOut.println(s);
        }
        StdOut.println("Dequeue:");
        StdOut.println(d.dequeue());
        StdOut.println("------------------");
        StdOut.println("All queue:");
        for (var s : d) {
            StdOut.println(s);
        }
        StdOut.println("Dequeue:");
        StdOut.println(d.dequeue());
        StdOut.println("------------------");
        StdOut.println("All queue:");
        for (var s : d) {
            StdOut.println(s);
        }

        for (int i = 0; i < 100; i++) {
            var q = new RandomizedQueue<String>();
            q.enqueue("A");
            q.enqueue("B");
            StdOut.print(q.dequeue());
            StdOut.print(q.dequeue());
            StdOut.println();
        }
    }
}
