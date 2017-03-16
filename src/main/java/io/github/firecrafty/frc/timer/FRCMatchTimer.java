package io.github.firecrafty.frc.timer;

import javax.swing.*;

/**
 * @author firecrafty
 */
public class FRCMatchTimer {
    public static GUI gui;
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }
        gui = GUI.getInstance();
    }
}
