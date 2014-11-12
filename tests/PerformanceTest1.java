import java.io.PrintWriter;
import semisplay.AbstractTree;
import semisplay.Splay;
import semisplay.SemiSplay;
import semisplay.Full;
import semisplay.Key;
import semisplay.NumberReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Laurens Martin
 */
public class PerformanceTest1 {

    public static void main(String[] args) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("PerformanceTest1_raw.csv", "UTF-8");
            writer.println(";Total time;Add time;Contains Time;"
                    + "Insertions;Constructed tops;Comparisons");
            String file = "tests/testfile";
            AbstractTree splay = new Splay();
            AbstractTree semi = new SemiSplay();
            AbstractTree full = new Full();
            writer.println(file);
            writer.println();
            printStatistics(file, splay, writer);
            printStatistics(file, semi, writer);
            printStatistics(file, full, writer);
            writer.close();
        } catch (Exception e) {
            System.err.println("Er ging iets fout bij het testen.");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    
    private static void printStatistics(String file, AbstractTree tree, PrintWriter writer){
        writer.println(tree.getClass());
        long[] results = timeTest(file, tree);
        writer.println(";" + results[0] + ";" + results[1] + ";" + results[2] + 
                ";" + AbstractTree.getInsertions() + ";" + AbstractTree.getConstructed()
                + ";" + AbstractTree.getComparisons());
        AbstractTree.resetStatistics();
        writer.println();
    }
    
    private static long[] timeTest(String file, AbstractTree tree){
        long[] results = new long[3];
        NumberReader reader = new NumberReader(file);
        System.gc();
        long begin = System.currentTimeMillis();
        while (reader.hasNext() && reader.getType() == 't') {
            tree.insert(new Key(reader.next()));
        }
        long pause = System.currentTimeMillis();
        while (reader.hasNext()) {
            tree.lookup(new Key(reader.next()));
        }
        long end = System.currentTimeMillis();
        reader.close();
        results[0] = end - begin;
        results[1] = pause - begin;
        results[2] = end - pause;
        return results;
    }
}
