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

    @Override
    public Top getRoot() {
        return root;
    }

    @Override
    public void setRoot(Top root) {
        this.root = root;
    }

    @Override
    public void setSplayLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public abstract AbstractTree copy();

    @Override
    public int getDepth() {
        return maxDepth(root);
    }

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

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isBinarySearchTree() {
        // controleer de waarden

        ArrayList<Key> tree = toArrayList();
        for (int i = 0; i < tree.size() - 1; i++) {
            if (tree.get(i).compareTo(tree.get(i + 1)) != -1) {
                return false;
            }
        }
        return true;
    }

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

    @Override
    public void rebalance() {
        if (!isBalanced()) {
            ArrayList<Key> values = toArrayList();
            root = balanceKeys(values);
        }
    }

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

    @Override
    public ArrayList<Key> toArrayList() {
        ArrayList<Key> list = new ArrayList<>();
        for (TreeIterator<Key> it = iterator(); it.hasNext();) {
            list.add(it.next());
        }
        return list;
    }

    @Override
    public Key getSmallest() {
        // meest linker kind
        // get root, while (left) take left
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

    @Override
    public Key getLargest() {
        // meest rechterkind
        // get root, while (right) take right
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

    @Override
    public boolean insert(Key key) {
        /* try three */
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

    @Override
    public TreeIterator<Key> iterator() {
        return new MyTreeIterator();
    }

    abstract TopStack splay(TopStack path);

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

    private TopStack splayCounter(TopStack path) {
        int count = 0;
        while (!path.isEmpty() && count != limit) {
            path = splay(path);
            count++;
        }
        return path;
    }

    protected class MyTreeIterator implements TreeIterator<Key> {

        protected Top current;
        protected Key lastValue;
        protected TopStack path;

        protected MyTreeIterator() {
            path = new TopStack();
            current = null;
            lastValue = getLargest();
        }

        @Override
        public void reset() {
            path.clear();
            current = null;
            lastValue = getLargest();
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
                // AANPASSEN!!
                return null;
            }
            if (current == null) {
                current = root;
                while (current.hasLeft()) {
                    path.push(current);
                    current = current.getLeft();
                }
            } else if (current.hasRight()) {
                current = current.getRight();
                while (current.hasLeft()) {
                    path.push(current);
                    current = current.getLeft();
                }
            } else {
                try {
                    current = path.pop();
                } catch (Exception e) {
                }
            }
            return current.getKey();
        }
    }

}
