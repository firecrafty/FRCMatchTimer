package io.github.firecrafty.frc.timer.utils;

/**
 * @author firecrafty
 */
public class Counter {
    private int gearsScored =  0;
    private int gearsDropped = 0;

    public int getGearsScored() {
        return gearsScored;
    }

    public int getGearsDropped() {
        return gearsDropped;
    }
    public int incrementGearsScored() {
        return ++gearsDropped;
    }
    public int incrementGearsDropped() {
        return ++gearsDropped;
    }
    public int decrementGearsScored() {
        return --gearsScored;
    }
    public int decrementGearsDropped() {
        return --gearsDropped;
    }
    public void resetCounters() {
        gearsScored = 0;
        gearsDropped = 0;
    }
}
