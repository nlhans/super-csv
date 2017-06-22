package org.supercsv.io;

import org.junit.Test;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CsvListReaderSimpleTest {

    /**
     * Helper method to use CsvListParser and CsvPreference.STANDARD_PREFERENCE
     * to parse String s and compare it to expected param with assertEquals
     *
     * @param s csv as string
     * @param expected list of lists that is expected when s is parsed
     * @param processors optional argument; array of processors to be used when parsed
     * @throws IOException
     */
    private void testListReaderWithStdPrefs(
            String s,
            List<List<String>> expected,
            CellProcessor[] processors) throws IOException {

        Reader reader = new StringReader(s);
        CsvPreference prefs = CsvPreference.STANDARD_PREFERENCE;
        CsvListReader listReader = new CsvListReader(reader, prefs);

        List<List<Object>> rows = new ArrayList<List<Object>>();

        while (true) {
            List<Object> row = null;

            if (processors == null) {
                // if processors == null then read values as strings,
                // convert to objects and insert into list of rows
                List<String> stringResult = listReader.read();

                if (stringResult != null) {
                    // on eof stringResult will be null. this will result
                    // to row being null and breaking out of loop
                    row = new ArrayList<Object>();

                    for (String strRes : stringResult) {
                        row.add(strRes);
                    }
                }
            } else {
                row = listReader.read(processors);
            }

            if (row == null) {
                break; // eof
            }

            rows.add(row);
        }

        assertEquals(expected, rows);
    }

    @Test
    public void testSimpleCsv() throws IOException {
        String s = "aaa,bbb,ccc\r\nddd,eee,fff";
        List<List<String>> expected = new ArrayList<List<String>>();
        expected.add(Arrays.asList("aaa", "bbb", "ccc"));
        expected.add(Arrays.asList("ddd", "eee", "fff"));

        testListReaderWithStdPrefs(s, expected, null);
    }


    @Test
    public void testCRLFEnding() throws IOException {
        String s = "aaa,bbb,ccc\r\nddd,eee,fff\r\n";
        List<List<String>> expected = new ArrayList<List<String>>();
        expected.add(Arrays.asList("aaa", "bbb", "ccc"));
        expected.add(Arrays.asList("ddd", "eee", "fff"));

        testListReaderWithStdPrefs(s, expected, null);
    }

    @Test
    public void testQuotedValuesCsv() throws IOException {
        String s = "\"aaa\",\"bbb\",\"ccc\"\r\n\"ddd\",\"eee\",\"fff\"";
        List<List<String>> expected = new ArrayList<List<String>>();
        expected.add(Arrays.asList("aaa", "bbb", "ccc"));
        expected.add(Arrays.asList("ddd", "eee", "fff"));

        testListReaderWithStdPrefs(s, expected, null);
    }

    @Test
    public void testCommaInCsv() throws IOException {
        String s = "\"a,aa\",\",bbb\",\"ccc\"\r\n\"ddd,\",\"eee\",\"fff\"";
        List<List<String>> expected = new ArrayList<List<String>>();
        expected.add(Arrays.asList("a,aa", ",bbb", "ccc"));
        expected.add(Arrays.asList("ddd,", "eee", "fff"));

        testListReaderWithStdPrefs(s, expected, null);
    }

    @Test
    public void testQuoteInCsv() throws IOException {
        // according to the specification (RFC4180), " must be escaped with another "
        String s = "\"aa\"\"a\",\"bbb\",\"ccc\"\r\n\"ddd\"\"\",\"eee\",\"fff\"";
        List<List<String>> expected = new ArrayList<List<String>>();
        expected.add(Arrays.asList("aa\"a", "bbb", "ccc"));
        expected.add(Arrays.asList("ddd\"", "eee", "fff"));

        testListReaderWithStdPrefs(s, expected, null);
    }

    @Test
    // https://github.com/super-csv/super-csv/issues/128
    // There's an open issue that claims that commas inside quotes
    // do not work. However, this does not seem to be the case
    // since this test passes.
    // ...Unless the CellProcessors used were more exotic
    public void testCommaSeparatedName() throws IOException {
        String s = "User,Group\r\n\"John, Black\",Legal Department";
        List<List<String>> expected =
                Arrays.asList(
                        Arrays.asList("User", "Group"),
                        Arrays.asList("John, Black", "Legal Department")
                );

        testListReaderWithStdPrefs(s, expected, new CellProcessor[] {new NotNull(), new NotNull()});
    }


}