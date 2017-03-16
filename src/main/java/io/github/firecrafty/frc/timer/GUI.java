package io.github.firecrafty.frc.timer;

import io.github.firecrafty.frc.timer.utils.Audio;
import io.github.firecrafty.frc.timer.utils.Counter;
import io.github.firecrafty.frc.timer.utils.MatchTimer;
import io.github.firecrafty.frc.timer.utils.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author firecrafty
 */
public class GUI extends JFrame {
    private MatchTimer matchTimer = new MatchTimer();
    private Counter counter = new Counter();

    private static GUI instance_ = new GUI();

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

    public static GUI getInstance() {
        return instance_;
    }

    private GUI() {
        format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(2);
        addComponents();
        setTitle("FRC Match Timer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        updateDisplay();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDisplay();
            }
        }, 0, 100);
        pack();
    }

    private void createButtonPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(startStopButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(autonomousCheck, gbc);
        gbc.gridx = 2;
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
                    if(matchTimer.isUsingAutonomous()) {
                        Audio.playSound("start_auto.wav");
                    } else {
                        Audio.playSound("start_tele.wav");
                    }
                    matchTimer.setState(State.RUNNING);
                    startStopButton.setText("Stop");
                }
            }
        });
        scoredGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoredGearField.setText(Integer.toString(counter.incrementGearsScored()));
            }
        });
        droppedGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                droppedGearField.setText(Integer.toString(counter.incrementGearsDropped()));
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                counter.resetCounters();
                updateCounters();
                matchTimer.setState(State.RESET);
            }
        });
        autonomousCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                matchTimer.useAutonomous(autonomousCheck.isSelected());
            }
        });
    }

    public void updateDisplay() {
        long[] timeRemaining = matchTimer.getTimeRemaining();
        //int seconds = (int)((remaining)% 1000);
        timeLabel.setText(format.format(timeRemaining[0]) + ":" + format.format(timeRemaining[1]));
        if(timeRemaining[0] == 0 && timeRemaining[1] == 0) {
            Audio.playSound("end.wav");
        }

    }
    private void addComponents() {
        createButtonPanel();
        createHelperPanel();
        addActionListeners();
        gbc.gridx = 0;
        gbc.gridy = 0;
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, 200));
        mainPanel.add(timeLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);
        gbc.gridy = 2;
        mainPanel.add(helperPanel, gbc);
        getContentPane().add(mainPanel);
    }
    public void updateCounters() {
        scoredGearField.setText(Integer.toString(counter.getGearsScored()));
        droppedGearField.setText(Integer.toString(counter.getGearsDropped()));
    }



}
