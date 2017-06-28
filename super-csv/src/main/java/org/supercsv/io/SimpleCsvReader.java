package org.supercsv.io;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.TryReadAllResult;

import java.io.IOException;
import java.util.List;

public interface SimpleCsvReader {

    public boolean next() throws IOException;

    public boolean tryRead(List<String> values);

    public boolean tryRead(List<Object> values, final CellProcessor... processors);

    public TryReadAllResult<String> tryReadAll() throws IOException;

    public TryReadAllResult<Object> tryReadAll(final CellProcessor... processors) throws IOException;
}
