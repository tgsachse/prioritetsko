// A priority queue that implements Software Transactional Memory.
// Written by Harrison Black.

package prioritetsko;

import org.deuce.Atomic;
import java.lang.Comparable;
import java.util.Collection;
import org.deuce.transaction.TransactionException;

public class STMPriorityQueue
    <E extends Comparable<E>>
    extends SequentialPriorityQueue<E> {

    @Override
    @Atomic
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
    public int size() {
        try {
            return super.size();
        }
        catch (NullPointerException | IndexOutOfBoundsException exception) {
            throw new TransactionException();
        }
    }
}
