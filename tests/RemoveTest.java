import java.util.concurrent.ThreadLocalRandom;
import semisplay.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Laurens Martin
 */
public class RemoveTest {

    // The random number generator.
    private final ThreadLocalRandom rand;
    
     public static void main(String args[]) {
         new RemoveTest();
     }
    
    public RemoveTest() {
        // ThreadLocalRandom is very fast.
        rand = ThreadLocalRandom.current();
        runTest("Splay");
        runTest("SemiSplay");
    }
    
    private void runTest(String type){
        int failed = 0;
        AbstractTree tree;
        switch (type) {
            case "Splay":
                tree = new Splay();
                break;
            case "SemiSplay":
                tree = new SemiSplay();
                break;
            default:
                System.out.println("Onbekend type boom: kies Splay of SemiSplay");
                return;
        }
        Key[] keys = generateKeys((int) (Math.pow(2,23)));
        System.gc();
        long beginInsertions = System.currentTimeMillis();
        for(int i=0;i<keys.length;i++){
            tree.insert(keys[i]);
        }
        long endInsertions = System.currentTimeMillis();
        long insertionTimer = endInsertions - beginInsertions;
        shuffle(keys);
        System.gc();
        long beginRemoval = System.currentTimeMillis();
        for(int i=0;i<keys.length;i++){
            if (tree.remove(keys[i]) == false){
                failed++;
            }
        }
        long endRemoval = System.currentTimeMillis();
        long removalTimer = endRemoval - beginRemoval;
        System.out.println("Number of failed removes for " + type + ": " + failed);
        System.out.println("Insertion time: " + insertionTimer + "\tRemoval time: " + removalTimer);
    }

    /**
     * Generate an array of keys and permute them.
     *
     * @return The array of keys
     */
    private Key[] generateKeys(int n) {
        Key[] num = new Key[n];
        for (int i = 0; i < num.length; ++i) {
            num[i] = new Key(i);
        }

        /* Rearrange all keys randomly. */
        shuffle(num);

        return num;
    }

    public void shuffle(Key[] num) {
        for (int i = 0; i < num.length; ++i) {
            int doel = rand.nextInt(i, num.length);
            Key tmp = num[i];
            num[i] = num[doel];
            num[doel] = tmp;
        }
    }
}
