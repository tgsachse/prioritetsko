// Adaptive Priority Queue with Elimination and Combining.
// Written by Christopher Taliaferro & Ben Faria

package Prioritetsko;

import java.util.concurrent.atomic.*;

class Element<E extends Comparable<E>> {
    public E value;
    public int status;

    public Element(E value, int status) {
        this.value = value;
        this.status = status;
    }
}

public class ECPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    private SkipList skipList;
    private AtomicReference<Element>[] elimination;

    // Server thread that handles elimination array operations
    private Server serverThread;

    // Fixed Values
    private final int eliminationArraySize = 100;

    public ECPriorityQueue () {
        // Initialize a new skiplist
        skipList = new SkipList<>();
        // Initialize elimination array
        elimination = new AtomicReference[eliminationArraySize];

        for(int i = 0; i < eliminationArraySize; i++){
            elimination[i] = new AtomicReference<Element>();
        }

        // Initialize and begin server thread
        serverThread = new Server();
        serverThread.start();
    }

    // Add element into our priority queue
    public void insert(E element) {

    }

    // Removes minimum priority element from priority queue
    public E retrieve() throws EmptyQueueException {
        E retrievedElement = null;
        return retrievedElement;        
    }

    // Server thread that constantly checks elimination array for 
    // removes and values that need to be added to the skiplist
    private class Server extends Thread {
        protected volatile boolean run;

        public Server() {
            run = true;
        }
        public void run(){
            while (run)
                for(int i = 0; i < eliminationArraySize; i++) {
                    // TODO: Case where we must remove from the elimination array

                    // TODO: Case where we must add to skiplist
                }
        }
    }
}