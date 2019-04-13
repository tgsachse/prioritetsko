// Adaptive Priority Queue with Elimination and Combining.
// Written by Christopher Taliaferro & Ben Faria

package Prioritetsko;

import java.util.ArrayList;
import java.util.Random;

public class ECPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    public ArrayList<E> elimination;
    public Random priotity;

    // Server thread that handles elimination array operations
    private Server serverThread;

    // Fixed Values
    private final int eliminationArraySize = 100;

    public ECPriorityQueue () {
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
        Element inserting = new Element(element, 1, priotity.nextInt());
        
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