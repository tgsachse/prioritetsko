// Main driver program that tests all provided queues in this package.
// Written by Tiger Sachse.

package Prioritetsko;

public class PrioritetskoDriver {
    public static void main(String[] args) {
        SynchronizedPriorityQueue<Integer> queue = new SynchronizedPriorityQueue<Integer>();

        queue.insert(new Integer(10));
        queue.insert(new Integer(30));
        queue.insert(new Integer(1));
        queue.insert(new Integer(1));
        queue.insert(new Integer(1));
        queue.insert(new Integer(3));
        queue.insert(new Integer(100));
        queue.insert(new Integer(0));
        queue.insert(new Integer(200));
        queue.insert(new Integer(99));
        queue.insert(new Integer(10000));


        /*
        System.out.println(queue);
        while (!queue.isEmpty()) {
            try {
                System.out.println(queue.retrieve());
                //System.out.println(queue);
            }
            catch (EmptyQueueException exception) {
                break;
            }
        }*/
    }
}
