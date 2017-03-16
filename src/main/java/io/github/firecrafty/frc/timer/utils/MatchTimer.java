package io.github.firecrafty.frc.timer.utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * @author firecrafty
 */
public class MatchTimer {
    private long[] time = {2, 15};
    private long duration;
    private long remaining = 135000;
    private long lastUpdate;
    boolean autonomous;
    Timer timer;
    State state = State.RESET;

    public MatchTimer() {
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        });
        timer.setInitialDelay(400);
    }

    public void setState(State state) {
        State previousState = this.state;
        this.state = state;
        switch(previousState) {
            case RESET:
                switch(this.state) {
                    case RUNNING:
                        startTimer();
                        break;
                    case STOPPED:
                        setState(State.RESET);
                        break;
                }
                break;
            case RUNNING:
                switch(this.state) {
                    case RESET:
                        resetTimer();
                        break;
                    case STOPPED:
                        stopTimer();
                        break;
                }
                break;
            case STOPPED:
                switch(this.state) {
                    case RUNNING:
                        startTimer();
                        break;
                    case RESET:
                        resetTimer();
                        break;
                }
                break;
        }
    }

    public State getState() {
        return state;
    }

    private void updateTimer() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        remaining -= elapsed;
        lastUpdate = now;

        if(remaining < 0) remaining = 0;

        time[0] = TimeUnit.MILLISECONDS.toMinutes(remaining);
        time[1] = TimeUnit.MILLISECONDS.toSeconds(remaining - TimeUnit.MINUTES.toMillis(time[0]));
    }

    public long[] getTimeRemaining() {

        return time;
    }

    public void startTimer() {
        // Restore the time we're counting down from and restart the timer.
        lastUpdate = System.currentTimeMillis();
        timer.start(); // Start the timer
    }

    public void stopTimer() {
        // Subtract elapsed time from the remaining time and stop timing
        long now = System.currentTimeMillis();
        remaining -= (now - lastUpdate);
        timer.stop(); // Stop the timer
    }

    public void resetTimer() {
        stopTimer();
        remaining = autonomous ? 150000 : 135000;
        time[0] = TimeUnit.MILLISECONDS.toMinutes(remaining);
        time[1] = TimeUnit.MILLISECONDS.toSeconds(remaining - TimeUnit.MINUTES.toMillis(time[0]));
    }

    public void useAutonomous(boolean autonomous) {
        this.autonomous = autonomous;
        if(state == State.RESET) {
            resetTimer();
        }
    }

    public boolean isUsingAutonomous() {
        return autonomous;
    }

}
