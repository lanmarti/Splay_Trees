/*
 The purpose of this program is to first build a splay-tree
 with a set of keys with values from [0..n-1] in random order.
 Then to do a large series of lookups in this tree.
 The method 'usage' shows how to invoke this program.

 This program combines three different modes of operation.
 The random8020 mode is the default.
 The randomZIPF mode is enabled by the option "-z".
 The quotient mode is enabled by the "-q topmost/percent" option.

 For example to invoke the program for random8020 do this:

 java -cp . semisplay.SplayQuery -o 8 1000000 20000000

 This inserts 1000000 keys into a default tree in random order,
 then does 20000000 random lookups. Here the parameter 8 is an
 argument to option "-o" and denotes the "ongelijkmatigheid".
 This determines the degree of imbalance in the random lookups.
 "-o 1" is the minimum value, which here means that all
 numbers have about an equal chance  for being looked up.
 "-o 8" means that the 20% of the keys which are looked up most
 will receive approximately 80% of the lookups.
 The option "-o 30" would lead to the 10% most visited keys
 to receiving approximately 90% of the lookups.
 The key values are chosen randomly. That is, frequency of
 lookup is independent of key value.

 The dryrun option "-d" shows by percentage how often a lookup
 occurs. It is recommended to examine the output of this option
 before deciding on actual measurements.

 The randomZIPF mode has a more advanced interpretation of biasedness.
 This mode is enabled by the "-z" option. The "-o" parameter works
 somewhat differently. Here the "-o 0" means that all key values have
 about equal chance of being looked up.
 "-o 1" is already very strongly biased.
 Again use the "-d" option first to see what is going on.
 Note that the argument to "-o" is a floating point number.

 A third mode is the quotient mode. This is a very simple method.
 Maybe too simple, because it is unrealistic. No application will
 ever exhibit this behavior. The option "-q topmost/percent" enables
 a mode in which the "topmost"% of the keys receive "percent"% of the
 lookups. Hence the "100 - topmost"% keys receive "100 - percent"%
 of the lookups. Apart from this the distribution is evenly spread.
 Please note that whenever we speak of topmost we mean frequency of lookup.
 That is, a key is topmost if it receives more lookups than other keys.

 This program is an adapted Java version of the C-programs
 makenumbers_random8020.c and makenumbers_random_zipf_vary.c.
 More information about the ideas and parameters can be found
 in the paper "Rehabilitation of an unloved child: semi-splaying"
 a copy of which can be found in the project map on Minerva.
 Feel free to consult with AD2@ugent.be for further information.
 */
package semisplay;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Run long performance tests on splay trees.
 */
public class SplayQuery {
    // long for insertion time
    private long timer;

    // The random number generator.
    private final ThreadLocalRandom rand;

    // Random generator seed.
    private long seed;

    // If we only need to examine the lookup chances by percentage.
    private boolean dryrun;

    // The number of keys to insert into the tree.
    private int inserts;

    // The number of lookup operations to do.
    private int lookups;

    // Should we use the ZIPF method?
    private boolean zipf;

    // The number of re-shuffles when using ZIPF
    private int partitions;

    // Should we use the quotient method?
    private boolean useQuotient;
    private double topmostQuotient;
    private double percentQuotient;

    /**
     * The degree of biasedness.
     * For 8020 this is in the range [1,infinity].
     * For ZIPF this is in the range [0,1].
     */
    private double ongelijkmatigheid;

    // The name of the AbstractTree class to use.
    private String splayClassName = "SemiSplay";

    public SplayQuery() {
        // ThreadLocalRandom is very fast.
        rand = ThreadLocalRandom.current();
    }
    
    public String runQuery(String args[]){
        setArgs(args).run();
        return getDataAndClear();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SplayQuery().setArgs(args).run();
    }

