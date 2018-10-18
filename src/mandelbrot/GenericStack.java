package mandelbrot;

/**
 * Dynamic and generic doubly linked stack that uses nodes to implement a LIFO
 * data structure. Implements the GenericDataStructure interface to ensure that the stack
 * has the appropriate methods.
 * @param <T>
 */

public class GenericStack<T> implements GenericDataStructure<T> {

    /**DoublyLinkedNodes that reference the bottom and top of the stack*/
    private DoublyLinkedNode<T> bottom;
    private DoublyLinkedNode<T> top;


    /**Constructs an empty stack*/
    public GenericStack() {

        this.bottom = null;
        this.top = null;

    }

    /**Constructs a stack containing a single node*/
    public GenericStack(DoublyLinkedNode<T> item) {

        this.bottom = item;
        this.top = item;

    }

    /**Adds a node to the top of the stack*/
    public void add(T item) {

        DoublyLinkedNode<T> node = new DoublyLinkedNode<>(item);
        if (this.isEmpty()) {

            bottom = node;

        }
        else {

            top.setNext(node);
            node.setPrevious(top);

        }
        top = node;

    }

    /**Removes and returns the top of the stack*/
    public T remove() {

        if (!isEmpty()) {
            T output = top.getData();
            top = top.getPrevious();
            if (top == null) bottom = null;
            else top.setNext(null);
            return output;
        }
        return null;

    }

    /**Checks whether the stack contains any nodes*/
    public boolean isEmpty() {

        return bottom == null;

    }

}
