package io.github.firecrafty.frc.timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author firecrafty
 */
public class GUI extends JFrame {
    private long remaining = 15000;
    private long lastUpdate;
    private int scoredGearCount = 0;
    private int droppedGearCount = 0;
    private boolean useAutonomous = false;
    private static GUI instance_ = new GUI();

    State timerState = State.RESET;

    NumberFormat format;
    JPanel mainPanel = new JPanel(new GridBagLayout());
    JLabel timeLabel = new JLabel();

    JPanel buttonPanel = new JPanel(new GridBagLayout());
    JButton startStopButton = new JButton();
    JCheckBox autonomousCheck = new JCheckBox("Use autonomous?");
    JCheckBox soundCheck = new JCheckBox("Play match noises?");
    JButton resetButton = new JButton("Reset");

    JPanel helperPanel = new JPanel(new GridBagLayout());
    JButton scoredGearButton = new JButton("Scored Gear");
    JButton droppedGearButton = new JButton("Dropped Gear");
    JTextField scoredGearField = new JTextField("0", 2);
    JTextField droppedGearField = new JTextField("0", 2);

    GridBagConstraints gbc = new GridBagConstraints();

    Timer timer;

    public static GUI getInstance() {
        return instance_;
    }

    private GUI() {
        format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(2);
        setTimerState(State.RESET);
        setTitle("FRC Match Timer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDisplay();
            }
        });
        timer.setInitialDelay(0);
        resetTimer();
        pack();
    }

    public void setTimerState(State state) {
        State previousState = this.timerState;
        this.timerState = state;
        switch(this.timerState) {
            case RUNNING:
                startStopButton.setText("Stop");
                break;
            case STOPPED:
            case RESET:
                startStopButton.setText("Start");
                break;
        }
        switch(previousState) {
            case RESET:
                switch(state) {
                    case RUNNING:

                }
        }
    }

    public State getTimerState() {
        return timerState;
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
                if(timerState == State.RUNNING) {
                    setTimerState(State.STOPPED);
                    stopTimer();
                } else {
                    if(timerState == State.RESET) {
                        remaining = useAutonomous ? 150000 : 135000;
                    }
                    setTimerState(State.RUNNING);
                    startTimer();
                }
            }
        });
        scoredGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoredGearCount++;
                updateCounters();
            }
        });
        droppedGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                droppedGearCount++;
                updateCounters();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTimerState(State.RESET);
                scoredGearCount = 0;
                droppedGearCount = 0;
                updateCounters();
                resetTimer();

            }
        });
        autonomousCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useAutonomous = autonomousCheck.isSelected();
                resetTimer();
            }
        });
    }

    private void updateCounters() {
        scoredGearField.setText(Integer.toString(scoredGearCount));
        droppedGearField.setText(Integer.toString(droppedGearCount));
    }

    public void updateDisplay() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastUpdate;
        remaining -= elapsed;
        lastUpdate = now;

        if(remaining < 0) remaining = 0;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining - TimeUnit.MINUTES.toMillis(minutes));
        //int minutes = (int)(remaining/60000);
        //int seconds = (int)((remaining)% 1000);
        timeLabel.setText(format.format(minutes) + ":" + format.format(seconds));
        if(remaining == 0) {
            timer.stop();
        }

    }

    void startTimer() {
        // Restore the time we're counting down from and restart the timer.
        lastUpdate = System.currentTimeMillis();
        timer.start(); // Start the timer
    }

    void stopTimer() {
        // Subtract elapsed time from the remaining time and stop timing
        long now = System.currentTimeMillis();
        remaining -= (now - lastUpdate);
        timer.stop(); // Stop the timer
    }

    void resetTimer() {
        stopTimer();
        remaining = useAutonomous ? 150000 : 135000;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining - TimeUnit.MINUTES.toMillis(minutes));
        timeLabel.setText(format.format(minutes) + ":" + format.format(seconds));

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



}
