
package semisplay;

import java.util.ArrayList;

/**
 * Specification for an abstract tree class for binary search trees
 * (specifically for semi-splay and related BSTs).
 *
 * This class includes the interface for Iterable, so as to be able to use it
 * in for-each clauses. In this class and all subclasses we will be using
 * the provided Top class (unmodified) to represent nodes in the tree.
 *
 * The goal of any implementation should be to reuse as much of the methods
 * when implementing other methods. However, this only applies in sofar we
 * can guarantee optimal performance. We want to minimize fiddling with
 * references to children when possible.
 *
 * Recursion is a very powerful concept, but unfortunately recursion
 * in Java can be rather slow and it brings the danger of stack overflows.
 * If desired, we can always emulate recursion with a stack.
 * To that end, we freely provide the array-based TopStack class.
 *
 * Use an editor setting of 80 columns maximum, replace all tabs with 8 spaces,
 * indentation is always 4 spaces, and remove trailing spaces on file save.
 *
 * We suggest to implement the following methods approximately in the order
 * they are presented here. This may help one to gradually become familiar
 * with the structures and progressively tackle more subtle problems.
 */
public abstract class AbstractTree implements TreeIterable<Key> {

    /** @return A reference to the root node of the tree. */
    public abstract Top getRoot();
    /** @param root Use the given tree as the new tree unmodified. */
    public abstract void setRoot(Top root);

    /** Set a limitation on the number of splay operations
     * which are done for each lookup/insert/remove operation.
     * @param limit Limit splays if >= 0, unlimited splays if negative.
     * By default the number of splay operations is not limited (-1). */
    public abstract void setSplayLimit(int limit);

    /** @return A new and complete deep-copy of the current tree.
     * This method must be implemented first, in order to use Toppie! */
    public abstract AbstractTree copy();

    /** @return The depth of the tree. */
    public abstract int getDepth();
    /** @return The total number of top-nodes in the tree. */
    public abstract int getSize();

    /** Does the current tree conform to the formal specification of a BST? */
    public abstract boolean isBinarySearchTree();

    /** Check whether the current tree is a balanced tree. */
    public abstract boolean isBalanced();
    /** Rebalance the current tree. */
    public abstract void rebalance();

    /** Give all elements of the tree in natural order (in-order).
     *  For a definition see theorem 16.1.5 in the course notes of AD1.
     *  @return ArrayList with all keys. */
    public abstract ArrayList<Key> toArrayList();

    /** Return the smallest key in the tree.
     * @return The smallest key if the tree was non-empty, else null. */
    public abstract Key getSmallest();
    /** Return the largest key in the tree.
     * @return The largest key if the tree was non-empty, else null. */
    public abstract Key getLargest();

    /** Lookup the given Key in the current tree.
     * @return True if and only if the key was found. */
    public abstract boolean lookup(Key key);

    /** Insert a new Key, but avoid duplicates.
     * @return True if insertion succeeded (the key is not a duplicate). */
    public abstract boolean insert(Key key);

    /** Remove the given Key from the tree.
     * @return True if and only if the key was found and deleted. */
    public abstract boolean remove(Key key);

    /** @return A new iterator over the current tree. */
    public abstract TreeIterator<Key> iterator();

    /** Maintain some counters for measurement purposes. */
    private static int insertions, constructed, duplicates,
                       deletions, notFounds, comparisons;
    /** Reset all counters. */
    public static void resetStatistics() {
        insertions = constructed = duplicates =
        deletions = notFounds = comparisons = 0;
    }

    /** Count how many keys have been inserted. */
    public static void incrInsertions() { ++insertions; }
    public static int  getInsertions() { return insertions; }
    /** Count how often a new Top object has been created. */
    public static void incrConstructed() { ++constructed; }
    public static int  getConstructed() { return constructed; }
    /** Count how many duplicate keys have not been inserted. */
    public static void incrDuplicates() { ++duplicates; }
    public static int  getDuplicates() { return duplicates; }
    /** Count how often a top has been deleted. */
    public static void incrDeletions() { ++deletions; }
    public static int  getDeletions() { return deletions; }
    /** Count how often a key lookup or deletion failed .*/
    public static void incrNotFounds() { ++notFounds; }
    public static int  getNotFounds() { return notFounds; }
    /** Count how many times we compared two keys .*/
    public static void incrComparisons() { ++comparisons; }
    public static int  getComparisons() { return comparisons; }

}
