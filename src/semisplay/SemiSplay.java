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

    /**
     * @return A new and complete deep-copy of the current tree. This method
     * must be implemented first, in order to use Toppie!
     */
    @Override
    public AbstractTree copy() {
        return new SemiSplay(this);
    }

    public SemiSplay() {
        super();
    }

    /**
     * Clone the given tree.
     */
    private SemiSplay(SemiSplay clone) {
        super();
        if (clone.getRoot() != null) {
            this.root = clone.getRoot().copy();
        }
        this.size = clone.getSize();
        this.limit = clone.limit;
    }

    /**
     * Splay the given path according to the standard semi-splay algorithm.
     *
     * @return path from root to top of splayed subtree or an empty path if the
     * path has less than 3 nodes.
     */
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

        /* adjust root if necessary */
        if (grandparent == root) {
            root = top;
            path.clear();
        } /* adjust grandgrandparent child reference */ else {
            Top ggParent = path.get(path.size() - 4);
            if (ggParent.compareTop(top) == -1) {
                ggParent.setRight(top);
            } else {
                ggParent.setLeft(top);
            }

            /* adjust path */
            path.pop();
            path.pop();
            path.swap(top);
        }
        return path;
    }
}
