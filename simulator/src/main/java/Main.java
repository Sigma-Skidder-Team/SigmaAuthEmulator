import simulator.AuthSimulator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AuthSimulator::new);
    }
}