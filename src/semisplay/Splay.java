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
public class Splay extends AbstractSplayTree {

    public Splay() {
    }

    /**
     * @return A new and complete deep-copy of the current tree. This method
     * must be implemented first, in order to use Toppie!
     */
    @Override
    public AbstractTree copy() {
        return new Splay(this);
    }

    /**
     * Clone the given tree.
     */
    private Splay(Splay clone) {
        if (clone.getRoot() != null) {
            this.root = clone.getRoot().copy();
        }
        this.size = clone.getSize();
        this.limit = clone.limit;
    }

    /**
     * Splay the given path according to the splay algorithm.
     *
     * @return path from root to top of splayed subtree.
     */
    @Override
    public TopStack splay(TopStack path) {
        if (!path.hasParent()) {
            path.clear();
            return path;
        }
        Top current = path.peek();
        Top parent = path.getParent();

        // case parent==root:
        if (!path.hasGrandParent()) {
            if (current.compareTop(parent) == -1) {
                parent.setLeft(current.getRight());
                current.setRight(parent);
            } else {
                parent.setRight(current.getLeft());
                current.setLeft(parent);
            }
            root = current;
            path.clear();
        } else {
            Top grandparent = path.getGrandParent();

            if (current.compareTop(parent) == -1) {
                /*  situatie 1:
                 *          C
                 *      B
                 *  A
                 */
                if (parent.compareTop(grandparent) == -1) {
                    parent.setLeft(current.getRight());
                    grandparent.setLeft(parent.getRight());
                    current.setRight(parent);
                    parent.setRight(grandparent);
                } /* situatie 2:
                 *  A
                 *      C
                 *  B
                 */ else {
                    grandparent.setRight(current.getLeft());
                    parent.setLeft(current.getRight());
                    current.setLeft(grandparent);
                    current.setRight(parent);
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
                } /*  situatie 4:
                 *  A
                 *      B
                 *          C
                 */ else {
                    grandparent.setRight(parent.getLeft());
                    parent.setRight(current.getLeft());
                    parent.setLeft(grandparent);
                    current.setLeft(parent);
                }
            }
            /* adjust root if necessary */
            if (grandparent == root) {
                root = current;
                path.clear();
            } /* adjust grandgrandparent child reference */ else {
                Top ggParent = path.get(path.size() - 4);
                if (ggParent.compareTop(current) == -1) {
                    ggParent.setRight(current);
                } else {
                    ggParent.setLeft(current);
                }

                /* adjust path */
                path.pop();
                path.pop();
                path.swap(current);
            }
        }
        return path;
    }
}
