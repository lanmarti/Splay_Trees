/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semisplay;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Laurens Martin
 */
public class SemiSplayTest {
    
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
        Top root = new Top(new Key(15));
        AbstractSplayTree instance = new SemiSplay();
        instance.setRoot(root);
        assertEquals(instance.getRoot(),root);
    }

    /**
     * Test of setSplayLimit method, of class AbstractSplayTree.
     */
    @Test
    public void testSetSplayLimit() {
        System.out.println("setSplayLimit");
        int limit = 0;
        AbstractSplayTree instance = new SemiSplay();
        instance.setSplayLimit(limit);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copy method, of class AbstractSplayTree.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
        AbstractSplayTree instance = new SemiSplay();
        AbstractTree expResult = null;
        AbstractTree result = instance.copy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDepth method, of class AbstractSplayTree.
     */
    @Test
    public void testGetDepth() {
        System.out.println("getDepth");
        AbstractSplayTree instance = new SemiSplay();
        int expResult = 0;
        int result = instance.getDepth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBinarySearchTree method, of class AbstractSplayTree.
     */
    @Test
    public void testIsBinarySearchTree() {
        System.out.println("isBinarySearchTree");
        AbstractTree instance = new SemiSplay();
        boolean expResult = false;
        boolean result = instance.isBinarySearchTree();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBalanced method, of class AbstractSplayTree.
     */
    @Test
    public void testIsBalanced() {
        System.out.println("isBalanced");
        AbstractTree instance = new SemiSplay();
        boolean expResult = false;
        boolean result = instance.isBalanced();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rebalance method, of class AbstractSplayTree.
     */
    @Test
    public void testRebalance() {
        System.out.println("rebalance");
        AbstractTree instance = new SemiSplay();
        instance.rebalance();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toArrayList method, of class AbstractSplayTree.
     */
    @Test
    public void testToArrayList() {
        System.out.println("toArrayList");
        AbstractTree instance = new SemiSplay();
        ArrayList<Key> expResult = null;
        ArrayList<Key> result = instance.toArrayList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSmallest method, of class AbstractSplayTree.
     */
    @Test
    public void testGetSmallest() {
        System.out.println("getSmallest");
        AbstractTree instance = new SemiSplay();
        Key expResult = null;
        Key result = instance.getSmallest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLargest method, of class AbstractSplayTree.
     */
    @Test
    public void testGetLargest() {
        System.out.println("getLargest");
        AbstractTree instance = new SemiSplay();
        Key expResult = null;
        Key result = instance.getLargest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of lookup method, of class AbstractSplayTree.
     */
    @Test
    public void testLookup() {
        System.out.println("lookup");
        Key key = null;
        AbstractTree instance = new SemiSplay();
        boolean expResult = false;
        boolean result = instance.lookup(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insert method, of class AbstractSplayTree.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        Key key = null;
        AbstractTree instance = new SemiSplay();
        boolean expResult = false;
        boolean result = instance.insert(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class AbstractSplayTree.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Key key = null;
        AbstractTree instance = new SemiSplay();
        boolean expResult = false;
        boolean result = instance.remove(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iterator method, of class AbstractSplayTree.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        AbstractTree instance = new SemiSplay();
        TreeIterator<Key> expResult = null;
        TreeIterator<Key> result = instance.iterator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of splay method, of class AbstractSplayTree.
     */
    @Test
    public void testSplay() {
        System.out.println("splay");
        TopStack path = null;
        AbstractTree instance = new SemiSplay();
        TopStack expResult = null;
        TopStack result = instance.splay(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
}
