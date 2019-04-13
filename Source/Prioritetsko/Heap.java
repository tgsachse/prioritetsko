// Heap Implementation used by Priority Queue
// Written by Ben Faria

package Prioritetsko;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Heap<Element>
{
    AtomicReference<ArrayList<Element>> minHeap;

    public Heap()
    {
        minHeap = new AtomicReference<ArrayList<Element>>(new ArrayList<Element>());
    }
}