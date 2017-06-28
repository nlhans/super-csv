package org.supercsv.io;

import org.junit.Assert;
import org.junit.Test;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.TryReadAllResult;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;

public class SimpleCsvListReaderTest {



    @Test
    public void testTryRead() throws IOException {
        String csv = "Connor,John,16\r\nSarah,Connor,18\r\n\r\n";
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(csv), STANDARD_PREFERENCE);

        // Expected elements:
        String[] line1Arr = new String[] { "Connor","John", "16" };
        String[] line2Arr = new String[] { "Sarah","Connor", "18" };

        List<String[]> expected = new ArrayList<String[]>();
        expected.add(line1Arr);
        expected.add(line2Arr);

        int read = 0;
        int lines = 0;
        while (listReader.next())
        {
            lines++;
            List<String> values = new ArrayList<String>();
            values.add("foo");
            if (listReader.tryRead(values))
            {
                if (read < expected.size()) {
                    Assert.assertArrayEquals(values.toArray(), expected.get(read));
                }
                else {
                    Assert.assertFalse(true);
                }
                read++;
            }
            else {
                Assert.assertEquals(values.size(), 0);
            }
        }
        Assert.assertEquals(read, 2);
        Assert.assertEquals(lines, 2);
    }

    @Test
    public void testTryReadWithProcessor() throws IOException {
        String csv = "Connor,John,16\r\nSarah,Connor,18\r\nJohn,Test,ABC\r\n";
        CellProcessor[] processors = { new NotNull(), new NotNull(), new ParseInt()  };
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(csv), STANDARD_PREFERENCE);

        Object[] line1Arr = new Object[] { "Connor","John", 16 };
        Object[] line2Arr = new Object[] { "Sarah","Connor", 18 };

        List<Object[]> expected = new ArrayList<Object[]>();
        expected.add(line1Arr);
        expected.add(line2Arr);

        int read = 0;
        int lines = 0;
        while (listReader.next())
        {
            lines++;
            List<Object> values = new ArrayList<Object>();
            values.add("foo");
            if (listReader.tryRead(values, processors))
            {
                if (read < expected.size()) {
                    Assert.assertArrayEquals(values.toArray(), expected.get(read));
                }
                else {
                    Assert.assertFalse(true);
                }
                read++;
            }
            else {
                Assert.assertEquals(values.size(), 0);
            }
        }
        Assert.assertEquals(read, 2);
        Assert.assertEquals(lines, 3);
    }

    @Test
    public void testTryReadAll() throws IOException {
        String csv = "Connor,John,16\r\nSarah,Connor,18\r\nJohn,Test,ABC\r\n";
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(csv), STANDARD_PREFERENCE);

        List<List<String>> expected = Arrays.asList(
                Arrays.asList("Connor", "John", "16"),
                Arrays.asList("Sarah", "Connor", "18"),
                Arrays.asList("John", "Test", "ABC")
        );

        TryReadAllResult<String> context = listReader.tryReadAll();

        List<List<String>> parsed = context.getValues();

        Assert.assertTrue(context.isSuccess());
        Assert.assertEquals(expected, parsed);
        Assert.assertTrue(context.getFailed().isEmpty());
    }

    @Test
    public void testTryReadAllEmpty() throws IOException {
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(""), STANDARD_PREFERENCE);

        TryReadAllResult tryReadAllResult = listReader.tryReadAll();

        Assert.assertTrue(tryReadAllResult.isSuccess());
        Assert.assertTrue(tryReadAllResult.getValues().isEmpty());
        Assert.assertTrue(tryReadAllResult.getFailed().isEmpty());
    }

    @Test
    public void testTryReadAllFail() throws IOException {
        // trying to execute ParseInt on "ABC"
        String csv = "Connor,John,16\r\nSarah,Connor,18\r\nJohn,Test,ABC\r\n";
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(csv), STANDARD_PREFERENCE);

        List<List<Object>> expected = Arrays.asList(
                Arrays.asList(new Object[] {"Connor", "John", 16}),
                Arrays.asList(new Object[] {"Sarah", "Connor", 18})
        );

        TryReadAllResult<Object> context = listReader.tryReadAll(new NotNull(), new NotNull(), new ParseInt());

        List<List<Object>> parsed = context.getValues();
        List<String> failed = context.getFailed();

        Assert.assertFalse(context.isSuccess());
        Assert.assertEquals(expected, parsed);
        Assert.assertEquals(1, failed.size());
        Assert.assertEquals("John,Test,ABC", failed.get(0));
    }

    @Test
    public void testTryReadAllFail2() throws IOException {
        // trying to execute NotNull on null value
        String csv = "Connor,John,16\r\nSarah,,18\r\nJohn,Test,20\r\n";
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(csv), STANDARD_PREFERENCE);

        List<List<Object>> expected = Arrays.asList(
                Arrays.asList(new Object[] {"Connor", "John", 16}),
                Arrays.asList(new Object[] {"John", "Test", 20})
        );

        TryReadAllResult<Object> context = listReader.tryReadAll(new NotNull(), new NotNull(), new ParseInt());

        List<List<Object>> parsed = context.getValues();
        List<String> failed = context.getFailed();

        Assert.assertFalse(context.isSuccess());
        Assert.assertEquals(expected, parsed);
        Assert.assertEquals(1, failed.size());
        Assert.assertEquals("Sarah,,18", failed.get(0));
    }


    @Test
    public void testNext() throws IOException {
        String csv = "Connor,John,16\r\nSarah,Connor,18\r\nJohn,Test,ABC\r\n";
        SimpleCsvReader listReader = new SimpleCsvListReader(new StringReader(csv), STANDARD_PREFERENCE);

        Assert.assertTrue(listReader.next());
        Assert.assertTrue(listReader.next());
        Assert.assertTrue(listReader.next());
        Assert.assertFalse(listReader.next());
    }
}
