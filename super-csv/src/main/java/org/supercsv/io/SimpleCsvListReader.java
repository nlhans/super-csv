package org.supercsv.io;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.TryReadAllResult;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class SimpleCsvListReader implements SimpleCsvReader {
    private CsvListReader listReader;

    public SimpleCsvListReader(Reader reader, CsvPreference prefs) {
        listReader = new CsvListReader(reader, prefs);
    }

    public SimpleCsvListReader(ITokenizer tokenizer, CsvPreference prefs) {
        listReader = new CsvListReader(tokenizer, prefs);
    }

    public boolean next() throws IOException {
        try {
            return listReader.readRow();
        } catch (SuperCsvException ex) {
            return false;
        }
    }

    public boolean tryRead(List<String> values) {
        if (values == null) {
            return false;
        }
        values.clear();
        try {
            values.addAll(new ArrayList<String>(listReader.getColumns()));
            return true;
        } catch (SuperCsvException ex) {
            // ignore all exceptions; just tell user that the read was not successful
            return false;
        }
    }

    public boolean tryRead(List<Object> values, CellProcessor... processors) {
        if (values == null) {
            return false;
        }
        if (processors == null) {
            return false;
        }
        values.clear();
        try {
            values.addAll(listReader.executeProcessors(processors));
            return true;
        } catch (SuperCsvException ex) {
            // ignore all exceptions; just tell user that the read was not successful
            return false;
        }
    }

    public TryReadAllResult<String> tryReadAll() throws IOException {
        TryReadAllResult<String> tryReadAllResult = new TryReadAllResult<String>();

        List<String> columns = new ArrayList<String>();

        while (next()) {
            boolean successfullyParsed = tryRead(columns);

            if (successfullyParsed) {
                tryReadAllResult.addValues(new ArrayList<String>(columns));
            } else {
                tryReadAllResult.addFailed(listReader.getUntokenizedRow());
            }
        }

        return tryReadAllResult;
    }

    public TryReadAllResult<Object> tryReadAll(CellProcessor... processors) throws IOException {
        TryReadAllResult<Object> tryReadAllResult = new TryReadAllResult<Object>();

        List<Object> columns = new ArrayList<Object>();

        while (next()) {
            boolean successfullyParsed = tryRead(columns, processors);

            if (successfullyParsed) {
                tryReadAllResult.addValues(new ArrayList<Object>(columns));
            } else {
                tryReadAllResult.addFailed(listReader.getUntokenizedRow());
            }
        }

        return tryReadAllResult;
    }
}
