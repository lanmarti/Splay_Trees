
package semisplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Companion class for ToppieFXML.fxml.
 */
public class ToppieFXMLCompanion implements Initializable {

    /** The tree which is currently shown. */
    private AbstractTree tree;
    /** Save a little history so we can backup and restore. */
    private final UndoRedo undo = new UndoRedo();

    /** The following fields should all be filled in automatically
     * by JavaFX on application startup (that's why they are public). */
    public VBox rootBox;
    public ComboBox<String> insertBox;
    public ComboBox<String> removeBox;
    public ComboBox<String> classBox;
    public Canvas canvas;
    public ScrollPane scrollCanvas;
    public Label depthLabel;
    public Label sizeLabel;
    public Label binaryLabel;
    public Label balancedLabel;
    public Label largestLabel;
    public Label smallestLabel;
    public Label lookupLabel;
    public TextField lookupField;
    public TextField iterateField;
    public TextField comparisons;
    public TextField constructed;
    public TextField deletions;
    public TextField duplicates;
    public TextField insertions;
    public TextField notfounds;
    public TextField reparents;
    public TextField visits;
    public Slider splayLimit;
    public Slider undoRedo;

    public ToppieFXMLCompanion() {
    }

    /**
     * Implement a mechanism to backup the current tree
     * and restore it later (if desired).
     * Please Note! that for this to work we MUST have
     * implemented the deep-copy operation of trees.
     */
    private class UndoRedo {
        private final LinkedList<AbstractTree> backups = new LinkedList<>();
        private int current = -1;
        private static final int maxBackups = 6;

        /** @return Current index into the history. */
        public int getCurrent() {
            return current;
        }

        /** @return Number of currently backed up trees. */
        public int size() {
            return backups.size();
        }

        /** @param tree Add this tree to the history. */
        public void backup(AbstractTree tree) {
            if ( ! backups.contains(tree) && tree != null) {
                while (backups.size() > current + 1 && backups.size() > 0) {
                    backups.removeLast();
                }
                while (backups.size() + 1 > maxBackups) {
                    backups.removeFirst();
                }
                backups.addLast(tree);
                current += 1;
                assert current + 1 == backups.size();
                updateSlider();
            }
        }

        /** Change our position in the history. */
        public void setCurrent(int newCurrent) {
            if (newCurrent != current && current >= 0) {
                if (newCurrent >= 0 && newCurrent < backups.size()) {
                    current = newCurrent;
                    restore();
                }
            }
        }

        /** Backup to previous state (if possible.) */
        public void undo() {
            if (current > 0) {
                current -= 1;
                restore();
                updateSlider();
            }
        }

        /** Undo the undo! :-0 */
        public void redo() {
            if (current + 1 < backups.size()) {
                current += 1;
                restore();
                updateSlider();
            }
        }

        /** Draw the tree. */
        private void restore() {
            assert current >= 0 && current < backups.size();
            tree = backups.get(current);
            draw();
        }

        /** Update the slider state which is visible in the GUI. */
        private void updateSlider() {
            if (Math.abs(undoRedo.getMax() - backups.size()) > 1e-6) {
                undoRedo.setMin(0 - (backups.size() - 1));
            }
            if (Math.abs(undoRedo.getValue() + (size() - 1) + current) > 1e-6) {
                undoRedo.setValue(current - (size() - 1));
            }
            undoRedo.setDisable(backups.size() < 2);
        }
    }

    /** Show the depth of the tree. */
    public void getDepth(ActionEvent ev) {
        depthLabel.setText(String.format("%d", tree.getDepth()));
    }

    /** Show the number of nodes in the tree. */
    public void getSize(ActionEvent ev) {
        sizeLabel.setText(String.format("%d", tree.getSize()));
    }

    /** Show the largest element in the tree. */
    public void getLargest(ActionEvent ev) {
        String s;
        if (tree == null) {
            s = "empty";
        } else {
            try {
                Key k = tree.getLargest();
                s = k.toString();
                draw();
            } catch (Exception ex) {
                System.err.println("Exception " + ex);
                s = "Exception";
            }
        }
        largestLabel.setText(s);
    }

    /** Show the smallest element in the tree. */
    public void getSmallest(ActionEvent ev) {
        String s;
        if (tree == null) {
            s = "empty";
        } else {
            try {
                Key k = tree.getSmallest();
                s = k.toString();
                draw();
            } catch (Exception ex) {
                System.err.println("Exception " + ex);
                s = "Exception";
            }
        }
        smallestLabel.setText(s);
    }

