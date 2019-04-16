// A synchronized (coarse-grained locking) priority queue from scratch.
// Written by Tiger Sachse.

package prioritetsko;

import java.lang.Comparable;
import java.util.Collection;

// Provides a synchronized, generic priority queue.
public class SynchronizedPriorityQueue
    <E extends Comparable<E>>
    extends SequentialPriorityQueue<E> {

    @Override
    // Add an element to the synchronized priority queue.
    public synchronized void insert(E element) {
        super.insert(element);
    }

    @Override
    // Add a collection of elements to the synchronized priority queue.
    public synchronized void insert(Collection<? extends E> collection) {
        super.insert(collection);
    }

    @Override
    // Get and remove the element at the front of the synchronized priority queue.
    public synchronized E retrieve() throws EmptyQueueException {
        return super.retrieve();
    }

    @Override
    // Get the element at the front of the synchronized priority queue.
    public synchronized E peek() throws EmptyQueueException {
        return super.peek();
    }

    @Override
    // Check if the synchronized priority queue is empty.
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    // Get a string representation of this synchronized priority queue.
    public synchronized String toString() {
        return super.toString();
    }

    @Override
    // Clear out the synchronized priority queue.
    public synchronized void clear() {
        super.clear();
    }

    @Override
    // Get the size of the priority queue.
    public synchronized int size() {
        return super.size();
    }
}
