// Written by Christopher Taliaferro

package Prioritetsko;

class Element<E extends Comparable<E>> {
    public E value;
    public int status;
    public int priority;

    public Element(E value, int status, int priority) {
        this.value = value;
        this.status = status;
        this.priority = priority;
    }
}