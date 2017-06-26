package org.supercsv.io;

import org.junit.Test;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.mock.IdentityTransform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hans on 26-6-17.
 */
public class AbstractCsvProcessorTest extends AbstractCsvProcessor {

    private static final List<String> LIST = Arrays.asList("Ezio", "25", "Venice");

    private static final CellProcessor[] PROCESSORS = new CellProcessor[] { new IdentityTransform(), new ParseInt(),
            null };

    private static final int LINE_NO = 23;

    private static final int ROW_NO = 12;

    /**
     * Tests the executeCellProcessors() method.
     */
    @Test
    public void testExecuteCellProcessors() {
        List<Object> destinationList = new ArrayList<Object>();
        executeCellProcessors(destinationList, LIST, PROCESSORS, LINE_NO, ROW_NO);
        assertTrue(destinationList.size() == 3);
        assertEquals("Ezio", destinationList.get(0));
        assertEquals(Integer.valueOf(25), destinationList.get(1));
        assertEquals("Venice", destinationList.get(2));
    }

    /**
     * Tests the executeCellProcessors() method with a null destination List (should throw an Exception).
     */
    @Test(expected = NullPointerException.class)
    public void testExecuteCellProcessorsWithNullDestination() {
        executeCellProcessors(null, LIST, PROCESSORS, LINE_NO, ROW_NO);
    }

    /**
     * Tests the executeCellProcessors() method with a null source List (should throw an Exception).
     */
    @Test(expected = NullPointerException.class)
    public void testExecuteCellProcessorsWithNullSource() {
        executeCellProcessors(new ArrayList<Object>(), null, PROCESSORS, LINE_NO, ROW_NO);
    }

    /**
     * Tests the executeCellProcessors() method with a processors array (should throw an Exception).
     */
    @Test(expected = NullPointerException.class)
    public void testExecuteCellProcessorsWithNullProcessors() {
        executeCellProcessors(new ArrayList<Object>(), LIST, null, LINE_NO, ROW_NO);
    }

    /**
     * Tests the executeCellProcessors() method with a source List whose size doesn't match the number of CellProcessors
     * (should throw an Exception).
     */
    @Test(expected = SuperCsvException.class)
    public void testExecuteCellProcessorsWithSizeMismatch() {
        final List<Object> invalidSizeList = new ArrayList<Object>();
        executeCellProcessors(new ArrayList<Object>(), invalidSizeList, PROCESSORS, LINE_NO, ROW_NO);
    }

}
