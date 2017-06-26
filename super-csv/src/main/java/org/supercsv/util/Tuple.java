package org.supercsv.util;

/**
 * Created by hans on 26-6-17.
 */
public class Tuple<Ta, Tb> {
    Ta first;
    Tb second;

    public Tuple(Ta a, Tb b){
        first=a;
        second=b;
    }

    public Ta GetFirst() { return first; }
    public Tb GetSecond() { return second; }
}
