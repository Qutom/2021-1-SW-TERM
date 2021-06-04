package com.example.pnuwalker;

public class Pair<T> {
    private T first;
    private T second;

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public Pair(T[] arr) {
        if ( arr.length >= 2 ) {
            first = arr[0];
            second = arr[1];
        } else {
            first = null;
            second = null;
        }
    }
    public void setFirst(T first) { this.first = first; }
    public void setSecond(T second) { this.second = second; }

    public T getFirst() { return first; }
    public T getSecond() { return second; }

    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
