
package semisplay;

import java.util.Iterator;

/**
 * Add a reset function to iterable.
 * @param <T>
 */
public interface TreeIterator<T> extends Iterator<T> {

    /**
     * If the caller of this iterator wishes to <b>prematurely</b>
     * abandon the current iteration then she <b>must</b>
     * call the following reset function in order for the
     * iterator to maintain optimal tree performance properties.
     */
    public void reset();

}
