package org.supercsv.util;

/**
 * Created by hans on 26-6-17.
 */
public class Tuple<TA, TB> {
    TA first;
    TB second;

    public Tuple(TA a, TB b){
        first=a;
        second=b;
    }

    public TA GetFirst() { return first; }
    public TB GetSecond() { return second; }
}
