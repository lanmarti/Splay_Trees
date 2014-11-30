/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semisplay;

import static java.lang.Math.floor;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Laurens Martin
 */
public abstract class AbstractSplayTree extends AbstractTree {

    protected Top root = null;
    protected int limit = -1;
    protected int size = 0;

    /**
     * @return A reference to the root node of the tree.
     */
    @Override
    public Top getRoot() {
        return root;
    }

    @Override
    public void setRoot(Top root) {
        this.root = root;
    }

    /**
     * Set a limitation on the number of splay operations which are done for
     * each lookup/insert/remove operation.
     *
     * @param limit Limit splays if >= 0, unlimited splays if negative. By
     * default the number of splay operations is not limited (-1).
     */
    @Override
    public void setSplayLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public abstract AbstractTree copy();

    /**
     * @return The depth of the tree. utitlize the recursive helpfunction
     * maxDepth to calculate this.
     */
    @Override
    public int getDepth() {
        return maxDepth(root);
    }

    /**
     * @return the depth of the subtree with this top as root.
     */
    private int maxDepth(Top top) {
        if (top == null || (!top.hasLeft() && !top.hasRight())) {
            return 0;
        } else {
            int leftDepth = maxDepth(top.getLeft());
            int rightDepth = maxDepth(top.getRight());
            if (leftDepth > rightDepth) {
                return (leftDepth + 1);
            } else {
                return (rightDepth + 1);
            }
        }
    }

