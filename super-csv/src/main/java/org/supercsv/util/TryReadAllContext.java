package org.supercsv.util;

import java.util.ArrayList;
import java.util.List;

public class TryReadAllContext<T> {
    private List<List<T>> values = new ArrayList<List<T>>();
    private List<String> failed = new ArrayList<String>();
    private boolean success = true;

    public List<List<T>> getValues() {
        return values;
    }

    public List<String> getFailed() {
        return failed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void addValues(List<T> values) {
        this.values.add(values);
    }

    public void addFailed(String untokenizedRow) {
        success = false;
        failed.add(untokenizedRow);
    }
}