    /**
     * Tell the user which options we support.
     * @param s The unknown option if not-null.
     */
    public void usage(String s) {
        if (s != null) {
            System.err.println("Invalid option " + s);
        }
        System.err.printf("Usage: %s [options] inserts lookups\n",
                          this.getClass().getName());
        System.err.println("Where options include:");
        System.err.println("\t-c class: Which tree class to use.");
        System.err.println("\t-d: Dryrun; show elaborate biasedness details.");
        System.err.println("\t-o ongelijkmatigheid: in the range [1,-] or [0,1].");
        System.err.println("\t-p partities: number of reshuffles with ZIPF");
        System.err.println("\t-q topmost/percentage: use quotient method.");
        System.err.println("\t-s seed: Use seed to prime the randoms.");
        System.err.println("\t-z: Use the ZIPF method, instead of 8020.");
        System.err.println("\"inserts\": the number of insert operations.");
        System.err.println("\"lookups\": the number of lookup operations.");
        System.exit(1);
    }

    /**
     * Process all commandline arguments and options.
     * @param args Array of commandline arguments
     * @return The current object.
     */
    public SplayQuery setArgs(String[] args) {
        int i = 0;
        try {
            for (; i < args.length; ++i) {
                String a = args[i];
                if (a.charAt(0) != '-') {
                    break;
                }
                System.out.printf("Processing argument %d: %s\n", i, a);
                switch (a) {
                    case "-c":
                        splayClassName = args[++i];
                        break;
                    case "-d":
                        dryrun = true;
                        break;
                    case "-o":
                        ongelijkmatigheid = Double.parseDouble(args[++i]);
                        break;
                    case "-p":
                        partitions = Integer.parseInt(args[++i]);
                        break;
                    case "-q":
                        try {
                            String qs[] = args[++i].split("/");
                            topmostQuotient = Double.parseDouble(qs[0]);
                            percentQuotient = Double.parseDouble(qs[1]);
                        }
                        catch (Exception e) {
                        }
                        if (topmostQuotient <= 0 || topmostQuotient >= 100 ||
                            percentQuotient <= 0 || percentQuotient >= 100) {
                            System.err.printf("Invalid quotient argument %s\n",
                                              args[i]);
                            System.exit(1);
                        } else {
                            useQuotient = true;
                        }
                        break;
                    case "-s":
                        seed = Long.parseLong(args[++i]);
                        break;
                    case "-z":
                        zipf = true;
                        break;
                    default:
                        usage(a);
                }
            }
            if (i + 2 <= args.length) {
                inserts = Integer.parseInt(args[i++]);
                lookups = Integer.parseInt(args[i++]);
            } else {
                usage(null);
            }
            if (i < args.length) {
                usage(args[i]);
            }
        }
        catch (NumberFormatException ex) {
            System.err.printf("Invalid %dth argument: %s\n", i, args[i]);
            System.exit(1);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.printf("Missing option argument at position %d\n",
                              i);
            System.exit(1);
        }

        if (zipf && useQuotient) {
            System.err.println("Conflicting options -z and -q.");
            System.exit(1);
        }

        return this;
    }

    private void initRandom() {

        if (seed > 0) {
            // ThreadLocalRandom doesn't support seeds!
            // Fake a seed by ignoring some prefix of numbers.
            int max = (int) (seed % 997);
            for (int i = 0; i < max; ++i) {
                rand.nextInt();
            }
        }

    }

    /**
     * Create biases for the ZIPF method according to "ongelijkmatigheid".
     * @return an array of biases.
     */
    private double[] initZipf() {
        double C = 0;

        for (int i = 1; i <= inserts; ++i) {
            C += 1.0 / Math.pow((double) i, ongelijkmatigheid);
        }

        C = 1.0 / C;

        double[] kans = new double[inserts + 1];
        kans[0] = 1.0;
        for (int i = 1; i < kans.length; ++i) {
            double dbuffer = C / Math.pow((double) i, ongelijkmatigheid);
            kans[i] = kans[i - 1] - dbuffer;
        }

        return kans;
    }

    /**
     * Generate an array of keys and permute them.
     * @return The array of keys
     */
    private Key[] generateKeys() {
        Key[] num = new Key[inserts];
        for (int i = 0; i < num.length; ++i) {
            num[i] = new Key(i);
        }

        /* Rearrange all keys randomly. */
        shuffle(num);

        return num;
    }

