package simulator.impl;

import simulator.utils.VisualLogs;
import sigma.auth.encryption.Encryptor;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LicenseTab extends JPanel {

    private final JTextField pathToFile = new JTextField("jello/jello.lic");

    public LicenseTab(JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Path to .lic file:"));
        add(pathToFile);

        JButton decrypt = new JButton("Decrypt license");
        decrypt.addActionListener(e -> decrypt());
        add(decrypt);

        tabs.add(this);
    }

    private void decrypt() {
        String path = this.pathToFile.getText();
        File file = new File(path);

        new Thread(() -> {
            if (file.exists() && file.isFile()) {
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    Encryptor encryptor = new Encryptor(fileContent);
                    VisualLogs.displayInfo("Username: " + encryptor.username + "\nAuth token: " + encryptor.authToken + "\nAgora token: " + encryptor.agoraToken, "Decrypted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}
