package Prioritetsko;

import java.util.*;

public class SequentialPriorityQueue<E extends Comparable<E>> {
    private ArrayList<E> elements;

    // Create a new, empty priority queue.
    public SequentialPriorityQueue() {
        elements = new ArrayList<E>();
        elements.add(null);
    }

    /*
    public SequentialPriorityQueue(Comparator comparator) {
    }
    */

    //public SequentialPriorityQueue(Elements to enqueue)


    public void insert(E element) {
        elements.add(element);
        percolateElementUp(elements.size() - 1);
    }

    public E retrieve() throws EmptyQueueException {
        E retrievedElement;
        try {
            retrievedElement = elements.get(1);
        }
        catch (IndexOutOfBoundsException exception) {
            throw new EmptyQueueException("The priority queue is empty!");
        }

        try {
            elements.set(1, elements.remove(elements.size() - 1));
            percolateElementDown(1);
        }
        catch (IndexOutOfBoundsException exception) {
        }

        return retrievedElement;
    }

    public E peek() throws EmptyQueueException {
        try {
            return elements.get(1);
        }
        catch (IndexOutOfBoundsException exception) {
            throw new EmptyQueueException("The priority queue is empty!");
        }
    }

    // make iterable

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int elementIndex = 1; elementIndex < (elements.size() - 1); elementIndex++) {
            stringBuilder.append(elements.get(elementIndex));
            stringBuilder.append(" ");
        }
        stringBuilder.append(elements.get(elements.size() - 1));

        return stringBuilder.toString();
    }

    public boolean isEmpty() {
        return elements.size() < 2;
    }

    private int getLeftChildIndex(int parentIndex) {
        return parentIndex * 2;
    }

    private int getRightChildIndex(int parentIndex) {
        return (parentIndex * 2) + 1;
    }

    private int getParentIndex(int childIndex) {
        return childIndex / 2;
    }

    private void percolateElementUp(int initialIndex) {
        int currentIndex = initialIndex;
        int parentIndex = getParentIndex(currentIndex);

        while (parentIndex >= 1) {
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

    private void percolateElementDown(int initialIndex) {
        int currentIndex = initialIndex;
        int leftChildIndex = getLeftChildIndex(currentIndex);
        int rightChildIndex = getRightChildIndex(currentIndex);

        while (leftChildIndex <= (elements.size() - 1)) {
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
