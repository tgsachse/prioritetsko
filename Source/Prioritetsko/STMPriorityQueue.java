// A priority queue that implements Software Transactional Memory.
// Written by Harrison Black and Tiger Sachse.

package Prioritetsko;

import java.util.ArrayList;
import java.lang.Comparable;
import java.util.Collection;
import java.util.Collections;

// These three can be removed later
import java.util.Stack;
import java.util.Random;
import java.util.EmptyStackException;

// Provides a synchronized, generic priority queue.
public class STMPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    private ArrayList<E> elements;

    public void finish() {}

    // Create a new, empty priority queue.
    public STMPriorityQueue() {
        elements = new ArrayList<E>();
    }

    // Create a new priority queue with a collection of elements.
    public STMPriorityQueue(Collection<? extends E> collection) {
        elements = new ArrayList<E>();

        for (E element : collection) {
            insert(element);
        }
    }

    @Atomic
    // Add an element to the priority queue.
    public void insert(E element) {

        // The element is added to the end of the elements list and then is
        // percolated up to its appropriate position.
        elements.add(element);
        percolateElementUp(getMaxIndex());
    }

    // Add a collection of elements to the priority queue.
    public void insert(Collection<? extends E> collection) {
        for (E element : collection) {
            insert(element);
        }
    }

    @Atomic
    // Get and remove the element at the front of the priority queue.
    public E retrieve() throws EmptyQueueException {

        // Attempt to retrieve the front element. If the queue is empty, throw
        // an exception.
        E retrievedElement;
        try {
            retrievedElement = elements.get(0);
        }
        catch (IndexOutOfBoundsException exception) {
            throw new EmptyQueueException("The priority queue is empty!");
        }

        // Attempt to move the element at the back of the elements list to the
        // front, then percolate that element down. If this causes an index
        // error (because the retrieved element from earlier was the only
        // element in the queue) then do nothing.
        try {
            elements.set(0, elements.remove(getMaxIndex()));
            percolateElementDown(0);
        }
        catch (IndexOutOfBoundsException exception) {
        }

        return retrievedElement;
    }

    @Atomic
    // Get the element at the front of the priority queue.
    public E peek() throws EmptyQueueException {
        try {
            return elements.get(0);
        }
        catch (IndexOutOfBoundsException exception) {
            throw new EmptyQueueException("The priority queue is empty!");
        }
    }

    @Atomic
    // Check if the priority queue is empty.
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    // Get a string representation of this priority queue.
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int elementIndex = 1; elementIndex < (getMaxIndex()); elementIndex++) {
            stringBuilder.append(elements.get(elementIndex));
            stringBuilder.append(" ");
        }
        stringBuilder.append(elements.get(getMaxIndex()));

        return stringBuilder.toString();
    }

    @Atomic
    // Clear out the priority queue.
    public void clear() {
        elements.clear();
    }

    @Atomic
    // Get the size of the priority queue.
    public int size() {
        return elements.size();
    }

    // Get a parent's left child's index.
    private int getLeftChildIndex(int parentIndex) {
        return (parentIndex * 2) + 1;
    }

    // Get a parent's right child's index.
    private int getRightChildIndex(int parentIndex) {
        return (parentIndex * 2) + 2;
    }

    // Get a child's parent's index.
    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    // Get the maximum index currently occupied in the priority queue.
    private int getMaxIndex() {
        return elements.size() - 1;
    }

    // Move an element up through the priority queue to its proper place.
    private void percolateElementUp(int initialIndex) {
        int currentIndex = initialIndex;
        int parentIndex = getParentIndex(currentIndex);

        // While the parent index of the current element is in bounds, if the
        // parent element is larger than the current element: swap the two and
        // repeat.
        while (parentIndex >= 0) {
            E parentElement = elements.get(parentIndex);
            E currentElement = elements.get(currentIndex);

            if (parentElement.compareTo(currentElement) > 0) {
                Collections.swap(elements, parentIndex, currentIndex);
                currentIndex = parentIndex;
                parentIndex = getParentIndex(currentIndex);
            }
            else {
                break;
            }
        }
    }

    // Move an element down through the priority queue to its proper place.
    private void percolateElementDown(int initialIndex) {
        int currentIndex = initialIndex;
        int leftChildIndex = getLeftChildIndex(currentIndex);
        int rightChildIndex = getRightChildIndex(currentIndex);

        while (leftChildIndex <= (getMaxIndex())) {

            // Load the left child and current element. Attempt to load the
            // right child as well. If it does not exist, swap the current
            // element with the left child (if the left child is larger).
            E currentElement = elements.get(currentIndex);
            E leftChildElement = elements.get(leftChildIndex);
            E rightChildElement;
            try {
                rightChildElement = elements.get(rightChildIndex);
            }
            catch (IndexOutOfBoundsException exception) {
                if (leftChildElement.compareTo(currentElement) < 0) {
                    Collections.swap(elements, currentIndex, leftChildIndex);
                }
                break;
            }

            // Swap with the larger of the two children if the child is larger
            // than the current element.
            if (leftChildElement.compareTo(rightChildElement) < 0) {
                if (leftChildElement.compareTo(currentElement) < 0) {
                    Collections.swap(elements, currentIndex, leftChildIndex);
                    currentIndex = leftChildIndex;
                }
                else {
                    break;
                }
            }
            else {
                if (rightChildElement.compareTo(currentElement) < 0) {
                    Collections.swap(elements, currentIndex, rightChildIndex);
                    currentIndex = rightChildIndex;
                }
                else {
                    break;
                }
            }

            leftChildIndex = getLeftChildIndex(currentIndex);
            rightChildIndex = getRightChildIndex(currentIndex);
        }
    }

    // EVERYTHING BELOW HERE WILL BE DELETED

    // This class has it's own main at the moment due to deuce needed
    // to be compiled as a jar. I am testing this file on it's own for
    // now and will integrated it into the script later. 
    public static void main(String[] args) throws InterruptedException {
        int totalRuns = 10;
        int totalThreads = 10;
        int totalPushes = 10;
        int totalPops = 10;

        STMPriorityQueue<Integer> queue = new STMPriorityQueue<Integer>();

        double[] results = runTests(
                totalRuns,
                totalThreads,
                totalPushes,
                totalPops,
                queue
        );

        printResults(queue, results);
        
        // Get the average for each thread count.
        for (int threadID = 0; threadID < results.length; threadID++) {
            results[threadID] /= totalRuns;
        }

        for (int threadID = 0; threadID < results.length; threadID++) {
            System.out.printf(
                "Threads: %2d | Milliseconds: %f\n",
                threadID + 1,
                results[threadID]
            );
        }
    }

    // Run tests on a given queue with a range of threads.
    private static double[] runTests(
        int totalRuns,
        int totalThreads,
        int totalPushes,
        int totalPops,
        PriorityQueue<Integer> queue) throws InterruptedException {

        // For each run and for each thread count, run a test with that thread
        // count on the provided queue and save the results into an array.
        double[] results = new double[totalThreads];
        for (int runID = 0; runID < totalRuns; runID++) {
            for (int threadID = 0; threadID < totalThreads; threadID++) {
                results[threadID] += runTest(
                    threadID + 1,
                    totalPushes,
                    totalPops,
                    queue
                );
            }
        }

        // Get the average for each thread count.
        for (int threadID = 0; threadID < results.length; threadID++) {
            results[threadID] /= totalRuns;
        }

        return results;
    }

    // Push to and pop from a given queue a specific number of times.
    private static double runTest(
        int totalThreads,
        int totalPushes,
        int totalPops,
        PriorityQueue<Integer> queue) throws InterruptedException {

        // Allocate an appropriate number of threads for this test.
        Thread[] threads = new Thread[totalThreads];
        for (int threadID = 0; threadID < totalThreads; threadID++) {
            threads[threadID] = new Thread(
                new QueueManipulator(
                    totalPops,
                    totalPushes,
                    queue
                )
            );
        }

        // Begin the test, then wait for all threads to finish.
        long startTime = System.nanoTime();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long stopTime = System.nanoTime();
	queue.finish();

        // Return the execution time of this test (per thread) in milliseconds.
        return ((double) (stopTime - startTime)) / 1000000 / totalThreads;
    }

    // Print results for a queue's tests.
    private static void printResults(PriorityQueue<Integer> queue, double[] results) {
        System.out.printf(
            "Execution time per thread for the %s:\n",
            queue.getClass().getSimpleName()
        );

        for (int threadID = 0; threadID < results.length; threadID++) {
            System.out.printf(
                "Threads: %2d | Milliseconds: %f\n",
                threadID + 1,
                results[threadID]
            );
        }
    }


    // A runnable class that pushes to and pops from a queue a specific number of times.
