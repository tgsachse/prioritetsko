// A priority queue that implements Software Transactional Memory.
// Written by Harrison Black.

package prioritetsko;

import org.deuce.Atomic;
import java.lang.Comparable;
import java.util.Collection;
import javolution.util.FastTable;

public class STMPriorityQueue
    <E extends Comparable<E>>
    extends SequentialPriorityQueue<E> {

    public STMPriorityQueue() {
        elements = new FastTable<E>();
    }

    @Override
    @Atomic
    public void insert(E element) {
        super.insert(element);
    }

    @Override
    @Atomic
    public void insert(Collection<? extends E> collection) {
        super.insert(collection);
    }

    @Override
    @Atomic
    public E retrieve() throws EmptyQueueException {
        return super.retrieve();
    }

    @Override
    @Atomic
    public E peek() throws EmptyQueueException {
        return super.peek();
    }

    @Override
    @Atomic
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    @Atomic
    public String toString() {
        return super.toString();
    }

    @Override
    @Atomic
    public void clear() {
        super.clear();
    }

    @Override
    @Atomic
    public int size() {
        return super.size();
    }
}
