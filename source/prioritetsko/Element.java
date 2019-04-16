// Written by Christopher Taliaferro

package prioritetsko;

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
