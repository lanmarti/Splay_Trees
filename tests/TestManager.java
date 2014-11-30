

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
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
        if (args.length > 0) {
            if (null != args[0]) switch (args[0]) {
                case "correctheid":
                    runCorrectnessTest();
                    break;
                case "verdubbeling":
                    runDoublingTest();
                    break;
                case "performance":
                    runPerformanceTest(args);
                    break;
                case "ongelijkheid":
                    runInequalityTest();
                    break;
            }
        } else {
            System.out.println("gebruik een van de volgende argumenten:"
                    + "\n\tcorrectheid"
                    + "\n\talgemeen"
                    + "\n\tperformance ( Splay SemiSplay Full Full_alt )"
                    + "\n\tongelijkheid");
        }
    }

    private static void runCorrectnessTest() {
        System.out.println("Running correctness tests");
        JUnitCore.runClasses(SplayTest.class, SemiSplayTest.class, FullTest.class);
        AbstractTree.resetStatistics();
    }

    private static void runGeneralTest() {
        System.out.println("Running general test");
        new PerformanceTest1().runTests();
        AbstractTree.resetStatistics();
    }

    private static void runPerformanceTest(String args[]) {
        String set = "performance";
        String trees[] = {"Splay", "SemiSplay", "Full", "Full_alt"};
        if (args.length > 1){
            trees = Arrays.copyOfRange(args,1,args.length);
            for ( String tree : trees) {
                if (!"Splay".equals(tree) || !"SemiSplay".equals(tree) ||
                        !"Full".equals(tree) || !"Full_alt".equals(tree)){
                    System.err.println("Ongeldige boomklasse: "+ tree);
                    System.err.println("Kies een of meedere uit: "
                            + "Splay SemiSplay Full Full_alt");
                } 
            }
        }
        String ongelijk[] = {"1", "30"};
        int values[] = {10, 25, 50, 100, 250, 500, 750, 1000};
        int multipliers[] = {1, 5, 10, 25, 50, 75, 100};
        System.out.println("Running performance tests for " + Arrays.toString(trees) );
        runAllQueries(set, trees, ongelijk, values, multipliers);
        AbstractTree.resetStatistics();
    }
    
    private static void runDoublingTest(){
        String set = "Doubling";
        System.out.println("Running second set of performance tests");
        String trees[] = {"Splay", "SemiSplay", "Full", "Full_alt"};
        String ongelijk[] = {"1", "5", "15", "30"};
        int values[] = {1, 2, 4, 8, 16, 32};
        int multipliers[] = {1, 5, 10, 15, 20, 25};
        runAllQueries(set, trees, ongelijk, values, multipliers);
        AbstractTree.resetStatistics();
    }
    
    private static void runInequalityTest(){
        System.out.println("Running inequality tests");
        String set = "Ongelijkheid";
        String trees[] = {"Splay", "SemiSplay", "Full", "Full_alt"};
        String ongelijk[] = {"1", "5", "15", "30"};
        int values[] = { 1, 5, 10, 15, 20, 25 };
        int multipliers[] = { 1, 5, 10, 15, 20, 25 };
        runAllQueries(set, trees, ongelijk, values, multipliers);
        AbstractTree.resetStatistics();
    }

    private static void runAllQueries(String set, String trees[], 
            String ongelijk[], int values[], int multipliers[]) {
        String args[] = {"-o", "0", "-c", "", "", ""};
        File dir = new File(set);
        try{
            dir.mkdir();
        } catch(SecurityException ex) {
            System.err.println("Directory "+ set + " already exists." );
        }
        for (String o : ongelijk) {
            for (String tree : trees) {
                try (PrintWriter writer = new PrintWriter(set + "/" + set +"Test"
                        + "_o" + o + "_" + tree + "_rawData.csv", "UTF-8")) {
                    writer.println(";Insertion time;Lookup time;"
                            + "Insertions;Constructed tops;Comparisons");
                    args[3] = tree;
                    args[1] = o;
                    for (int value : values) {
                        for (int multiplier : multipliers) {
                            args[4] = Integer.toString(value * 1000);
                            args[5] = Integer.toString(value * multiplier * 1000);
                            writer.println("inserts;" + args[4] + ";lookups;" + args[5]);
                            for (int i = 1; i < 6; i++) {
                                writer.println(i + new SplayQuery().runQuery(args));
                            }
                            writer.println();
                        }
                    }
                    writer.close();
                } catch (Exception e) {
                    System.err.println("Writing to file failed: " + e);
                }
            }
        }
    }

}
