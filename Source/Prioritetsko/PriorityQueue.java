// The general interface for all priority queues in this package.
// Written by Tiger Sachse.

package Prioritetsko;

import java.lang.Comparable;

// Interface for generic priority queues that support insertion and retrieval.
public interface PriorityQueue<E extends Comparable<E>> {
    public void insert(E element);
    public E retrieve() throws EmptyQueueException;
    public void finish();
}
