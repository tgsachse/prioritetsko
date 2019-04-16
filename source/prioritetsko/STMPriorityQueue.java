// A priority queue that implements Software Transactional Memory.
// Written by Harrison Black and Tiger Sachse.

package prioritetsko;

import org.deuce.Atomic;
import java.lang.Comparable;
import java.util.Collection;
import org.deuce.transaction.TransactionException;

// Provides a parallelized priority queue using software transactional memory.
public class STMPriorityQueue
    <E extends Comparable<E>>
    extends SequentialPriorityQueue<E> {

    @Override
    @Atomic
    // Insert an element. If the transaction fails, start over.
    public void insert(E element) {
        try {
            super.insert(element);
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Insert a bunch of elements. If the transaction fails, start over.
    public void insert(Collection<? extends E> collection) {
        try {
            super.insert(collection);
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Retrieve an element. If the transaction fails, start over.
    public E retrieve() throws EmptyQueueException {
        try {
            return super.retrieve();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Peek at the highest-priority element. If the transaction fails, start over.
    public E peek() throws EmptyQueueException {
        try {
            return super.peek();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Check if the priority queue is empty. If the transaction fails, start over.
    public boolean isEmpty() {
        try {
            return super.isEmpty();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Get this queue as a string. If the transaction fails, start over.
    public String toString() {
        try {
            return super.toString();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Empty the queue. If the transaction fails, start over.
    public void clear() {
        try {
            super.clear();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }

    @Override
    @Atomic
    // Get the size of the queue. If the transaction fails, start over.
    public int size() {
        try {
            return super.size();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }
}
