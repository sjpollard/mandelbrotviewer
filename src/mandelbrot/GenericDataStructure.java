package mandelbrot;

/**
 * Interface used by the generic data structures to ensure they contain the proper methods
 * that are necessary for a dynamic data structure to function. As the inner workings of the data
 * structures are different, they implement them in different ways.
 *@param <T>
 */

public interface GenericDataStructure<T> {

    /**Adds T to the data structure in the correct place*/
    void add(T item);

    /**Removes the correct T from wherever it should be removed and returns it*/
    T remove();

    /**Checks whether the data structure is empty of items*/
    boolean isEmpty();

}