class QueueManipulator implements Runnable {
    private Random random;
    private int totalPops;
    private int totalPushes;
    private Stack<Integer> integerStack;
    private PriorityQueue<Integer> queue;

    // Initialize this manipulator with a queue and some push and pop targets.
    public QueueManipulator(
        int totalPops,
        int totalPushes,
        PriorityQueue<Integer> queue) {

        this.queue = queue;
        this.totalPops = totalPops;
        this.totalPushes = totalPushes;

        // Create a stack of random numbers. These are preallocated to prevent
        // allocation time from polluting the performance of the stack during
        // testing.
        random = new Random();
        integerStack = new Stack<Integer>();
        for (int count = 0; count < totalPushes; count++) { 
            integerStack.add(new Integer(random.nextInt()));
        }
    }

    @Override
    // Push to and pop from a queue a specific number of times.
    public void run() {
        int remainingPushes = totalPushes;
        int remainingPops = totalPops;

        float odds = ((float) totalPushes) / (totalPushes + totalPops);
        while (remainingPushes > 0 || remainingPops > 0) {
            boolean executePush = (random.nextFloat() > odds);
            if (executePush) {
                try {
                    queue.insert(integerStack.pop());
                }
                catch (EmptyStackException exception) {
                }
                remainingPushes--;
            }
            else {
                try {
                    queue.retrieve();
                }
                catch (EmptyQueueException exception) {
                }
                remainingPops--;
            }
        }
    }
}

// A structure that holds command line arguments.
class Arguments {
    public int totalThreads;
    public int totalPushes;
    public int totalPops;
    public int totalRuns;
}

}