    /**
     * @return The total number of top-nodes in the tree.
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Does the current tree conform to the formal specification of a BST?
     */
    @Override
    public boolean isBinarySearchTree() {
        ArrayList<Key> tree = toArrayList();
        for (int i = 0; i < tree.size() - 1; i++) {
            if (tree.get(i).compareTo(tree.get(i + 1)) != -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether the current tree is a balanced tree.
     */
    @Override
    public boolean isBalanced() {
        if (root != null) {
            int optimal = (int) floor(log(size) / log(2));
            if (getDepth() != optimal) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rebalance the current tree. Use the recursive balanceKeys function to
     * create a balanced tree from sorted values.
     */
    @Override
    public void rebalance() {
        if (!isBalanced()) {
            ArrayList<Key> values = toArrayList();
            root = balanceKeys(values);
        }
    }

    /**
     * Recusively balance the given list of sorted values into a tree
     *
     * @return the middle element of the list.
     */
    private Top balanceKeys(List<Key> values) {
        if (values.isEmpty()) {
            return null;
        } else {
            int middle = values.size() / 2;
            Top top = new Top(values.get(middle));
            top.setLeft(balanceKeys(values.subList(0, middle)));
            top.setRight(balanceKeys(values.subList(middle + 1, values.size())));
            return top;
        }
    }

    /**
     * Give all elements of the tree in natural order (in-order). For a
     * definition see theorem 16.1.5 in the course notes of AD1.
     *
     * @return ArrayList with all keys.
     */
    @Override
    public ArrayList<Key> toArrayList() {
        ArrayList<Key> list = new ArrayList<>();
        for (TreeIterator<Key> it = iterator(); it.hasNext();) {
            list.add(it.next());
        }
        return list;
    }

    /**
     * Return the smallest key in the tree.
     *
     * @return The smallest key if the tree was non-empty, else null.
     */
    @Override
    public Key getSmallest() {
        Top top = root;
        TopStack stack = new TopStack();
        if (top != null) {
            stack.add(top);
            while (top.hasLeft()) {
                top = top.getLeft();
                stack.add(top);
            }
            splayCounter(stack);
            return top.getKey();
        }
        return null;
    }

    /**
     * Return the largest key in the tree.
     *
     * @return The largest key if the tree was non-empty, else null.
     */
    @Override
    public Key getLargest() {
        Top top = root;
        TopStack stack = new TopStack();
        if (top != null) {
            stack.add(top);
            while (top.hasRight()) {
                top = top.getRight();
                stack.add(top);
            }
            splayCounter(stack);
            return top.getKey();
        }
        return null;
    }

    /**
     * Lookup the given Key in the current tree.
     *
     * @return True if and only if the key was found.
     */
    @Override
    public boolean lookup(Key key) {
        TopStack path = getPath(key);
        int found = path.peek().compareTo(key);
        splayCounter(path);
        if (found != 0) {
            incrNotFounds();
            return false;
        }
        return true;
    }

    /**
     * Insert a new Key, but avoid duplicates.
     *
     * @return True if insertion succeeded (the key is not a duplicate).
     */
    @Override
    public boolean insert(Key key) {
        if (root != null) {
            TopStack path = getPath(key);
            int branch = path.peek().compareTo(key);
            if (branch == 0) {
                incrDuplicates();
                splayCounter(path);
                return false;
            } else if (branch == -1) {
                Top insertion = new Top(key);
                path.peek().setRight(insertion);
                path.push(insertion);
            } else {
                Top insertion = new Top(key);
                path.peek().setLeft(insertion);
                path.push(insertion);
            }
            splayCounter(path);
        } else {
            setRoot(new Top(key));
        }
        size++;
        incrInsertions();
        return true;
    }

    /**
     * Remove the given Key from the tree.
     *
     * @return True if and only if the key was found and deleted.
     */
    @Override
    public boolean remove(Key key) {
        TopStack path = getPath(key);
        if (path.peek().compareTo(key) == 0) {
            Top toRemove = path.peek();
            Top replacement = null;
            /* grootste linkerkind */
            if (toRemove.hasLeft()) {
                replacement = toRemove.getLeft();
                while (replacement.hasRight()) {
                    path.add(replacement);
                    replacement = replacement.getRight();
                }
                // pas kinderen van laatste element op het pad aan indien nodig
                if (path.peek() != toRemove) {
                    path.peek().setRight(replacement.getLeft());
                } else {
                    toRemove.setLeft(replacement.getLeft());
                }
            } /* kleinste rechterkind */ else if (toRemove.hasRight()) {
                replacement = toRemove.getRight();
                while (replacement.hasLeft()) {
                    path.add(replacement);
                    replacement = replacement.getLeft();
                }
                // pas kinderen van laatste element op het pad aan indien nodig
                if (path.peek() != toRemove) {
                    path.peek().setLeft(replacement.getRight());
                } else {
                    toRemove.setRight(replacement.getRight());
                }

            } /* Element zonder kinderen(!= wortel) */ else {
                // boom bestaat enkel uit wortel
                if (toRemove == getRoot()) {
                    setRoot(null);
                } // te verwijderen element heeft geen kinderen
                else {
                    path.pop();
                    if (path.peek().isLeft(toRemove)) {
                        path.peek().setLeft(null);
                    } else {
                        path.peek().setRight(null);
                    }
                }
            }
            if (replacement != null) {
                toRemove.setKey(replacement.getKey());
            }
            incrDeletions();
            splayCounter(path);
            return true;
        } else {
            incrNotFounds();
            splayCounter(path);
            return false;
        }
    }

    /**
     * @return A new iterator over the current tree.
     */
    @Override
    public TreeIterator<Key> iterator() {
        return new MyTreeIterator();
    }

    abstract TopStack splay(TopStack path);

    /**
     * Look for a top with the given value in the tree
     *
     * @return if the key can be found in the tree: the path from root to key.
     * If the key can't be found, return the path from root to the hypothetical
     * parent.
     */
    private TopStack getPath(Key key) {
        if (root == null) {
            return null;
        }
        Top top = root;
        TopStack stack = new TopStack();
        boolean complete = false;
        while (!complete) {
            stack.push(top);
            // top > sleutel
            if (top.compareTo(key) == 1) {
                if (top.hasLeft()) {
                    top = top.getLeft();
                } else {
                    complete = true;
                }
            } // top < sleutel
            else if (top.compareTo(key) == -1) {
                if (top.hasRight()) {
                    top = top.getRight();
                } else {
                    complete = true;
                }
            } // top = sleutel
            else {
                complete = true;
            }
        }
        return stack;
    }

    /**
     * Limit the amount of times the splay operation can be performed
     * consecutively. Call this whenever there needs to be splayed.
     *
     * @return the path after splaying operation is complete.
     */
    private TopStack splayCounter(TopStack path) {
        int count = 0;
        while (!path.isEmpty() && count != limit) {
            path = splay(path);
            count++;
        }
        return path;
    }

    /**
     * Interior class for the TreeIterator
     */
    protected class MyTreeIterator implements TreeIterator<Key> {

        protected Top current;
        protected Key lastValue;
        protected TopStack path, splaypath;
        protected int branch;

        protected MyTreeIterator() {
            path = new TopStack();
            splaypath = new TopStack();
            current = null;
            lastValue = getLargest();
            branch = 0;
        }

        @Override
        public void reset() {
//            int i = 0;
//            while (!path.isEmpty()) {
//                System.out.println("stack " + i + ": " + splaypath.pop().getKey().get());
//                i++;
//            }
            splayCounter(splaypath);
            path.clear();
            splaypath.clear();
            current = null;
            lastValue = getLargest();
            branch = 0;
        }

        @Override
        public boolean hasNext() {
            if (root == null) {
                return false;
            } else {
                return current == null || !current.getKey().equals(lastValue);
            }
        }

        @Override
        public Key next() {
            if (root == null) {
                return null;
            }
            if (current == null) {
                current = root;
                while (current.hasLeft()) {
                    path.push(current);
                    splaypath.push(current);
                    current = current.getLeft();
                }
                splaypath.push(current);
            } else if (current.hasRight()){
                current = current.getRight();
                while(current.hasLeft()){
                    path.push(current);
                    splaypath.push(current);
                    current = current.getLeft();
                }
                path.push(current);
                splaypath.push(current);
            } else {
                current = path.pop();
                Top child = splaypath.pop();
                Top parent = splaypath.peek();
                while (!path.isEmpty() && parent.isRight(child)){
                    child = splaypath.pop();
                    parent = splaypath.peek();
                }
            }
            return current.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
