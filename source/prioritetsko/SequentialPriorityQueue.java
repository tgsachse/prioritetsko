// A sequential priority queue from scratch.
// Written by Tiger Sachse.

package prioritetsko;

import java.util.ArrayList;
import java.lang.Comparable;
import java.util.Collection;
import java.util.Collections;

// Provides a sequential, generic priority queue.
public class SequentialPriorityQueue
    <E extends Comparable<E>>
    implements PriorityQueue<E> {

    private ArrayList<E> elements;

    // Create a new, empty priority queue.
    public SequentialPriorityQueue() {
        elements = new ArrayList<E>();
    }

    // Create a new priority queue with a collection of elements.
    public SequentialPriorityQueue(Collection<? extends E> collection) {
        elements = new ArrayList<E>();

        for (E element : collection) {
            insert(element);
        }
    }

    @Override
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

    @Override
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

    // Get the element at the front of the priority queue.
    public E peek() throws EmptyQueueException {
        try {
            return elements.get(0);
        }
        catch (IndexOutOfBoundsException exception) {
            throw new EmptyQueueException("The priority queue is empty!");
        }
    }

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

    // Clear out the priority queue.
    public void clear() {
        elements.clear();
    }

    // Get the size of the priority queue.
    public int size() {
        return elements.size();
    }

    @Override
    // Only necessary to match the priority queue interface.
    public void finish() {}

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
}