    /**
     * Load the tree class, insert the keys and do the lookups.
     */
    public void run() {
        System.out.printf("Run %d, %d, %d\n", inserts, lookups, seed);

        // get our (semi-)splay variant.
        AbstractTree tree = loadClass(splayClassName);
        initRandom();

        // The ZIPF method uses an array of biases.
        double[] kans = zipf ? initZipf() : null;

        Key[] num = generateKeys();
        System.gc();
        long beginInsertions = System.currentTimeMillis();
        // insert all keys into the tree
        for (int i = 0; i < num.length; ++i) {
            tree.insert(num[i]);
        }
        long endInsertions = System.currentTimeMillis();
        timer=endInsertions-beginInsertions;

        /* Again, rearrange all keys randomly. */
        shuffle(num);

        if (zipf) {
            runZipf(tree, num, kans);
        }
        else if (useQuotient) {
            runQuotient(tree, num);
        } else {
            run8020(tree, num);
        }
    }

    /**
     * Do lookups according to the 80/20 method.
     * @param tree The tree
     * @param num An array of inserted keys.
     */
    private void run8020(AbstractTree tree, Key[] num) {
        if (dryrun) {
            int percent = 100;
            int[] use = new int[percent];
            int all = 0;
            for (int i = 0; i < lookups; ++i) {
                double d = rand.nextDouble();
                d = Math.pow(d, 1.0 / ongelijkmatigheid);
                int positie = (int) Math.floor(d * inserts);
                int index = (int) Math.floor(percent * positie / num.length);
                ++use[index];
                ++all;
            }
            double cumu = 0.0;
            for (int i = 0; i < percent; ++i) {
                double perc = (double) percent * use[i] / all;
                cumu += perc;
                System.err.printf("Van %3d tot %3d %% wordt %10d keren "
                                  + "= %5.2f %% bezocht, "
                                  + "cumulatief %6.2f %%.\n",
                                  i, i + 1, use[i], perc, cumu);
            }
        } else {
            for (int i = 0; i < lookups; ++i) {
                double d = rand.nextDouble();
                d = Math.pow(d, 1.0 / ongelijkmatigheid);
                int positie = (int) Math.floor(d * inserts);
                boolean lookup = tree.lookup(num[positie]);
                if (lookup != true) {
                    System.err.printf("Lookup %d failed\n", num[positie].get());
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Do lookups according to the ZIPF method
     * @param tree The tree
     * @param num Array of inserted keys
     * @param kans Array of biases.
     */
    private void runZipf(AbstractTree tree, Key[] num, double[] kans) {
        if (dryrun) {
            int percent = 100;
            int[] use = new int[percent];
            int all = 0;
            for (int i = 0; i < lookups; ++i) {
                double d = rand.nextDouble();
                int positie = getIndex(d, kans, num.length);
                int index = (int) Math.floor(percent * positie / num.length);
                ++use[index];
                ++all;
            }
            double cumu = 0.0;
            for (int i = 0; i < percent; ++i) {
                double perc = (double) percent * use[i] / all;
                cumu += perc;
                System.err.printf("Van %3d tot %3d %% wordt %10d keren "
                                  + "= %5.2f %% bezocht, "
                                  + "cumulatief %6.2f %%.\n",
                                  i, i + 1, use[i], perc, cumu);
            }
        } else {
            int partSize = partitions > 1
                           ? (lookups + (partitions - 1)) / partitions
                           : lookups;
            int shuffleAt = partSize;
            for (int i = 0; i < lookups; ++i) {
                if (i == shuffleAt) {
                    shuffle(num);
                    shuffleAt += partSize;
                }

                double d = rand.nextDouble();
                int positie = getIndex(d, kans, num.length);
                boolean lookup = tree.lookup(num[positie]);
                if (lookup != true) {
                    System.err.printf("Lookup %d failed\n", num[positie].get());
                    System.exit(1);
                }
            }
        }
    }

    /**
     * The ZIPF method uses some tricky and misty details.
     * @param d Unbiased index
     * @param kans Array of chances
     * @param size Number of elements
     * @return The skewed index
     */
    public int getIndex(double d, double[] kans, int size) {
        boolean found = false;
        int left = 1, right = size, index = size / 2;

        while (!found) {
            if (kans[index] >= d) {
                left = index;
                index = (left + right + 1) / 2;
            } else if (kans[index - 1] < d) {
                right = index;
                index = (left + right) / 2;
            } else {
                // kans[index-1]>=d en kans[index]<d
                found = true;
            }
        }

        return index - 1;
    }

    /**
     * Do lookups according to the quotient method.
     * @param tree The tree
     * @param num An array of inserted keys.
     */
    private void runQuotient(AbstractTree tree, Key[] num) {
        System.err.printf("quotient topmost %6.2f / %6.2f percent lookups\n",
                          topmostQuotient, percentQuotient);
        if (dryrun) {
            int percent = 100;
            int[] use = new int[percent];
            int all = 0;
            for (int i = 0; i < lookups; ++i) {
                double d = rand.nextDouble();
                if (d < percentQuotient / 100.0) {
                    d *= topmostQuotient / percentQuotient;
                } else {
                    d -= percentQuotient / 100.0;
                    d *= (100 - topmostQuotient) / (100 - percentQuotient);
                    d += topmostQuotient / 100.0;
                }
                int positie = (int) Math.floor(d * inserts);
                if (positie >= inserts) {
                    System.err.printf("Invalid positie %d\n", positie);
                    System.exit(1);
                }
                int index = (int) Math.floor(percent * positie / num.length);
                ++use[index];
                ++all;
            }
            double cumu = 0.0;
            for (int i = 0; i < percent; ++i) {
                double perc = (double) percent * use[i] / all;
                cumu += perc;
                System.err.printf("Van %3d tot %3d %% wordt %10d keren "
                                  + "= %5.2f %% bezocht, "
                                  + "cumulatief %6.2f %%.\n",
                                  i, i + 1, use[i], perc, cumu);
            }
        } else {
            for (int i = 0; i < lookups; ++i) {
                double d = rand.nextDouble();
                if (d < percentQuotient / 100.0) {
                    d *= topmostQuotient / percentQuotient;
                } else {
                    d -= percentQuotient / 100.0;
                    d *= (100 - topmostQuotient) / (100 - percentQuotient);
                    d += topmostQuotient / 100.0;
                }
                int positie = (int) Math.floor(d * inserts);
                boolean lookup = tree.lookup(num[positie]);
                if (positie >= inserts || !lookup) {
                    System.err.printf("Lookup of %d failed\n", positie);
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Delay the current thread
     * @param millis The delay time in milliseconds
     */
    public void delay(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {
        }
    }

    /**
     * Use the Fisher-Yates shuffle to randomly reorder an array.
     * @param num An array of Keys.
     * @see <a href="https://en.wikipedia.org/wiki/Fisher-Yates_shuffle#The_modern_algorithm">
     *      algorithm details</a>.
     */
    public void shuffle(Key[] num) {
        for (int i = 0; i < num.length; ++i) {
            int doel = rand.nextInt(i, num.length);
            Key tmp = num[i];
            num[i] = num[doel];
            num[doel] = tmp;
        }
    }

    /**
     * Load a tree class by name.
     * @param className The name of the class without package prefix.
     * @return An instance of the specified class.
     * If this fails to load your class then examine the value of your
     * CLASSPATH environment variable, or the -cp option to java.
     */
    private AbstractTree loadClass(String className) {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            String fullClassName = "semisplay." + className;
            // System.out.println("Loading class " + fullClassName);
            Class myClass = classLoader.loadClass(fullClassName);
            // System.out.println("Instantiating " + myClass.getCanonicalName());
            return (AbstractTree) myClass.newInstance();
        }
        catch (ClassNotFoundException |
               InstantiationException |
               IllegalAccessException ex) {
            System.err.printf("Failed to load class %s: %s\n",
                              className, ex.getLocalizedMessage());
            System.exit(1);
        }
        return null;
    }

    public String getDataAndClear(){
        String out = ";"+timer+";"+AbstractTree.getInsertions()+";"
                +AbstractTree.getConstructed()+";"+AbstractTree.getComparisons();
        AbstractTree.resetStatistics();
        return out;
    }
}
