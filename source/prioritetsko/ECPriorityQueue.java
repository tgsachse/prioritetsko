// Adaptive Priority Queue with Elimination and Combining.
// Written by Christopher Taliaferro

package prioritetsko;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class ECPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    public CopyOnWriteArrayList<Element<E>> elimination;
    public AtomicReference<Heap<E>> pQueue; 
    private Random priotity;
    private AtomicBoolean lock;

    // Server thread that handles elimination array operations
    private Server serverThread;

    private final int REMOVE = 1;
    private final int INSERT = 2;

    public ECPriorityQueue () {
        // Initialize our priority queue
        pQueue = new AtomicReference<Heap<E>>(new Heap<E>()); 

        // Initialize elimination array
        elimination = new CopyOnWriteArrayList<>();

        // Random number to assign to new elements
        priotity = new Random();

        lock = new AtomicBoolean();

        // Initialize and begin server thread
        serverThread = new Server();
        serverThread.start();
    }

    // Add element into our priority queue
    public void insert(E element) {
        Heap<E> currentpQueue = pQueue.get();
        Heap<E> updatedpQueue = new Heap<E>(currentpQueue);

        Element<E> inserting = new Element<>(element, INSERT, priotity.nextInt());
        Element<E> minValue = updatedpQueue.getMin();

        if (minValue == null || inserting.priority < minValue.priority) {
            elimination.add(inserting);
        }
        else {
            updatedpQueue.insert(inserting);

            if (!pQueue.compareAndSet(currentpQueue, updatedpQueue))
            {
                elimination.add(inserting);
            }
        }
    }

    // Removes minimum priority element from priority queue
    public E retrieve() throws EmptyQueueException {
        Heap<E> currentpQueue = pQueue.get();
        Heap<E> updatedpQueue;

        CopyOnWriteArrayList<Element<E>> copiedElimination = new CopyOnWriteArrayList<Element<E>>(elimination);
        Element<E> retVal;

        for(Element<E> object: copiedElimination) {
            if (currentpQueue.getMin() == null || (object.priority < currentpQueue.getMin().priority && object.status == INSERT)) {
                int idx = copiedElimination.indexOf(object);
                retVal = copiedElimination.get(idx);
                Element<E> newElement = new Element<E>(retVal.value, REMOVE, retVal.priority);
                elimination.add(newElement);
                elimination.remove(object);
                return retVal.value;
            }
        }

        do {
            currentpQueue = pQueue.get();
            updatedpQueue = new Heap<E>(currentpQueue);

            retVal = updatedpQueue.removeMin();
        }while (!pQueue.compareAndSet(currentpQueue, updatedpQueue));
        
        if(retVal == null) return null;

        return retVal.value;     
    }

    public void finish() {
        serverThread.finish();
    }

    // Server thread that constantly checks elimination array for 
    // removes and values that need to be added to the skiplist
    private class Server extends Thread {
        protected volatile boolean isRunning;

        public Server() {
            isRunning = true;
        }

        public void finish() {
            isRunning = false;
        }
        
        public void run(){
            while (isRunning) {
                Heap<E> currentpQueue;
                Heap<E> updatedpQueue;
                CopyOnWriteArrayList<Element<E>> copiedElimination = new CopyOnWriteArrayList<Element<E>>(elimination);
                for(Element<E> object: copiedElimination) {
                    if (object.status == REMOVE)
                        elimination.remove(object);
                    else if (object.status == INSERT){
                        currentpQueue = pQueue.get();
                        updatedpQueue = new Heap<E>(currentpQueue);

                        updatedpQueue.insert(object);

                        if (pQueue.compareAndSet(currentpQueue, updatedpQueue))
                            elimination.remove(object);
                    }
                }
            }
        }
    }
}
