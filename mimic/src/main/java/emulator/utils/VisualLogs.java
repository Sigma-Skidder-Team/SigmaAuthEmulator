package emulator.utils;

import javax.swing.*;

/**
 * @author Knezo
 */
public class VisualLogs {
    public static void displayError(String reason, String title) {
        JOptionPane.showMessageDialog(null, reason, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void displayInfo(String information, String title) {
        JOptionPane.showMessageDialog(null, information, title, JOptionPane.INFORMATION_MESSAGE);
    }
}