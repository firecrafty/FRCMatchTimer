/*
 * MatchTimer.java
 *
 * This file is part of FRCMatchTimer (https://github.com/firecrafty/FRCMatchTimer)
 *
 * Copyright (C) 2017 Ryan Blue
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.firecrafty.frc.timer.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * @author firecrafty
 */
public class MatchTimer {
    private static MatchTimer instance_;
    boolean autonomous;
    Timer timer;
    State state = State.RESET;
    private long[] time = {2, 15};
    private long autonomousDuration = 150000;
    private long teleopOnlyDuration = 135000;
    private long remaining = teleopOnlyDuration;
    private long lastUpdate;


    public static MatchTimer getInstance() {
        if(instance_ == null) instance_ = new MatchTimer();
        return instance_;
    }
    private MatchTimer() {
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer();
            }
        });
        //timer.setInitialDelay(400);
    }

    private void updateTimer() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        remaining -= elapsed;
        lastUpdate = now;

        if(remaining < 0) remaining = 0;

        updateTimeRemaining();
    }
    private void updateTimeRemaining() {
        time[0] = TimeUnit.MILLISECONDS.toMinutes(remaining);
        time[1] = TimeUnit.MILLISECONDS.toSeconds(remaining - TimeUnit.MINUTES.toMillis(time[0]));
    }

    public State getState() {
        return state;
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

    public long[] getTimeRemaining() {

        return time;
    }

    public void startTimer() {
        // Restore the time we're counting down from and restart the timer.
        lastUpdate = System.currentTimeMillis();
        timer.start(); // Start the timer
    }

    public void useAutonomous(boolean autonomous) {
        this.autonomous = autonomous;
        if(state == State.RESET) {
            resetTimer();
        }
    }

    public void resetTimer() {
        stopTimer();
        remaining = autonomous ? autonomousDuration : teleopOnlyDuration;
        updateTimeRemaining();
    }

    public void stopTimer() {
        timer.stop(); // Stop the timer
        // Subtract elapsed time from the remaining time and stop timing
        long now = System.currentTimeMillis();
        remaining -= (now - lastUpdate);
        updateTimeRemaining();
    }

    public boolean isUsingAutonomous() {
        return autonomous;
    }
    public boolean inAutonomous() {
        return time[0] == 2 && time[1] > 15;
    }
}
