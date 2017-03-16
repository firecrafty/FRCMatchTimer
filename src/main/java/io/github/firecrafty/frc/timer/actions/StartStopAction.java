package io.github.firecrafty.frc.timer.actions;

import io.github.firecrafty.frc.timer.GUI;
import io.github.firecrafty.frc.timer.State;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author firecrafty
 */
public class StartStopAction extends AbstractAction {
    GUI gui;
    public StartStopAction(GUI gui) {
        super();
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(gui.getTimerState() == State.RUNNING) {
            gui.setTimerState(State.STOPPED);
        } else {
            gui.setTimerState(State.RUNNING);
        }
    }
}
