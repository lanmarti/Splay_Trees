import java.io.PrintWriter;
import org.junit.runner.JUnitCore;
import semisplay.AbstractTree;
import semisplay.SplayQuery;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Laurens Martin
 */
public class TestManager {

    public static void main(String[] args) {
        if (args.length > 1) {
            if ("-c".equals(args[1])) {
                runCorrectnessTests();
            }
        }
        runPerformanceTests();
    }

    private static void runCorrectnessTests() {
        System.out.println("Running correctness tests");
        JUnitCore.runClasses(SplayTest.class, SemiSplayTest.class, FullTest.class);
    }

    private static void runPerformanceTests() {
        System.out.println("Running performance tests");
        new PerformanceTest1().runTests();
        AbstractTree.resetStatistics();
        try (PrintWriter writer = new PrintWriter("PerformanceTests_rawData.csv", "UTF-8")) {
            writer.println(";Insertion time;"
                    + "Insertions;Constructed tops;Comparisons");
            runAllQueries(writer, "50000", "50000");
            writer.close();
        } catch (Exception e) {
            System.err.println("Writing to file failed: " + e);
        }
    }

    private static void runAllQueries(PrintWriter writer, String insertions, String lookups) {
        String args[] = {"-c", "Splay", insertions, lookups};
        writer.println(args[1]);
        writer.println(new SplayQuery().runQuery(args));
        writer.println();
        args[1] = "SemiSplay";
        writer.println(args[1]);
        writer.println(new SplayQuery().runQuery(args));
        writer.println();
        args[1] = "Full";
        writer.println(args[1]);
        writer.println(new SplayQuery().runQuery(args));
        writer.println();
    }
}
