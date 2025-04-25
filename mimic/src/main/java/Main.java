import emulator.SimpleApp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleApp::new);
    }
}