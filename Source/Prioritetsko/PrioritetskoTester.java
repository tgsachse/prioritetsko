// Tests a variety of synchronized priority queue implementations and output the
// results to stdout. This program is executed like so:
//     $ java PrioritetskoTester <totalThreads> <totalPushes> <totalPops> [totalRuns]
// Written by Tiger Sachse.

package Prioritetsko;

import java.util.Stack;
import java.util.Random;
import java.util.ArrayList;
import java.util.EmptyStackException;

// Test a variety of synchronized priority queues.
public class PrioritetskoTester {
    private static final int DEFAULT_RUNS = 50;

    // Main entry point to this program.
    public static void main(String[] argumentsVector) throws InterruptedException {
        
        // Parse the command line arguments and create an arraylist of queues for testing.
        Arguments arguments = parseArguments(argumentsVector);
        ArrayList<PriorityQueue<Integer>> queues = new ArrayList<PriorityQueue<Integer>>();
        queues.add(new SynchronizedPriorityQueue<Integer>());
        queues.add(new SynchronizedPriorityQueue<Integer>());
        queues.add(new SynchronizedPriorityQueue<Integer>());
        
        // Run tests on each queue and save the results in an array.
        for (PriorityQueue<Integer> queue : queues) {
            double[] results = runTests(
                arguments.totalRuns,
                arguments.totalThreads,
                arguments.totalPushes,
                arguments.totalPops,
                queue
            );

            //printResults(results);
        }
    }

    // Parse the command line arguments.
    private static Arguments parseArguments(String[] argumentsVector) {
        Arguments arguments = new Arguments();

        // Ensure that enough arguments are present.
        if (argumentsVector.length < 3) {
            System.err.println(
                "Arguments: <totalThreads> <totalPushes> <totalPops> [totalRuns]"
            );
            System.exit(1);
        }

        // Parse out the first three integers.
        arguments.totalThreads = 0;
        arguments.totalPushes = 0;
        arguments.totalPops = 0;
        try {
            arguments.totalThreads = Integer.parseInt(argumentsVector[0]);
            arguments.totalPushes = Integer.parseInt(argumentsVector[1]);
            arguments.totalPops = Integer.parseInt(argumentsVector[2]);
        }
        catch (NumberFormatException exception) {
            System.err.println("Your arguments must all be integers.");
            System.exit(2);
        }

        // If a fourth integer is given, overwrite the default totalRuns value.
        arguments.totalRuns = DEFAULT_RUNS;
        if (argumentsVector.length > 3) {
            try {
                arguments.totalRuns = Integer.parseInt(argumentsVector[3]);
            }
            catch (NumberFormatException exception) {
                System.err.println("The number of runs must be an integer.");
                System.exit(3);
            }
        }

        // Ensure that all integers are positive.
        if (arguments.totalThreads < 1
            || arguments.totalPushes < 1
            || arguments.totalPops < 1
            || arguments.totalRuns < 1) {

            System.err.println("Your arguments must all be positive integers.");
            System.exit(4);
        }
        
        return arguments;
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

        // Return the execution time of this test in milliseconds.
        return ((double) (stopTime - startTime)) / 1000000;
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

