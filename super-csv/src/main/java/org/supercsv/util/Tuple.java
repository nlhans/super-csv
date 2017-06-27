package org.supercsv.util;

/**
 * Created by hans on 26-6-17.
 */
public class Tuple<A, B> {
    A first;
    B second;

    public Tuple(A a, B b){
        first=a;
        second=b;
    }

    public A getFirst() { return first; }
    public B getSecond() { return second; }
}
