// Heap Implementation used by Priority Queue
// Written by Ben Faria

package Prioritetsko;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Heap
{
    private AtomicReference<ArrayList<Element>> minHeap;

    public Heap()
    {
        minHeap = new AtomicReference<ArrayList<Element>>(new ArrayList<Element>());
    }

    // Insert Element by priority into minheap
    public boolean insert(Element e)
    {
        ArrayList<Element> currentHeap = minHeap.get();
        ArrayList<Element> updatedHeap = currentHeap;

        // Insert element into currentHeap
        updatedHeap.add(e);
        updatedHeap = percolateUp(updatedHeap, updatedHeap.size() - 1);

        return minHeap.compareAndSet(updatedHeap, updatedHeap);
    }

    // Remove min Element by priority from minheap
    public Element removeMin()
    {
        ArrayList<Element> currentHeap;
        ArrayList<Element> updatedHeap;
        Element min;

        // Continue until successful
        do
        {
            currentHeap = minHeap.get();
            updatedHeap = currentHeap;

            if (currentHeap.size() == 0)
                return null;

            min = updatedHeap.get(0);
            Element rem = updatedHeap.remove(updatedHeap.size() - 1);

            if (updatedHeap.size() > 0)
                updatedHeap.set(0, rem);
            updatedHeap = percolateDown(updatedHeap, 0);
        } while (!minHeap.compareAndSet(currentHeap, updatedHeap));

        return min;
    }

    // Get min Element without removing
    public Element getMin()
    {
        ArrayList<Element> currentHeap = minHeap.get();

        if (currentHeap.size() == 0)
            return null;

        return currentHeap.get(0);
    }

    // Percolate Element up through minheap
    private ArrayList<Element> percolateUp(ArrayList<Element> heap, int idx)
    {
        int parent = 0;
        
        // Move element up in minheap based on priority level
        if (idx != 0)
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
    private ArrayList<Element> percolateDown(ArrayList<Element> heap, int idx)
    {
        Element child = getMinChild(heap, idx);
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
    private ArrayList<Element> swap(ArrayList<Element> heap, int first, int second)
    {
        Element temp = heap.get(first);
        heap.set(first, heap.get(second));
        heap.set(second, temp);

        return heap;
    }

    // Get the min priority child for parent
    private Element getMinChild(ArrayList<Element> heap, int parent)
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