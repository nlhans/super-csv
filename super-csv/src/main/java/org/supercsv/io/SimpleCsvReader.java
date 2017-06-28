package org.supercsv.io;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.TryReadAllResult;

import java.io.IOException;
import java.util.List;

public interface SimpleCsvReader {

    boolean next() throws IOException;

    boolean tryRead(List<String> values);

    boolean tryRead(List<Object> values, final CellProcessor... processors);

    TryReadAllResult<String> tryReadAll() throws IOException;

    TryReadAllResult<Object> tryReadAll(final CellProcessor... processors) throws IOException;
}
