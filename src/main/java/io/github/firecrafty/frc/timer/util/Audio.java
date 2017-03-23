/*
 * Audio.java
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

import io.github.firecrafty.frc.timer.FRCMatchTimer;

import javax.sound.sampled.*;

/**
 * @author firecrafty
 */
public class Audio {
    public static synchronized void playSound(final String url) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(FRCMatchTimer.class.getResource("/sounds/" + url));
            clip.open(inputStream);
            clip.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void playSoundSequence(final String... urls) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[4096];
                for(String url : urls) {
                    try {
                        AudioInputStream is = AudioSystem.getAudioInputStream(FRCMatchTimer.class.getResource("/sounds/" + url));
                        AudioFormat format = is.getFormat();
                        SourceDataLine line = AudioSystem.getSourceDataLine(format);
                        line.open(format);
                        line.start();
                        while(is.available() > 0) {
                            int len = is.read(buffer);
                            line.write(buffer, 0, len);
                        }
                        line.drain();
                        line.close();
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
