package org.supercsv.util;

import java.util.List;

public class TryReadAllContext {
    private List<List<String>> values;
    private List<CsvContext> failed;
    private boolean success;

    public List<List<String>> getValues() {
        return values;
    }

    public List<CsvContext> getFailed() {
        return failed;
    }

    public boolean isSuccess() {
        return success;
    }
}
