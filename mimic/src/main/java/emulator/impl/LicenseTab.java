package emulator.impl;

import emulator.utils.VisualLogs;
import sigma.auth.Encryptor;
import sigma.hwid.HardwareIDGenerator;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class LicenseTab extends JPanel {

    private final JTextField hwid = new JTextField(Arrays.toString(HardwareIDGenerator.generateHardwareID("mcAzMFSvCM6wFCHcgzOn")));
    private final JTextField pathToFile = new JTextField("jello/jello.lic");

    public LicenseTab(JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        add(new JLabel("HWID:"));
        add(hwid);
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

        String hwid = this.hwid.getText().replaceAll("\\[|]|\\s", "");
        byte[] newHWID;
        if (!hwid.isEmpty()) {
            String[] parts = hwid.split(",");

            newHWID = new byte[parts.length];
            for (int i = 0; i < parts.length; i++) {
                newHWID[i] = Byte.parseByte(parts[i]);
            }
        } else {
            newHWID = new byte[0];
        }

        new Thread(() -> {
            if (file.exists() && file.isFile()) {
                try {
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    Encryptor encryptor;
                    if (hwid.isEmpty()) {
                        encryptor = new Encryptor(fileContent);
                    } else {
                        encryptor = new Encryptor(fileContent, newHWID);
                    }
                    VisualLogs.displayInfo("Username: " + encryptor.username + "\nAuth token: " + encryptor.authToken + "\nAgora token: " + encryptor.agoraToken, "Decrypted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}
