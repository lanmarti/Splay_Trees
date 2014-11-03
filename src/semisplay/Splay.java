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

    @Override
    public AbstractTree copy() {
        return new Splay(this);
    }

    // HERWERK DIT toppen klonen!
    private Splay(Splay clone) {
        this.root = clone.getRoot();
        this.size = clone.getSize();
        this.limit = clone.limit;
    }

    @Override
    public TopStack splay(TopStack path) {
        Top top;
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
            // identiek aan semisplay methode
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
                path.pop();
                path.pop();
                path.swap(top);
            }
        }
        return path;
    }
}