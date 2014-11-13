/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This implementation of Full Semi Splay creates new Tops when rebalancing.
 * @author Laurens Martin
 */
public class Full_alt extends AbstractSplayTree {

    public Full_alt() {
    }

    /**
     * @return A new and complete deep-copy of the current tree. This method
     * must be implemented first, in order to use Toppie!
     */
    @Override
    public AbstractTree copy() {
        return new Full_alt(this);
    }

    /**
     * Clone the given tree.
     */
    private Full_alt(Full_alt clone) {
        if (clone.getRoot() != null) {
            root = clone.getRoot().copy();
        }
        size = clone.getSize();
        limit = clone.limit;
    }

    /**
     * Splay the given path according to the full semi-splay algorithm. Store
     * the exterior nodes in a linked list, rebalance the path and add the
     * exterior nodes back to the newly balanced path.
     *
     * @return an empty path.
     */
    @Override
    public TopStack splay(TopStack path) {
        if (!path.hasGrandParent()) {
            path.clear();
            return path;
        }
        ArrayList<Top> leaves = new ArrayList();
        ArrayList<Key> values = new ArrayList<>();
        Top parent = path.pop();
        values.add(parent.getKey());
        LinkedList<Top> subtree = new LinkedList<>();

        // add exterior nodes in natural order to a deque
        subtree.add(parent.getLeft());
        subtree.add(parent.getRight());
        while (!path.isEmpty()) {
            Top child = parent;
            parent = path.pop();
            values.add(parent.getKey());
            if (parent.getLeft() != child) {
                subtree.addFirst(parent.getLeft());
            } else {
                subtree.add(parent.getRight());
            }
        }

        // balance the path
        Collections.sort(values);
        root = balancePath(values, leaves);

        // add the exterior nodes back to the balanced path
        Iterator it = subtree.iterator();
        for (Top leaf : leaves) {
            if (!leaf.hasLeft()) {
                leaf.setLeft((Top) it.next());
            }
            if (!leaf.hasRight()) {
                leaf.setRight((Top) it.next());
            }
        }

        path.clear();
        return path;
    }

    /**
     * create a balanced tree out of the given values, add nodes with one or no
     * children to the leaves list for usage in the splay method.
     *
     * @return the root of the balanced tree
     */
    private Top balancePath(List<Key> values, ArrayList<Top> leaves) {
        if (values.isEmpty()) {
            return null;
        } else {
            int middle = values.size() / 2;
            Top top = new Top(values.get(middle));
            top.setLeft(balancePath(values.subList(0, middle), leaves));
            top.setRight(balancePath(values.subList(middle + 1, values.size()), leaves));
            if (!top.hasRight() || !top.hasLeft()) {
                leaves.add(top);
            }
            return top;
        }
    }
}