package mandelbrot;

/**
 * Generic wrapper class that takes in any data type and then forms a DoublyLinked node that
 * can be traversed forwards or backwards. Used within the Queue and Stack objects to form
 * dynamic data structures.
 * */

public class Node<T> {

    /**Generic data type*/
    private T data;

    /**References to connected nodes*/
    private Node<T> next;
    private Node<T> previous;

    /**Constructs a null node*/
    public Node() {

        this.data = null;

    }

    /**Constructs a node with the data specified*/
    public Node(T data) {

        this.data = data;

    }

    public T getData() {

        return data;

    }

    public void setData(T data) {

        this.data = data;

    }

    public Node<T> getNext() {

        return next;

    }

    public void setNext(Node<T> next) {

        this.next = next;

    }

    public Node<T> getPrevious() {

        return previous;

    }

    public void setPrevious(Node<T> previous) {

        this.previous = previous;

    }

}
