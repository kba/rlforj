package rlforj.util.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import rlforj.util.HeapNode;
import rlforj.util.SimpleHeap;



/**
 * SimpleHeap Test
 *
 * @author vic
 */
public class SimpleHeapTest {
    // NOTE: JUnit 4 guideline is no longer to use TestCase but use annotations.
    // assert* can be static imported from org.junit.Assert.
    
    private static class A implements HeapNode {
        int a;

        int idx;
        public A(int i)
        {
            a = i;
        }

        public int compareTo(Object o)
        {
            if(this == o) return 0;
            A a2 = (A) o;
            if (a == a2.a)
                return 0;
            else if (a < a2.a)
                return -1;
            else
                return 1; // Explicit comparison handles all values of integers better.
//            return a - a2.a;
        }

        public int getHeapIndex()
        {
            return idx;
        }

        public void setHeapIndex(int heapIndex)
        {
            idx = heapIndex;
        }
    }

    @Test
    public void testIndex() {
        int[] arr = { 4, 5, 3, 7, 8, 1, 2, 20, 14, 100, -1 };

        SimpleHeap<A> h = new SimpleHeap<A>(20);

        for(int i:arr)
        {
            h.add(new A(i));
            assertIndexes(h);
        }

        while(h.size()!=0){
            System.out.println(h.poll().a);
            assertIndexes(h);
        }

        h.clear();
        assertIndexes(h);

        A a1=new A(12);
        A a2=new A(5);
        A a3=new A(10);
        A a4=new A(1);

        h.add(a1);h.add(a2);h.add(a3);h.add(a4);
        assertIndexes(h);

        a2.a = 1000;
        h.adjust(a2);
        assertIndexes(h);

        h.poll();h.poll();h.poll();
        assertEquals(1000, h.poll().a);

        h.add(a1);h.add(a2);h.add(a3);h.add(a4);
        assertIndexes(h);

        a2.a = -1000;
        h.adjust(a2);
        assertIndexes(h);

        assertEquals(-1000, h.poll().a);
    }

    /**
     * Test that SimleHeap behaves like a heap. The top of the heap is always 
     * the same as the first element in a sorted list.
     * @throws Exception
     */
    @Test
    public void testHeapFunctionality() throws Exception
    {
        Random rand = new Random();
        SimpleHeap<A> h = new SimpleHeap<A>(50);
        ArrayList<A> arr = new ArrayList<A>(1000);
        for (int i = 0; i < 1000; i++)
        {
            A a = new A(rand.nextInt());
            h.add(a);
            arr.add(a);
        }
        
        Collections.sort(arr);
        
        for (int i = 0; i < 1000; i++)
        {
            A a1 = h.poll();
            A a2 = arr.remove(0);
            assertEquals("SimpleHeap does not match Array at "+i, a1.a, a2.a);
            
            if (rand.nextInt(100) < 30)
            {
                // Make sure SimpleHeap works in the face of random insertions.
                A a = new A(rand.nextInt());
                h.add(a);
                arr.add(a);
                
                Collections.sort(arr);
            }
            
        }
    }
    
    /** 
     * Test that heap properties are maintained in face of property changes and
     * adjustments.
     * @throws Exception
     */
    @Test
    public void testHeapAdjust() throws Exception
    {
        Random rand = new Random();
        SimpleHeap<A> h = new SimpleHeap<A>(50);
        ArrayList<A> arr = new ArrayList<A>(1000);
        for (int i = 0; i < 1000; i++)
        {
            A a = new A(rand.nextInt());
            h.add(a);
            arr.add(a);
        }
        
        Collections.sort(arr);
        
        for (int i = 0; i < 2000; i++)
        {
            A a1 = h.poll();
            A a2 = arr.remove(0);

            assertEquals("SimpleHeap does not match Array at "+i, a1.a, a2.a);

            if (h.size() == 0)
                break;

            if (rand.nextInt(100) < 70)
            {
                // Make sure SimpleHeap works in the face of random adjusts.
                int idx = rand.nextInt(h.size());
                A a = h.getElementAt(idx); 
                a.a = rand.nextInt();
                
                h.adjust(a);
                Collections.sort(arr);
            }
            
        }
    }
    private void assertIndexes(SimpleHeap<A> h) {
        for(int i=0; i<h.size(); i++) {
            assertEquals(i, ((A)(h.getElementAt(i))).idx);
        }
    }

}
