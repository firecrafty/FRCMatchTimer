/*
 * ScoreKeeper.java
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

/**
 * @author firecrafty
 */
public class ScoreKeeper {
    private int score = 0;
    private int gearsScored = 0;
    private int gearsDropped = 0;
    private int rotorsTurning;
    private int gearScore = 0;
    private int ballScore = 0;
    private boolean climbed;

    public int getScore() {
        return Airship.getRotorScore();
    }

    public int getGearsScored() {
        return gearsScored;
    }

    public int getGearsDropped() {
        return gearsDropped;
    }

    public int incrementGearsScored() {
        Airship.scoreGear();
        return ++gearsScored;
    }

    public int incrementGearsDropped() {
        return ++gearsDropped;
    }

    public void setClimbed(boolean climbed) {
        if(this.climbed == climbed) return;
        boolean previousValue = this.climbed;
        this.climbed = previousValue;
        score += climbed ? 50 : -50;
    }

    public boolean isClimbed() {
        return climbed;
    }

    public void resetCounters() {
        score = 0;
        gearsScored = 0;
        gearsDropped = 0;
        gearScore = 0;
        ballScore = 0;
        climbed = false;
    }

    private void updateScore() {

    }
}

class Airship {
    private static int gearsScoredInTeleop;
    private static int gearsScoredInAutonomous;

    public static void scoreGear() {
        Rotor.addGear(false);
        gearsScoredInTeleop++;
    }
    public static void scoreGear(boolean inAutonomous) {
        Rotor.addGear(inAutonomous);
        if(inAutonomous) {
            gearsScoredInAutonomous++;
        } else {
            gearsScoredInTeleop++;
        }
    }

    public static int getGearsScored() {
        return gearsScoredInAutonomous + gearsScoredInAutonomous;
    }

    public static int getRotorScore() {
        int score = 0;
        for(Rotor rotor : Rotor.values()) {
            if(rotor.isSpinning()) {
                score += 40;
            }
            if(rotor.inAuton()) {
                score += 10;
            }
        }
        return score;
    }

    public static void reset() {
        gearsScoredInAutonomous = 0;
        gearsScoredInTeleop = 0;
        Rotor.resetAll();
    }

}

enum Rotor {
    ONE(1), TWO(2), THREE(4), FOUR(6);

    private final int TOTAL_ROTOR_GEARS;
    private int gearsRemaining;
    private boolean inAuton;


    public static void addGear(boolean inAuton) {
        for(Rotor rotor : Rotor.values()) {
            if(!rotor.isSpinning()) {
                rotor.addGearPrivate(inAuton);
                break;
            }
        }
    }

    public static void resetAll() {
        for(Rotor rotor : Rotor.values()) {
            rotor.reset();
        }
    }

    Rotor(int totalRotorGears) {
        this.TOTAL_ROTOR_GEARS = totalRotorGears;
        reset();
    }

    public boolean isSpinning() {
        return gearsRemaining == 0;
    }

    public boolean inAuton() {
        return inAuton;
    }

    private void addGearPrivate() {
        gearsRemaining--;
    }

    private void addGearPrivate(boolean inAuton) {
        gearsRemaining--;
        this.inAuton = inAuton;
    }

    public void reset() {
        gearsRemaining = TOTAL_ROTOR_GEARS;
        inAuton = false;
    }
}
