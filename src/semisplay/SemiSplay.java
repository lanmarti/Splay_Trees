/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semisplay;

/**
 *
 * @author Laurens Martin
 */
public class SemiSplay extends AbstractSplayTree {

    @Override
    public AbstractTree copy() {
        return new SemiSplay(this);
    }

    public SemiSplay() {
    }

    private SemiSplay(SemiSplay clone) {
        if (clone.getRoot() != null) {
            this.root = clone.getRoot().copy();
        }
        this.size = clone.getSize();
        this.limit = clone.limit;
        // DEPTH
    }

    @Override
    public TopStack splay(TopStack path) {
        Top top;
        if (!path.hasGrandParent()) {
            path.clear();
            return path;
        }
        Top current = path.peek();
        Top parent = path.getParent();
        Top grandparent = path.getGrandParent();

        if (current.compareTop(parent) == -1) {
            /*  situatie 1:
             *          C
             *      B
             *  A
             */
            if (parent.compareTop(grandparent) == -1) {
                grandparent.setLeft(parent.getRight());
                parent.setRight(grandparent);
                top = parent;
            } /* situatie 2:
             *  A
             *      C
             *  B
             */ else {
                grandparent.setRight(current.getLeft());
                parent.setLeft(current.getRight());
                current.setLeft(grandparent);
                current.setRight(parent);
                top = current;
            }
        } else {
            /* situatie 3:
             *      C
             *  A
             *      B
             */
            if (parent.compareTop(grandparent) == -1) {
                parent.setRight(current.getLeft());
                grandparent.setLeft(current.getRight());
                current.setLeft(parent);
                current.setRight(grandparent);
                top = current;
            } /*  situatie 4:
             *  A
             *      B
             *          C
             */ else {
                grandparent.setRight(parent.getLeft());
                parent.setLeft(grandparent);
                top = parent;
            }
        }

        /* aanpassen van root indien nodig */
        if (grandparent == root) {
            root = top;
            path.clear();
        } /* referentie naar de zojuist gesplayede deelboom aanpassen */ else {
            Top ggParent = path.get(path.size() - 4);
            if (ggParent.compareTop(top) == -1) {
                ggParent.setRight(top);
            } else {
                ggParent.setLeft(top);
            }

            /* pad opschonen */
            // System.out.println("Popping first on path: " + path.peek().getKey());
            path.pop();

            // System.out.println("Popping second on path: " + path.peek().getKey());
            path.pop();

            // System.out.println("Swapping third on path: " + path.peek().getKey() + " with new top:" + top.getKey());
            path.swap(top);
        }
        return path;
    }
}
