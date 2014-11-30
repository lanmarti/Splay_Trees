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
 *
 * @author Laurens Martin
 */
public class Full_alt extends AbstractSplayTree {


    public Full_alt() {
        super();
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
        super();
        if (clone.getRoot() != null) {
            root = clone.getRoot().copy();
        }
        size = clone.getSize();
        limit = clone.limit;
    }

    /**
     * Splay the given path according to the full semi-splay algorithm. Store
     * the exterior nodes in two lists, rebalance the path and add the
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
        ArrayList<Top> leaves = new ArrayList<>();
        ArrayList<Key> values = new ArrayList<>();
        List<Top> left_subtrees = new ArrayList<>();
        List<Top> right_subtrees = new ArrayList<>();
        
        Top parent = path.pop();
        values.add(parent.getKey());

        // add exterior nodes in natural order to lists
        left_subtrees.add(parent.getLeft());
        right_subtrees.add(parent.getRight());
        while (!path.isEmpty()) {
            Top child = parent;
            parent = path.pop();
            values.add(parent.getKey());
            if (parent.getLeft() != child) {
                left_subtrees.add(parent.getLeft());
            } else {
                right_subtrees.add(parent.getRight());
            }
        }

        // balance the path
        Collections.sort(values);
        root = balancePath(values,leaves);

        // add the exterior nodes back to the balanced path
        int i = left_subtrees.size() - 1;
        int j = 0;
        for (Top leaf : leaves) {
            if (!leaf.hasLeft()) {
                if (i >= 0) {
                    leaf.setLeft((Top) left_subtrees.get(i--));
                } else {
                    leaf.setLeft((Top) right_subtrees.get(j++));
                }
            }
            if (!leaf.hasRight()) {
                if (i >= 0) {
                    leaf.setRight((Top) left_subtrees.get(i--));
                } else {
                    leaf.setRight((Top) right_subtrees.get(j++));
                }
            }
        }

        leaves.clear();
        path.clear();
        return path;
    }

    /**
     * create a balanced tree out of the given values, add nodes with one or no
     * children to the leaves list for usage in the splay method.
     *
     * @return the root of the balanced tree
     */
    private Top balancePath(List<Key> values,List<Top> leaves) {
        if (values.isEmpty()) {
            return null;
        } else {
            int middle = values.size() / 2;
            Top top = new Top(values.get(middle));
            top.setLeft(balancePath(values.subList(0, middle),leaves));
            top.setRight(balancePath(values.subList(middle + 1, values.size()),leaves));
            if (!top.hasRight() || !top.hasLeft()) {
                leaves.add(top);
            }
            return top;
        }
    }
}
