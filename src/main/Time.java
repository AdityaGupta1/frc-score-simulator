package main;

public class Time {
    private final int time;
    private final boolean isSingleton;

    public Time(int time, boolean isSingleton) {
        this.time = time;
        this.isSingleton = isSingleton;
    }

    public int getTime() {
        return time;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public String toString() {
        return time + ", isSingleton: " + isSingleton;
    }
}
