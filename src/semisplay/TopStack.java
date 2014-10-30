
package semisplay;

import java.util.ArrayList;

/**
 * A utility class for conveniently traversing semisplay trees.
 */
public class TopStack extends ArrayList<Top> {

    /**
     * Retrieve (non-destructively) the element which is deepest in the stack.
     * @return The bottommost element.
     */
    public Top bottom() {
        return get(0);
    }

    /**
     * Push top onto the stack.
     * @param top The element to push onto the stack.
     */
    public void push(Top top) {
        add(top);
    }

    /**
     * Remove and return the topmost element.
     * @return the previously topmost element
     */
    public Top pop() {
        return remove(size() - 1);
    }

    /**
     * @return Retrieve (non-destructively, read-only) the topmost element.
     */
    public Top peek() {
        return get(size() - 1);
    }

    /**
     * @return Whether the stack has at least one element.
     */
    public boolean nonEmpty() {
        return ! isEmpty();
    }

    /**
     * @return The length of the path (is number of elements minus 1).
     */
    public int length() {
        return size() - 1;
    }

    /**
     * Replace the topmost value of the stack with a new one.
     * @param top The new topmost element.
     * @return The previous topmost element.
     */
    public Top swap(Top top) {
        int last = size() - 1;
        Top old = get(last);
        set(last, top);
        return old;
    }

    /**
     * Replace the element at the given position in the stack.
     * @param index The position where the replacement should occur.
     * @param top The new element to be placed at that position.
     * @return The previous element from that position.
     */
    public Top replace(int index, Top top) {
        Top old = get(index);
        set(index, top);
        return old;
    }

    /**
     * @return Whether the stack contains at least one child + parent.
     */
    public boolean hasParent() {
        return size() >= 2;
    }

    /**
     * @return The parent node for the topmost element.
     */
    public Top getParent() {
        return get(size() - 2);
    }

    /**
     * @return Whether the stack contains a child + parent + grandparent.
     */
    public boolean hasGrandParent() {
        return size() >= 3;
    }

    /**
     * @return The grandparent node for the topmost element.
     */
    public Top getGrandParent() {
        return get(size() - 3);
    }
}
