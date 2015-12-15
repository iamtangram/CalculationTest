package com.emc.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

/**
 * Test point. Test core functionality, Test file reader. Actually, I can use mockito to mock something or BDD to
 * verfity the cases, but I think this is not the examination point.
 * 
 * @author Jason.Wang
 */
public class CalculationTest {

    @Test
    public void testCoreNotEmpty() {
        NumCountThread nc = new NumCountThread("-1 0.3 1 1.1 1.2 3 a 1 0.6");
        nc.execute();
        assertEquals(8, nc.getCount());
        assertEquals(7.2f, nc.getSum(), 0.00001);
    }

    @Test
    public void testCoreEmpty() {
        NumCountThread nc2 = new NumCountThread("");
        nc2.execute();
        assertEquals(0, nc2.getCount());
        assertEquals(0.0f, nc2.getSum(), 0.00001);
    }

    @Test
    public void testCoreOnlyNonNumber() {
        NumCountThread nc3 = new NumCountThread("abc");
        nc3.execute();
        assertEquals(0, nc3.getCount());
        assertEquals(0.0f, nc3.getSum(), 0.00001);
    }

    @Test
    public void testReadFile() throws FileNotFoundException {
        NumCountThread nc = new NumCountThread("");
        BufferedReader reader = new BufferedReader(
                new FileReader(CalculationTest.class.getResource("/1.txt").getPath()));
        try {
            String s = nc.readFileAsLine(reader, 2);
            assertEquals("1 2 34 55 77 1 2 34 55 77 ", s);

        } catch (Exception e) {
            assertTrue("Got exception", false);
        }
    }

}
