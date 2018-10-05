package mandelbrot;

public interface DoublyLinked<T> {

    void add(T item);

    T remove();

    boolean isEmpty();

}
