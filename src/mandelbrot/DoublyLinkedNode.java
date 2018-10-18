package mandelbrot;

/**
 * Generic wrapper class that takes in any data type and then forms a DoublyLinked node that
 * can be traversed forwards or backwards. Used within the Queue and Stack objects to form
 * dynamic data structures.
 * */

public class DoublyLinkedNode<T> {

    /**Generic data type*/
    private T data;

    /**References to connected nodes*/
    private DoublyLinkedNode<T> next;
    private DoublyLinkedNode<T> previous;

    /**Constructs a null node*/
    public DoublyLinkedNode() {

        this.data = null;

    }

    /**Constructs a node with the data specified*/
    public DoublyLinkedNode(T data) {

        this.data = data;

    }

    public T getData() {

        return data;

    }

    public void setData(T data) {

        this.data = data;

    }

    public DoublyLinkedNode<T> getNext() {

        return next;

    }

    public void setNext(DoublyLinkedNode<T> next) {

        this.next = next;

    }

    public DoublyLinkedNode<T> getPrevious() {

        return previous;

    }

    public void setPrevious(DoublyLinkedNode<T> previous) {

        this.previous = previous;

    }

}
