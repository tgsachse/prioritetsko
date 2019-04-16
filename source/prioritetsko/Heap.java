// Heap Implementation used by Priority Queue
// Written by Ben Faria

package prioritetsko;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Heap<E extends Comparable<E>>
{
    private AtomicReference<ArrayList<Element<E>>> minHeap;

    public Heap()
    {
        minHeap = new AtomicReference<ArrayList<Element<E>>>(new ArrayList<Element<E>>());
    }

    // Insert Element by priority into minheap
    public boolean insert(Element<E> e)
    {
        ArrayList<Element<E>> currentHeap = minHeap.get();
        ArrayList<Element<E>> updatedHeap = new ArrayList<Element<E>>(currentHeap);

        // Insert element into currentHeap
        updatedHeap.add(e);
        updatedHeap = percolateUp(updatedHeap, updatedHeap.size() - 1);

        return minHeap.compareAndSet(currentHeap, updatedHeap);
    }

    // Remove min Element by priority from minheap
    public Element<E> removeMin()
    {
        ArrayList<Element<E>> currentHeap;
        ArrayList<Element<E>> updatedHeap;
        Element<E> min;

        // Continue until successful
        do
        {
            currentHeap = minHeap.get();
            updatedHeap = new ArrayList<Element<E>>(currentHeap);

            if (currentHeap.size() <= 0 )
                return null;

            min = updatedHeap.get(0);
            Element<E> rem = updatedHeap.remove(updatedHeap.size() - 1);

            if (updatedHeap.size() > 0)
                updatedHeap.set(0, rem);
            updatedHeap = percolateDown(updatedHeap, 0);
        } while (!minHeap.compareAndSet(currentHeap, updatedHeap));

        return min;
    }

    // Get min Element without removing
    public Element<E> getMin()
    {
        ArrayList<Element<E>> currentHeap = minHeap.get();

        if (currentHeap.size() <= 0)
            return null;

        return currentHeap.get(0);
    }

    // Percolate Element up through minheap
    private ArrayList<Element<E>> percolateUp(ArrayList<Element<E>> heap, int idx)
    {
        int parent = 0;
        
        // Move element up in minheap based on priority level
        if (idx > 0)
        {
            parent = (idx - 1)/2;
            if (heap.get(idx).priority < heap.get(parent).priority)
            {
                heap = swap(heap, parent, idx);
                percolateUp(heap, parent);
            }
        }

        return heap;
    }

    // Percolate Elementdown through minheap
    private ArrayList<Element<E>> percolateDown(ArrayList<Element<E>> heap, int idx)
    {
        Element<E> child = getMinChild(heap, idx);
        if (child == null)
            return heap;
        int childIdx = heap.indexOf(child);

        if (heap.size() > 1)
        {
            while (child.priority < heap.get(idx).priority)
            {
                heap = swap(heap, idx, childIdx);
                idx = childIdx;
                child = getMinChild(heap, idx);
                if (child == null)
                    return heap;

                childIdx = heap.indexOf(child);
            }
        }
        
        return heap;
    }

    // Swap to values in heap
    private ArrayList<Element<E>> swap(ArrayList<Element<E>> heap, int first, int second)
    {
        Element<E> temp = heap.get(first);
        heap.set(first, heap.get(second));
        heap.set(second, temp);

        return heap;
    }

    // Get the min priority child for parent
    private Element<E> getMinChild(ArrayList<Element<E>> heap, int parent)
    {
        int left = (parent * 2) + 1;
        int right = (parent * 2) + 2;
        
        if (left < heap.size() && right < heap.size())
        {
            if (heap.get(left).priority > heap.get(right).priority)
                return heap.get(right);

            return heap.get(left);
        }
        else if (left < heap.size())
        {
            return heap.get(left);
        }
        
        return null;
    }
}
