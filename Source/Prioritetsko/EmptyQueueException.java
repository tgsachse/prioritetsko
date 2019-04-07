// An exception that indicates a queue is empty.
// Written by Tiger Sachse.

package Prioritetsko;

// An exception class that indicates a queue is empty.
public class EmptyQueueException extends Exception {

    // Create a new EmptyQueueException with a message.
    public EmptyQueueException(String message) {
        super(message);
    }
}
