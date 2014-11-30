/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This implementation of Full Semi Splay rebalances the original Tops.
 * @author Laurens Martin
 */
public class Full extends AbstractSplayTree {

    public Full() {
        super();
    }

    /**
     * @return A new and complete deep-copy of the current tree. This method
     * must be implemented first, in order to use Toppie!
     */
    @Override
    public AbstractTree copy() {
        return new Full(this);
    }

    /**
     * Clone the given tree.
     */
    private Full(Full clone) {
        super();
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
        ArrayList<Top> nodes = new ArrayList<>();
        Top parent = path.pop();
        nodes.add(parent);
        LinkedList<Top> subtree = new LinkedList<>();

        // add exterior nodes in natural order to a deque
        subtree.add(parent.getLeft());
        subtree.add(parent.getRight());
        while (!path.isEmpty()) {
            Top child = parent;
            parent = path.pop();
            nodes.add(parent);
            if (parent.getLeft() != child) {
                subtree.addFirst(parent.getLeft());
            } else {
                subtree.add(parent.getRight());
            }
        }

        // balance the path
        Collections.sort(nodes, new Comparator<Top>() {

            public int compare(Top t1, Top t2) {
                return t1.compareTo(t2.getKey());
            }
        });
        root = balancePath(nodes, leaves);

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
    private Top balancePath(List<Top> nodes, ArrayList<Top> leaves) {
        if (nodes.isEmpty()) {
            return null;
        } else {
            int middle = nodes.size() / 2;
            Top top = nodes.get(middle);
            top.setLeft(balancePath(nodes.subList(0, middle), leaves));
            top.setRight(balancePath(nodes.subList(middle + 1, nodes.size()), leaves));
            if (!top.hasRight() || !top.hasLeft()) {
                leaves.add(top);
            }
            return top;
        }
    }
}
