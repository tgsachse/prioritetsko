// Adaptive Priority Queue with Elimination and Combining.
// Written by Christopher Taliaferro & Ben Faria

package Prioritetsko;

import java.util.ArrayList;
import java.util.Random;

public class ECPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    private ArrayList<Element> elimination;
    private Random priotity;
    private Heap pQueue; 

    // Server thread that handles elimination array operations
    private Server serverThread;

    private final int WAITING = 0;
    private final int REMOVE = 1;
    private final int INSERT = 2;

    public ECPriorityQueue () {
        // Initialize our priority queue
        pQueue = new Heap<>(); 

        // Initialize elimination array
        elimination = new ArrayList<>();

        // Random number to assign to new elements
        priotity = new Random();

        // Initialize and begin server thread
        serverThread = new Server();
        serverThread.start();
    }

    // Add element into our priority queue
    public void insert(E element) {
        Element<E> inserting = new Element<>(element, WAITING, priotity.nextInt());

        if (pQueue.insert(inserting)) {
            return;
        } else {
            elimination.add(inserting);
        }
    }

    // Removes minimum priority element from priority queue
    public E retrieve() throws EmptyQueueException {
        Element<E> retVal;

        try {
            retVal = pQueue.removeMin();
            return retVal.value;
        } catch (EmptyQueueException exception) {
            System.out.println("Queue is empty!");
        }        
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
                for(Element object: elimination) {
                    if (object.status == REMOVE)
                        elimination.remove(object);
                    
                    else if (object.status == INSERT){
                        if (pQueue.insert(object))
                            elimination.remove(object);
                    }
                }
        }
    }
}