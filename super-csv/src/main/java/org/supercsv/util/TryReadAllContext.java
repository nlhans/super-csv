package org.supercsv.util;

import java.util.ArrayList;
import java.util.List;

public class TryReadAllContext {
    private List<List<? extends Object>> values = new ArrayList<List<? extends Object>>();
    private List<String> failed = new ArrayList<String>();
    private boolean success = true;

    public List<List<?>> getValues() {
        return values;
    }

    public List<String> getFailed() {
        return failed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void addValues(List<?> values) {
        this.values.add(values);
    }

    public void addFailed(String untokenizedRow) {
        success = false;
        failed.add(untokenizedRow);
    }
}
