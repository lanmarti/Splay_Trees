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
 *
 * @author Laurens Martin
 */
public class Full extends AbstractSplayTree {
    private final ArrayList<Top> leaves = new ArrayList();

    public Full() {
    }

    @Override
    public AbstractTree copy() {
        return new Full(this);
    }

    private Full(Full clone) {
        if (clone.getRoot() != null) {
            root = clone.getRoot().copy();
        }
        size = clone.getSize();
        limit = clone.limit;
    }

    @Override
    public TopStack splay(TopStack path) {
        if (!path.hasGrandParent()) {
            path.clear();
            return path;
        }
        ArrayList<Key> values = new ArrayList<>();
        Top parent = path.pop();
        values.add(parent.getKey());
        LinkedList<Top> subtree = new LinkedList<>();
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

        // balance
        Collections.sort(values);
        root = balancePath(values);
        
        // plak buitenbomen
        Iterator it = subtree.iterator();
        for (Top leaf : leaves) {
            if (!leaf.hasLeft()) {
                leaf.setLeft((Top) it.next());
            }
            if (!leaf.hasRight()) {
                leaf.setRight((Top) it.next());
            }
        }

        leaves.clear();
        path.clear();
        return path;
    }
    
    private Top balancePath(List<Key> values){
        if (values.isEmpty()) {
            return null;
        } else {
            int middle = values.size() / 2;
            Top top = new Top(values.get(middle));
            top.setLeft(balancePath(values.subList(0, middle)));
            top.setRight(balancePath(values.subList(middle + 1, values.size())));
            if (!top.hasRight() || !top.hasLeft()){
                leaves.add(top);
            }
            return top;
        }
    }

}
