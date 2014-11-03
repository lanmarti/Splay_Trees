/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import semisplay.*;
import static org.junit.Assert.*;

/**
 *
 * @author Laurens Martin
 */
public class SemiSplayTest {

    private AbstractSplayTree instance;
    private ArrayList<Key> values;

    public SemiSplayTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new SemiSplay();
        values = new ArrayList();
        values.add(new Key(10));
        values.add(new Key(5));
        values.add(new Key(15));
        values.add(new Key(20));
        values.add(new Key(25));
        values.add(new Key(1));
        values.add(new Key(11));
        values.add(new Key(8));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setRoot method, of class AbstractSplayTree.
     */
    @Test
    public void testSetRoot() {
        System.out.println("setRoot");
        Top root = new Top(values.get(0));
        instance.setRoot(root);
        assertEquals(instance.getRoot(), root);
        instance.remove(values.get(0));
        assertEquals(instance.getRoot(), null);
    }

    /**
     * Test of copy method, of class AbstractSplayTree.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
        for(int i=0;i<6;i++){
            instance.insert(values.get(i));
        }
        AbstractTree copy = instance.copy();
        for (Key value : values) {
            assertEquals(instance.lookup(value),copy.lookup(value));
        }
    }

    /**
     * Test of getDepth method, of class AbstractSplayTree.
     */
    @Test
    public void testGetDepth() {
        System.out.println("getDepth");
        int expResult = 0;
        int result = instance.getDepth();
        assertEquals(expResult, result);
        instance.insert(values.get(0));
        expResult = 0;
        result = instance.getDepth();
        assertEquals(expResult, result);
        instance.insert(values.get(1));
        expResult = 1;
        result = instance.getDepth();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSize method, of class SemiSplay.
     */
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        for (Key value : values) {
            instance.insert(value);
        }
        int expResult = 8;
        int result = instance.getSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of isBinarySearchTree method, of class AbstractSplayTree.
     */
    @Test
    public void testIsBinarySearchTree() {
        System.out.println("isBinarySearchTree");
        for (int i = 0; i < 3; i++) {
            instance.insert(values.get(i));
        }
        boolean expResult = true;
        boolean result = instance.isBinarySearchTree();
        assertEquals(expResult, result);
    }

    /**
     * Test of rebalance method, of class AbstractSplayTree.
     */
    @Test
    public void testRebalance() {
        System.out.println("rebalance");
        for (Key value : values) {
            instance.insert(value);
        }
        instance.rebalance();
        int expResult = 3;
        int result = instance.getDepth();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSmallest method, of class AbstractSplayTree.
     */
    @Test
    public void testGetSmallest() {
        System.out.println("getSmallest");
        for (Key value : values) {
            instance.insert(value);
        }
        Key expResult = values.get(5);
        Key result = instance.getSmallest();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLargest method, of class AbstractSplayTree.
     */
    @Test
    public void testGetLargest() {
        System.out.println("getLargest");
        for (Key value : values) {
            instance.insert(value);
        }
        Key expResult = values.get(4);
        Key result = instance.getLargest();
        assertEquals(expResult, result);
    }

    /**
     * Test of lookup method, of class AbstractSplayTree.
     */
    @Test
    public void testLookup() {
        System.out.println("lookup");
        for (Key value : values) {
            instance.insert(value);
        }
        boolean expResult = true;
        boolean result = instance.lookup(values.get(1));
        assertEquals(expResult, result);
        expResult = false;
        result = instance.lookup(new Key(50));
        assertEquals(expResult, result);
    }

    /**
     * Test of insert method, of class AbstractSplayTree.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        ArrayList<Boolean> result = new ArrayList();
        ArrayList<Boolean> expResult = new ArrayList();
        for (Key value : values) {
            result.add(instance.insert(value));
            expResult.add(true);
        }
        boolean result2 = instance.insert(new Key(1));
        boolean expResult2 = false;
        assertEquals(expResult, result);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of remove method, of class AbstractSplayTree.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        for (int i = 0; i < 8; i++) {
            instance.insert(values.get(i));
        }
        boolean expResult = true;
        boolean result = instance.remove(values.get(2));
        assertEquals(expResult, result);
        expResult = false;
        result = instance.remove(values.get(2));
        assertEquals(expResult, result);
    }
}