    /** Show whether this tree is a binary search tree. */
    public void isBinary(ActionEvent ev) {
        String s;
        if (tree == null) {
            s = "empty";
        } else {
            try {
                boolean b = tree.isBinarySearchTree();
                s = Boolean.toString(b);
            } catch (Exception ex) {
                System.err.println("Exception " + ex);
                s = "Exception";
            }
        }
        binaryLabel.setText(s);
    }

    /** Show whether this tree is perfectly balanced. */
    public void isBalanced(ActionEvent ev) {
        String s;
        if (tree == null) {
            s = "empty";
        } else {
            try {
                boolean b = tree.isBalanced();
                s = Boolean.toString(b);
            } catch (Exception ex) {
                System.err.println("Exception " + ex);
                s = "Exception";
            }
        }
        balancedLabel.setText(s);
    }

    /** Show whether a particular element is present in the tree. */
    public void lookup(ActionEvent ev) {
        String text = lookupField.getText().trim();
        String s;
        if (tree == null || text.length() < 1) {
            s = "empty";
        } else {
            try {
                AbstractTree copy = tree.copy();
                Key key = new Key(text);
                boolean b = copy.lookup(key);
                s = Boolean.toString(b);
                undo.backup(copy);
                tree = copy;
                draw();
            } catch (NumberFormatException ex) {
                System.err.println("Number? " + ex);
                s = "Number?";
            } catch (Exception ex) {
                System.err.println("Exception " + ex);
                s = "Exception";
            }
        }
        lookupLabel.setText(s);
    }

    /** Iterate over all elements in the tree.
     * The optional until field limits the iteration. */
    public void iterateUntil(ActionEvent ev) {
        if (tree != null) {
            String text = iterateField.getText().trim();
            Key key = text.isEmpty() ? null : new Key(text);
            if (key != null) {
                AbstractTree copy = tree.copy();
                undo.backup(copy);
                tree = copy;
            }
            boolean found = false;
            TreeIterator<Key> it = tree.iterator();
            while (it.hasNext()) {
                Key k = it.next();
                System.out.printf("%s ", k.toString());
                if (key != null && k.compareTo(key) == Key.EQ) {
                    found = true;
                    System.out.println("reset.");
                    it.reset();
                    draw();
                    break;
                }
            }
            if (!found) {
                System.out.println("");
            }
        }
    }

    /** Rebalance the tree to a perfectly balanced binary search tree. */
    public void rebalance(ActionEvent ev) {
        if (tree == null) {
            mesg("The tree is empty.");
        } else {
            AbstractTree copy = tree.copy();
            copy.rebalance();
            undo.backup(copy);
            tree = copy;
            draw();
        }
    }

    /** Update a particular statistic in the GUI. */
    private void upStat(TextField stat, long val) {
        boolean gotPrevious = false;
        long previous = 0;
        try {
            String text = stat.getText();
            if ( ! text.isEmpty()) {
                String spl[] = text.split("\\s+");
                if (spl.length > 0) {
                    previous = Long.decode(spl[0]);
                    gotPrevious = true;
                }
            }
        } catch (Exception ex) {
        }
        if (gotPrevious && previous != val) {
            stat.setText(String.format("%d (%+d)", val, val - previous));
        } else {
            stat.setText(String.format("%d", val));
        }
    }

    /** Update all statistics in the GUI. */
    public void updateStats(ActionEvent ev) {
        upStat(comparisons, AbstractTree.getComparisons());
        upStat(constructed, AbstractTree.getConstructed());
        upStat(deletions, AbstractTree.getDeletions());
        upStat(duplicates, AbstractTree.getDuplicates());
        upStat(insertions, AbstractTree.getInsertions());
        upStat(notfounds, AbstractTree.getNotFounds());
    }

    /** Set all statistics to zero. */
    public void resetStats(ActionEvent ev) {
        AbstractTree.resetStatistics();
        updateStats(ev);
    }

    /** Get out a here! */
    public void fileClose(ActionEvent ev) {
        System.exit(0);
    }

