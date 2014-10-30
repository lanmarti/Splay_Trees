package semisplay;

/**
 * An unweighted Top class which we will use for all binary search trees.
 * We NEVER want to store a backwards pointer to parent top-nodes. Not-ever.
 *
 * Use an editor setting of 80 columns maximum, replace all tabs with 8 spaces,
 * indentation is always 4 spaces, and remove trailing spaces on file save.
 */
public final class Top implements Comparable<Key> {

    /** Key must be comparable. */
    private Key key;

    /** References to child nodes. */
    private Top left, right;

    public Top(Key key) {
        this.key = key;
        AbstractTree.incrConstructed();
    }

    /** @return the key for this top. */
    public Key getKey() { return key; }

    /** Set the key.
     * @param key The new key. */
    public void setKey(Key key) {
        this.key = key;
        AbstractTree.incrConstructed();
    }

    /** Compare this top with
     * @param key.
     * @return LT, EQ, or GT. */
    @Override
    public int compareTo(Key key) {
        AbstractTree.incrComparisons();
        return this.key.compareTo(key);
    }

    /** Compare this top with
     * @param top.
     * @return LT, EQ, or GT. */
    public int compareTop(Top top) {
        return this.compareTo(top.key);
    }

    /** Return a reference to its left | right child. */
    public Top getLeft() { return left; }
    public Top getRight() { return right; }

    /** Whether it HAS a non-null left | right child. */
    public boolean hasLeft() { return left != null; }
    public boolean hasRight() { return right != null; }

    /** Set the new value for the left | right child. */
    public void setLeft(Top left) { this.left = left; }
    public void setRight(Top right) { this.right = right; }

    /** Whether top is equal to the left | right child. */
    public boolean isLeft(Top top) { return top == left && top != null; }
    public boolean isRight(Top top) { return top == right && top != null; }

    /** Construct a deep-copy of this top. */
    public Top copy() {
        return new Top(this);
    }

    /** Copy constructor for tree cloning. */
    private Top(Top clone) {
        this.key = clone.key;
        if (clone.hasLeft()) { left = clone.getLeft().copy(); }
        if (clone.hasRight()) { right = clone.getRight().copy();}
    }
}
