package mandelbrot;

import java.util.Iterator;
/**
 * Dynamic and generic doubly linked queue that uses nodes to implement a FIFO
 * data structure. Implements the GenericDataStructure interface, ensuring that it has
 * the necessary methods.
 * @param <T>
 */

public class GenericQueue<T> implements GenericDataStructure<T>, Iterable<T>{

    /**DoublyLinkedNodes that reference the two ends of the queue*/
    private DoublyLinkedNode<T> head;
    private DoublyLinkedNode<T> tail;

    /**Constructs an empty queue*/
    public GenericQueue() {

        this.head = null;
        this.tail = null;

    }

    /**Constructs a queue containing a single node*/
    public GenericQueue(DoublyLinkedNode<T> head) {

        this.head = head;
        this.tail = head;

    }

    /**Adds a node onto the end of the queue*/
    public void add(T item) {

        DoublyLinkedNode<T> node = new DoublyLinkedNode<>(item);
        if (this.isEmpty()) {

            head = node;

        }
        else {

            tail.setNext(node);
            node.setPrevious(tail);

        }
        tail = node;

    }

    /**Removes and returns the head of the queue*/
    public T remove() {

        if (!isEmpty()) {
            T output = head.getData();
            head = head.getNext();
            return output;
        }
        return null;

    }

    public T peekHead() {

        return head.getData();

    }

    public T peekTail() {

        return tail.getData();

    }

    /**Checks whether the queue contains any nodes*/
    public boolean isEmpty() {
        
        return head == null;

    }

    /**Returns a generic iterator for this class*/
    @Override
    public Iterator<T> iterator() {

       return new QueueIterator();

    }

    /**Inner class that allows for the Queue to be iterated through*/
    class QueueIterator implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return head != null;
        }

        @Override
        public T next() {
            return GenericQueue.this.remove();
        }

    }

}