    /** Open a file with numbers to insert into the tree. */
    public void fileOpen(ActionEvent ev) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File for Reading Numbers, One Number per Line");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Files with numbers, one per line", "*"));

        File selectedFile = fileChooser.showOpenDialog(Toppie.theStage);
        if (selectedFile != null) {
            List<Key> list = new ArrayList<>();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(selectedFile));
                String text;

                while ((text = reader.readLine()) != null) {
                    list.add(new Key(text));
                }
            }
            catch (FileNotFoundException e) {
                System.err.printf("File %s: Not Found!\n", selectedFile);
            }
            catch (IOException e) {
                System.err.printf("Error reading from %s.\n", selectedFile);
            }
            finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                }
                catch (IOException e) {
                }
            }

            AbstractTree copy = tree.copy();
            for (Key key : list) {
                copy.insert(key);
            }
            undo.backup(copy);
            tree = copy;
            draw();
        }
    }

    /** Latex header. */
    private String latexPreamble() { return
          "\\documentclass[a4paper, 12pt, oneside]{article}\n"
        + "\n"
        + "\\usepackage{fullpage}\n"
        + "\\usepackage[dutch]{babel}\n"
        + "\\usepackage{tikz}\n"
        + "\\usetikzlibrary{arrows}\n"
        + "\n"
        + "\\begin{document}\n"
        + "\n"
        + "\\definecolor{kleur}{rgb}{0.627, 0.125, 0.941}\n"
        + "\\tikzstyle{top} = [circle, draw=kleur, semithick, solid, font=\\small]\n"
        + "\\tikzstyle{boog} = [->, >=stealth', thick, line cap=round, shorten >=0.9, draw=kleur ]\n"
        + "\n"
        + "\\begin{center}\n"
        + "\\begin{tikzpicture}[grow=down, text=black, level/.style={sibling distance=8cm/#1}]\n"
        + "\n";
    }

    /** Latex trailer. */
    private String latexPostamble() { return
          "\n"
        + "\\end{tikzpicture}\n"
        + "\\end{center}\n"
        + "\n"
        + "\\end{document}\n";
    }

    /** Save one top as Latex. */
    private void topSaveLatex(Top top, int indent, BufferedWriter writer)
            throws IOException {
        String s = new String(new char[indent]).replace("\0", " ");
        if (top != null) {
            writer.write(String.format(s + "child {\n"));
            writer.write(String.format(s + " node[top] {$%s$}\n",
                                       top.getKey().toString()));
            writer.write(String.format(s + " edge from parent[boog]\n"));
            Top l = top.getLeft(), r = top.getRight();
            if (l != null || r != null) {
                topSaveLatex(l, indent + 2, writer);
                topSaveLatex(r, indent + 2, writer);
            }
            writer.write(String.format(s + "}\n"));
        } else {
            writer.write(s + "child { edge from parent[draw=none] }\n");
        }
    }

    /** Save the tree as Latex. Convert the output to PDF
     * with tools like pdflatex or mklatex or something similar. */
    public void fileSaveLatex(ActionEvent ev) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File for Saving as LaTeX");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("LaTeX output file (*.tex)", "*.tex"));

        File selectedFile = fileChooser.showOpenDialog(Toppie.theStage);
        if (selectedFile != null) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(selectedFile));
                writer.write(latexPreamble());
                if (tree != null) {
                    Top root = tree.getRoot();
                    if (root != null) {
                        writer.write(String.format("\\node[top] {$%s$}\n",
                                                   root.getKey().toString()));
                        topSaveLatex(root.getLeft(), 1, writer);
                        topSaveLatex(root.getRight(), 1, writer);
                        writer.write(";\n");
                    }
                }
                writer.write(latexPostamble());
            }
            catch (IOException ex) {
                System.err.printf("Save as LaTeX to %s failed: %s\n",
                                  selectedFile, ex.getLocalizedMessage());
            }
            finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                }
                catch (IOException e) {
                }
            }
        }
    }

    /** Clear the screen. */
    public void editClear(ActionEvent ev) {
        clear();
    }

    /** Reset everything to nothing. */
    public void editReset(ActionEvent ev) {
        try {
            AbstractTree copy = tree.getClass().newInstance();
            undo.backup(copy);
            tree = copy;
            clear();
            resetStats(ev);
        } catch (InstantiationException | IllegalAccessException ex) {
            System.err.printf("Failed to load class %s: %s\n",
                    tree.getClass().getCanonicalName(),
                    ex.getLocalizedMessage());
        }
    }

    /** Activate the most wonderful piece of functionality. */
    public void helpAbout(ActionEvent ev) {
        AboutPopup.showAbout(Toppie.theStage);
    }

    /** Switch to a different tree subclass. */
    private void loadClass(String className) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            String fullClassName = "semisplay." + className;
            System.out.println("Loading class " + fullClassName + " ...");
            Class myClass = classLoader.loadClass(fullClassName);
            System.out.println("Instantiating " + myClass.getCanonicalName());
            AbstractTree newTree = (AbstractTree) myClass.newInstance();
            if (tree != null) {
                Top root = tree.getRoot();
                newTree.setRoot(root);
            }
            tree = newTree;
            undo.backup(tree);
        }
        catch (ClassNotFoundException |
               InstantiationException |
               IllegalAccessException ex) {
            System.err.printf("Failed to load class %s: %s\n",
                              className, ex.getLocalizedMessage());
        }
    }

    /** Switch to a different tree subclass. */
    public void setClass(ActionEvent ev) {
        if (classBox == null) {
            System.err.println("insert is null");
        } else {
            String item = classBox.getSelectionModel().getSelectedItem();
            if (item != null) {
                String trim = item.trim();
                if (trim.length() > 0) {
                    if ( ! classBox.getItems().contains(trim)) {
                        classBox.getItems().add(trim);
                    }
                    loadClass(trim);
                    draw();
                    mesg("Using tree object of class "
                            + tree.getClass().getCanonicalName());
                }
            }
        }
    }

    /** Insert one or more elements into the tree. */
    public void insert(ActionEvent ev) {
        if (insertBox == null) {
            System.err.println("insert is null");
        } else {
            String item = insertBox.getSelectionModel().getSelectedItem();
            if (item != null) {
                String trim = item.trim();
                if (trim.length() > 0) {
                    if ( ! insertBox.getItems().contains(trim)) {
                        insertBox.getItems().add(trim);
                    }
                    String s = null;
                    AbstractTree copy = tree.copy();
                    for (String str : trim.split("\\s+")) {
                        try {
                            copy.insert(new Key(str));
                        }
                        catch (NumberFormatException ex) {
                            s = "Number format exception.";
                        }
                    }
                    undo.backup(copy);
                    tree = copy;
                    draw();
                    if (s != null) {
                        mesg(s);
                    }
                }
            }
        }
    }

    /** Not used. */
    public void delay(int millis) {
        try { Thread.sleep(millis);
        } catch (InterruptedException ex) { }
    }

    /** Remove one or more elements from the tree. */
    public void remove(ActionEvent ev) {
        if (removeBox == null) {
            System.err.println("remove is null");
        } else {
            String item = removeBox.getSelectionModel().getSelectedItem();
            if (item != null) {
                String trim = item.trim();
                if (trim.length() > 0) {
                    if ( ! removeBox.getItems().contains(trim)) {
                        removeBox.getItems().add(trim);
                    }
                    String s = null;
                    AbstractTree copy = tree.copy();
                    for (String str : trim.split("\\s+")) {
                        try {
                            copy.remove(new Key(str));
                        }
                        catch (NumberFormatException ex) {
                            s = "Number format exception.";
                        }
                    }
                    undo.backup(copy);
                    tree = copy;
                    draw();
                    if (s != null) {
                        mesg(s);
                    }
                }
            }
        }
    }

    /** Drawing parameters (colors/fonts/shapes/...). */
    private GraphicsContext gc;

    /** Draw a circle at (x,y) with radius r. */
    private void circle(int x, int y, int r) {
        gc.strokeOval(x - r, y - r, r+r, r+r);
    }

    /* private void rect(int x, int y, int w, int h) {
        gc.strokeRect(x, y, w, h);
    } */

    /** Draw text at position (x,y). */
    private void text(String s, int x, int y) {
        gc.fillText(s, x, y);
    }

    /** Draw an arrow from (x,y) to (p,q). */
    private void arrow(int x, int y, int p, int q) {
        final double dx = p - x;
        final double dy = q - y;
        final double theta = Math.atan2(dy, dx);
        final double phi = Math.toRadians(25);
        final int leg = 8;

        for (int j = 0; j < 2; ++j) {
            double rho = theta + (2 * j - 1) * phi;
            double bx = p - leg * Math.cos(rho);
            double by = q - leg * Math.sin(rho);
            gc.strokeLine(bx, by, p, q);
        }
        gc.strokeLine(x, y, p, q);
    }

    /** Clean the screen. */
    public void clear(ActionEvent ev) {
        if (gc != null && canvas != null) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    /** Clean the screen. */
    private void clear() { clear(null); }

    /** Show a message at the top of the screen. */
    private void mesg(String s) { text(s, 4*s.length(), 20); }

    /** Ensure the canvas has at least size (w,h). */
    private void minSize(int w, int h) {
        int cw = (int) canvas.getWidth();
        int ch = (int) canvas.getHeight();
        if (w > cw) { canvas.setWidth(w); }
        if (h > ch) { canvas.setHeight(h); }
    }

    /** Temporary state for computing where a top should be drawn. */
    private class Node {
        private final Top t;
        private final int x, y;
        private int r;
        private int maxLeft, maxRight;

        public Node(Top t, int x, int y) {
            this.t = t;
            this.y = y;
            this.r = 0;
            this.maxLeft = x;
            this.maxRight = x;
            Node left = null;
            Node right = null;
            if ( ! t.hasLeft()) {
                if (t.hasRight()) {
                    maxRight = x + 15;
                }
            } else {
                left = new Node(t.getLeft(), x, y + 30);
                maxLeft = left.getX() + 15;
                if (t.hasRight()) {
                    maxRight = left.getMaxRight() + 30;
                } else {
                    maxRight = maxLeft;
                }
            }
            if ( t.hasRight()) {
                right = new Node(t.getRight(), maxRight, y + 30);
                maxRight = right.getMaxRight();
            }
            if (t.hasLeft() && t.hasRight()) {
                this.x = (left.getX() + right.getX()) / 2;
            } else if (t.hasLeft()) {
                this.x = left.getX() + 15;
            } else if (t.hasRight()) {
                this.x = right.getX() - 15;
            } else {
                this.x = x;
            }
            draw(left, right);
        }

        public int getMaxLeft() {
            return maxLeft;
        }

        public int getMaxRight() {
            return maxRight;
        }

        private void draw(Node left, Node right) {
            minSize(x + 20, y + 20);
            String s = t.getKey().toString();
            this.r = 5 + 2 * Math.min(3, s.length());
            circle(x, y, r);
            text(s, x, y);
            if (t.hasLeft()) {
                double dx = left.getX() - x;
                double dy = left.getY() - y;
                double theta = Math.atan2(dy, dx);
                arrow((int) Math.ceil(x + r * Math.cos(theta)),
                      (int) Math.ceil(y + r * Math.sin(theta)),
                      (int) Math.floor(left.getX() - left.getR() * Math.cos(theta)),
                      (int) Math.floor(left.getY() - left.getR() * Math.sin(theta)));
            }
            if (t.hasRight()) {
                double dx = right.getX() - x;
                double dy = right.getY() - y;
                double theta = Math.atan2(dy, dx);
                arrow((int) Math.ceil(x + r * Math.cos(theta)),
                      (int) Math.ceil(y + r * Math.sin(theta)),
                      (int) Math.floor(right.getX() - right.getR() * Math.cos(theta)),
                      (int) Math.floor(right.getY() - right.getR() * Math.sin(theta)));
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getR() {
            return r;
        }

    }

    /** Draw a tree to the screen. */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    private void draw() {

        if (canvas == null) {
            System.err.println("draw: canvas is null");
        } else {
            if (gc == null) {
                gc = canvas.getGraphicsContext2D();
                // gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.BLUE);
                gc.setFont(Font.getDefault());

                gc.strokeOval(10, 60, 1, 1);
                //gc.setLineWidth(3);
                gc.setStroke(Color.PURPLE);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
            }
            clear();
            Top r = tree.getRoot();
            if (r != null) {
                new Node(r, 40, 50);
            }
        }
    }

    /** Post-load setup of the GUI. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (classBox != null) {
            String[] allClasses = {"SemiSplay", "Splay", "Independent",
                "Extendended", "Full", "Student"};
            String firstClass = allClasses[0];
            classBox.getItems().addAll(allClasses);
            classBox.setValue(firstClass);
            loadClass(firstClass);
        } else {
            System.err.println("classBox == null");
            tree = new SemiSplay();
            undo.backup(tree);
        }

        if (splayLimit == null) {
            System.err.println("splayLimit == null in initialize");
        } else {
            splayLimit.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {
                            int limit = (int) Math.round(splayLimit.getValue());
                            tree.setSplayLimit(limit);
                        }
                });
        }

        if (undoRedo == null) {
            System.err.println("undoRedo == null in initialize");
        } else {
            undoRedo.valueProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {
                            int val = (int) Math.round(undoRedo.getValue());
                            int current = (undo.size() - 1) + val;
                            assert current >= 0;
                            undo.setCurrent(current);
                    }
                });
        }

        /* Ugly hack to get the input focus where we want it. */
        new Thread( new Task<Void>() {
            @Override
            public Void call() throws Exception {
                /* Note: This is NOT on the FX thread,
                 * so we cannot operate on our GUI state. */
                Thread.sleep(200);
                return null;
            }

            @Override
            public void succeeded() {
                /* Yes, this IS called on FX thread.
                 * Therefore, we now can control our GUI. */
                insertBox.requestFocus();
            }
        }).start();

    }
}
