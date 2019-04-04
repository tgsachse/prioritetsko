// Main driver program that tests all provided queues in this package.
// Written by Tiger Sachse.

package Prioritetsko;

import java.util.Stack;
import java.util.Random;
import java.util.ArrayList;

// ./program threads totalPushes totalPops [runs]
// ./program 32 10000 10000 50

public class PrioritetskoDriver {
    private static final int DEFAULT_RUNS = 50;

    public static void main(String[] argumentVector) throws InterruptedException {
        Arguments arguments = parseArguments(argumentVector);
        ArrayList<PriorityQueue<Integer>> queues = new ArrayList<PriorityQueue<Integer>>();
        queues.add(new SynchronizedPriorityQueue<Integer>());
        queues.add(new SynchronizedPriorityQueue<Integer>());
        queues.add(new SynchronizedPriorityQueue<Integer>());
        
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

    private static Arguments parseArguments(String[] argumentVector) {
        Arguments arguments = new Arguments();

        if (argumentVector.length < 3) {
            System.err.println(
                "Arguments: <totalThreads> <totalPushes> <totalPops> [totalRuns]"
            );
            System.exit(1);
        }

        arguments.totalThreads = 0;
        arguments.totalPushes = 0;
        arguments.totalPops = 0;
        try {
            arguments.totalThreads = Integer.parseInt(argumentVector[0]);
            arguments.totalPushes = Integer.parseInt(argumentVector[1]);
            arguments.totalPops = Integer.parseInt(argumentVector[2]);
        }
        catch (NumberFormatException exception) {
            System.err.println("Your arguments must all be integers.");
            System.exit(2);
        }

        arguments.totalRuns = DEFAULT_RUNS;
        if (argumentVector.length > 3) {
            try {
                arguments.totalRuns = Integer.parseInt(argumentVector[3]);
            }
            catch (NumberFormatException exception) {
                System.err.println("The number of runs must be an integer.");
                System.exit(3);
            }
        }

        if (arguments.totalThreads < 1
            || arguments.totalPushes < 1
            || arguments.totalPops < 1
            || arguments.totalRuns < 1) {

            System.err.println("Your arguments must all be positive integers.");
            System.exit(4);
        }
        
        return arguments;
    }

    private static double[] runTests(
        int totalRuns,
        int totalThreads,
        int totalPushes,
        int totalPops,
        PriorityQueue<Integer> queue) throws InterruptedException {

        double[] results = new double[totalThreads];
        for (int runID = 0; runID < totalRuns; runID++) {
            for (int threadID = 0; threadID < results.length; threadID++) {
                results[threadID] += runTest(
                    threadID + 1,
                    totalPushes,
                    totalPops,
                    queue
                );
            }
        }

        for (int threadID = 0; threadID < results.length; threadID++) {
            results[threadID] /= totalRuns;
        }

        return results;
    }
    
    private static double runTest(
        int totalThreads,
        int totalPushes,
        int totalPops,
        PriorityQueue<Integer> queue) throws InterruptedException {

        // Allocate an appropriate number of threads for this test.
        Thread[] threads = new Thread[totalThreads];
        for (int threadID = 0; threadID < threads.length; threadID++) {
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

        // Return the results of this test in milliseconds.
        return ((double) (stopTime - startTime)) / 1000000;
    }
}

class Arguments {
    public int totalThreads;
    public int totalPushes;
    public int totalPops;
    public int totalRuns;
}

class QueueManipulator implements Runnable {
    private Random random;
    private int totalPops;
    private int totalPushes;
    private Stack<Integer> integerStack;
    private PriorityQueue<Integer> queue;

    public QueueManipulator(
        int totalPops,
        int totalPushes,
        PriorityQueue<Integer> queue) {

        this.queue = queue;
        this.totalPops = totalPops;
        this.totalPushes = totalPushes;

        random = new Random();
        integerStack = new Stack<Integer>();
        for (int count = 0; count < totalPushes; count++) { 
            integerStack.add(random.nextInt());
        }
    }

    @Override
    public void run() {
        int remainingPushes = totalPushes;
        int remainingPops = totalPops;

        float odds = ((float) totalPushes) / (totalPushes + totalPops);
        while (remainingPushes > 0 || remainingPops > 0) {
            boolean executePush = (random.nextFloat() > odds);
            if (executePush) {
                queue.insert(integerStack.pop());
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
