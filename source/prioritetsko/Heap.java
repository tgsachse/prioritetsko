// Heap Implementation used by Priority Queue
// Written by Ben Faria

package prioritetsko;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Heap<E extends Comparable<E>>
{
    public ArrayList<Element<E>> minHeap;

    public Heap()
    {
        minHeap = new ArrayList<Element<E>>();
    }

    public Heap(Heap<E> h)
    {
        minHeap = h.minHeap;
    }

    // Insert Element by priority into minheap
    public void insert(Element<E> e)
    {
        // Insert element into currentHeap
        minHeap.add(e);
        percolateUp(minHeap.size() - 1);
    }

    // Remove min Element by priority from minheap
    public Element<E> removeMin()
    {
        if (minHeap.size() <= 0 )
            return null;

        Element<E> min = minHeap.get(0);
        Element<E> rem = minHeap.remove(minHeap.size() - 1);

        if (minHeap.size() > 0)
            minHeap.set(0, rem);

        percolateDown(0);

        return min;
    }

    // Get min Element without removing
    public Element<E> getMin()
    {
        if (minHeap.size() <= 0)
            return null;

        return minHeap.get(0);
    }

    // Percolate Element up through minheap
    private void percolateUp(int idx)
    {
        int parent = 0;
        
        // Move element up in minheap based on priority level
        if (idx > 0)
        {
            parent = (idx - 1)/2;
            if (minHeap.get(idx).priority < minHeap.get(parent).priority)
            {
                swap(parent, idx);
                percolateUp(parent);
            }
        }
    }

    // Percolate Elementdown through minheap
    private void percolateDown(int idx)
    {
        Element<E> child = getMinChild(idx);
        if (child == null)
            return;

        int childIdx = minHeap.indexOf(child);

        if (minHeap.size() > 1)
        {
            while (child.priority < minHeap.get(idx).priority)
            {
                swap(idx, childIdx);
                idx = childIdx;
                child = getMinChild(idx);
                if (child == null)
                    return;

                childIdx = minHeap.indexOf(child);
            }
        }
    }

    // Swap to values in heap
    private void swap(int first, int second)
    {
        Element<E> temp = minHeap.get(first);
        minHeap.set(first, minHeap.get(second));
        minHeap.set(second, temp);
    }

    // Get the min priority child for parent
    private Element<E> getMinChild(int parent)
    {
        int left = (parent * 2) + 1;
        int right = (parent * 2) + 2;
        
        if (left < minHeap.size() && right < minHeap.size())
        {
            if (minHeap.get(left).priority > minHeap.get(right).priority)
                return minHeap.get(right);

            return minHeap.get(left);
        }
        else if (left < minHeap.size())
        {
            return minHeap.get(left);
        }
        
        return null;
    }
}
