/*
 * Audio.java
 *
 * This file is part of FRCMatchTimer (https://github.com/firecrafty/FRCMatchTimer)
 *
 * Copyright (C) 2017 firecrafty
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

package io.github.firecrafty.frc.timer.utils;

import io.github.firecrafty.frc.timer.FRCMatchTimer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * @author firecrafty
 */
public class Audio {
    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(FRCMatchTimer.class.getResourceAsStream("/sounds/" + url));
                    clip.open(inputStream);
                    clip.start();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
