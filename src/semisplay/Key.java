
package semisplay;

/**
 * A simple key class for Top.
 */
public final class Key implements Comparable<Key> {

    private final long key;

    public Key(long value) {
        key = value;
    }

    public Key(String s) throws NumberFormatException {
        key = Long.parseLong(s);
    }

    public long get() { return key; }

    @Override
    public String toString() { return Long.toString(key); }

    public static final int LT = -1;
    public static final int EQ = 0;
    public static final int GT = 1;

    @Override
    public int compareTo(Key o) {
        return key < o.key ? LT : key > o.key ? GT : EQ;
    }
}
