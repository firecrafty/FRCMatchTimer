/*
 * GUI.java
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

package io.github.firecrafty.frc.timer;

import io.github.firecrafty.frc.timer.util.Audio;
import io.github.firecrafty.frc.timer.util.MatchTimer;
import io.github.firecrafty.frc.timer.util.ScoreKeeper;
import io.github.firecrafty.frc.timer.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Ryan Blue
 */
public class GUI extends JFrame {
    private static GUI instance_;
    NumberFormat format;
    JPanel mainPanel = new JPanel(new GridBagLayout());
    JLabel timeLabel = new JLabel();
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    JButton startStopButton = new JButton("Start");
    JCheckBox autonomousCheck = new JCheckBox("Use autonomous?");
    JCheckBox soundCheck = new JCheckBox("Play match noises?");
    JButton resetButton = new JButton("Reset");
    JPanel helperPanel = new JPanel(new GridBagLayout());
    JButton scoredGearButton = new JButton("Scored Gear");
    JButton droppedGearButton = new JButton("Dropped Gear");
    JTextField scoredGearField = new JTextField("0", 2);
    JTextField droppedGearField = new JTextField("0", 2);
    GridBagConstraints gbc = new GridBagConstraints();
    Timer timer = new Timer();
    private MatchTimer matchTimer = MatchTimer.getInstance();
    private ScoreKeeper scoreKeeper = new ScoreKeeper();
    private boolean startSoundPlayed;
    private boolean autoToTeleSoundPlayed;
    private boolean startEndGameSoundPlayed;
    private boolean endSoundPlayed;

    private GUI() {
        format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(2);
        addComponents();
        setTitle("FRC Match Timer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(1029, 589));
        // Still debating if I want this
        // setExtendedState(Frame.MAXIMIZED_BOTH);
        updateDisplay();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDisplay();
            }
        }, 0, 100);
    }

    private void addComponents() {
        createButtonPanel();
        createHelperPanel();
        addActionListeners();
        gbc.gridx = 0;
        gbc.gridy = 0;
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, 400));
        mainPanel.add(timeLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);
        gbc.gridy = 2;
        mainPanel.add(helperPanel, gbc);
        getContentPane().add(mainPanel);
    }

    public void updateDisplay() {
        long[] timeRemaining = matchTimer.getTimeRemaining();
        //int seconds = (int)((remaining)% 1000);
        timeLabel.setText(format.format(timeRemaining[0]) + ":" + format.format(timeRemaining[1]));
        if(matchTimer.getState() == State.RUNNING && soundCheck.isSelected()) {
            if((timeRemaining[0] == 2 && timeRemaining[1] == 16) && (matchTimer.isUsingAutonomous() && !autoToTeleSoundPlayed)) {
                Audio.playSoundSequence("end_autonomous.wav", "start_tele.wav");
                autoToTeleSoundPlayed = true;
            }
            if((timeRemaining[0] == 0 && timeRemaining[1] == 30) && !startEndGameSoundPlayed) {
                Audio.playSound("start_end_game.wav");
                startEndGameSoundPlayed = true;
            }
            if((timeRemaining[0] == 0 && timeRemaining[1] == 0) && !endSoundPlayed) {
                Audio.playSound("end.wav");
                endSoundPlayed = true;
            }
        }
    }

    private void createButtonPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(startStopButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(autonomousCheck, gbc);
        gbc.gridx = 2;
        soundCheck.setSelected(true);
        buttonPanel.add(soundCheck, gbc);
        gbc.gridx = 3;
        buttonPanel.add(resetButton, gbc);
    }

    private void createHelperPanel() {
        scoredGearField.setEditable(false);
        droppedGearField.setEditable(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        helperPanel.add(scoredGearButton, gbc);
        gbc.gridx = 1;
        helperPanel.add(droppedGearButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        helperPanel.add(scoredGearField, gbc);
        gbc.gridx = 1;
        helperPanel.add(droppedGearField, gbc);
    }

    private void addActionListeners() {
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(matchTimer.getState() == State.RUNNING) {
                    matchTimer.setState(State.STOPPED);
                    startStopButton.setText("Start");
                } else {
                    if(soundCheck.isSelected() && !startSoundPlayed) {
                        if(matchTimer.isUsingAutonomous()) {
                            Audio.playSound("start_auto.wav");
                        } else {
                            Audio.playSound("start_tele.wav");
                        }
                        startSoundPlayed = true;
                    }
                    matchTimer.setState(State.RUNNING);
                    startStopButton.setText("Stop");
                }
            }
        });
        scoredGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoredGearField.setText(Integer.toString(scoreKeeper.incrementGearsScored()));
            }
        });
        droppedGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                droppedGearField.setText(Integer.toString(scoreKeeper.incrementGearsDropped()));
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreKeeper.resetCounters();
                updateCounters();
                matchTimer.setState(State.RESET);
                startSoundPlayed = false;
                autoToTeleSoundPlayed = false;
                endSoundPlayed = false;
                startStopButton.setText("Start");
            }
        });
        autonomousCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matchTimer.useAutonomous(autonomousCheck.isSelected());
            }
        });
        soundCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void updateCounters() {
        scoredGearField.setText(Integer.toString(scoreKeeper.getGearsScored()));
        droppedGearField.setText(Integer.toString(scoreKeeper.getGearsDropped()));
    }

    public static GUI getInstance() {
        if(instance_ == null) instance_ = new GUI();
        return instance_;
    }


}
