
package semisplay;

/**
 * Interface for iterable trees.
 * @param <T> The class which is returned by TreeIterator.next().
 */
public interface TreeIterable<T> extends Iterable<T> {

    /**
     * Create a new iterator for iterating over this tree.
     * @return TreeIterator.
     */
    @Override
    public TreeIterator<T> iterator();
}
