package io.github.firecrafty.frc.timer;

import io.github.firecrafty.frc.timer.actions.StartStopAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author firecrafty
 */
public class GUI extends JFrame{
    private static GUI instance_ = new GUI();
    State state = State.ZEROED;

    JPanel mainPanel = new JPanel(new GridBagLayout());
    JLabel timeLabel = new JLabel(2 + ":" + 15);

    JPanel buttonPanel = new JPanel(new GridBagLayout());
    JButton startStopButton = new JButton(new StartStopAction(this));
    JCheckBox autonomousCheck = new JCheckBox("Use autonomous?");
    JCheckBox soundCheck = new JCheckBox("Play match noises?");
    JButton resetButton = new JButton("Reset");

    JPanel helperPanel = new JPanel(new GridBagLayout());
    JButton scoredGearButton = new JButton("Scored Gear");
    JButton droppedGearButton = new JButton("Dropped Gear");
    JTextField scoredGearField = new JTextField("0",  2);
    JTextField droppedGearField = new JTextField("0", 2);
    GridBagConstraints gbc = new GridBagConstraints();

    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("");
        }
    });
    public static GUI getInstance() {
        return instance_;
    }
    private GUI() {
        createButtonPanel();
        createHelperPanel();
        addActionListeners();
        setTimerState(State.ZEROED);
        gbc.gridx = 0;
        gbc.gridy = 0;
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, 200));
        mainPanel.add(timeLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);
        gbc.gridy = 2;
        mainPanel.add(helperPanel, gbc);
        getContentPane().add(mainPanel);
        setTitle("FRC Match Timer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    public void setTimerState(State state) {
        this.state = state;
        switch(state) {
            case RUNNING:
                startStopButton.setText("Stop");
                timer.stop();
                break;
            case STOPPED:
            case ZEROED:
                startStopButton.setText("Start");
                timer.stop();
                break;
        }
    }
    public State getTimerState() {
        return state;
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
        scoredGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoredGearField.setText(Integer.toString(Integer.parseInt(scoredGearField.getText()) + 1));
            }
        });
        droppedGearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                droppedGearField.setText(Integer.toString(Integer.parseInt(droppedGearField.getText()) + 1));
            }
        });
    }
    private void updateTime() {

    }

}
