package emulator;

import sigma.Client;
import sigma.auth.CaptchaChecker;
import sigma.auth.Encryptor;
import sigma.utils.Util;
import emulator.utils.VisualLogs;
import totalcross.json.CJsonUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;

public class SimpleApp extends JFrame {
    private final JTextArea changelogArea;
    private final JTextField loginUsername;
    private final JPasswordField loginPassword;
    private final JTextField loginCaptcha;
    private final JLabel loginCaptchaImage;

    private final JTextField registerUsername;
    private final JTextField registerEmail;
    private final JPasswordField registerPassword;
    private final JTextField registerCaptcha;
    private final JLabel registerCaptchaImage;

    private final JTextField premiumKey;
    private final JTextField premiumCaptcha;
    private final JLabel premiumCaptchaImage;


    private final JTextField hwid;
    private final JTextField pathToFile;

    private boolean captchaRequired = false;

    public SimpleApp() {
        setTitle("Simple Client GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Changelog Tab
        JPanel changelogPanel = new JPanel(new BorderLayout());
        changelogArea = new JTextArea();
        changelogArea.setEditable(false);
        JButton loadChangelog = new JButton("Get Changelog");
        loadChangelog.addActionListener(e -> changelogArea.setText(CJsonUtils.formatJsonArray(Util.INSTANCE.getChangelog())));
        changelogPanel.add(new JScrollPane(changelogArea), BorderLayout.CENTER);
        changelogPanel.add(loadChangelog, BorderLayout.SOUTH);
        tabs.add("Changelog", changelogPanel);

        // Login Tab
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginUsername = new JTextField();
        loginPassword = new JPasswordField();
        loginCaptcha = new JTextField();
        loginCaptchaImage = new JLabel(new ImageIcon("captcha.png")); // Placeholder image

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());

        JButton forgotPassword = new JButton("Forgot Password?");
        forgotPassword.addActionListener(e -> openLink("https://sigmaclient.info/pwdreset.php"));

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(loginUsername);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(loginPassword);

        if (captchaRequired) {
            loginPanel.add(loginCaptchaImage);
            loginPanel.add(loginCaptcha);
        }

        loginPanel.add(loginButton);
        loginPanel.add(forgotPassword);
        tabs.add("Login", loginPanel);

        // Register Tab
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerUsername = new JTextField();
        registerEmail = new JTextField();
        registerPassword = new JPasswordField();
        registerCaptcha = new JTextField();
        registerCaptchaImage = new JLabel(new ImageIcon("captcha.png")); // Placeholder image

        JButton switchToLogin = new JButton("Back to Login");
        switchToLogin.addActionListener(e -> tabs.setSelectedIndex(1));

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());

        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(registerUsername);
        registerPanel.add(new JLabel("Email:"));
        registerPanel.add(registerEmail);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(registerPassword);

        if (captchaRequired) {
            registerPanel.add(registerCaptchaImage);
            registerPanel.add(registerCaptcha);
        }

        registerPanel.add(registerButton);
        registerPanel.add(switchToLogin);
        tabs.add("Register", registerPanel);

        // Premium Tab
        JPanel premiumPanel = new JPanel();
        premiumPanel.setLayout(new BoxLayout(premiumPanel, BoxLayout.Y_AXIS));

        premiumKey = new JTextField();

        premiumCaptcha = new JTextField();
        premiumCaptchaImage = new JLabel(new ImageIcon("captcha.png")); // Placeholder image

        JButton redeemButton = new JButton("Redeem");
        redeemButton.addActionListener(e -> redeem());

        premiumPanel.add(premiumKey);
        premiumPanel.add(redeemButton);

        if (captchaRequired) {
            premiumPanel.add(premiumCaptchaImage);
            premiumPanel.add(premiumCaptcha);
        }

        tabs.add("Premium", premiumPanel);

        // Decryptor Tab
        JPanel decryptorPanel = new JPanel();
        decryptorPanel.setLayout(new BoxLayout(decryptorPanel, BoxLayout.Y_AXIS));

        hwid = new JTextField();
        pathToFile = new JTextField();

        JButton decryptButton = new JButton("Decrypt .lic");
        decryptButton.addActionListener(e -> decrypt());

        decryptorPanel.add(hwid);
        decryptorPanel.add(pathToFile);
        decryptorPanel.add(decryptButton);

        tabs.add("Decryptor", decryptorPanel);

        add(tabs);
        setVisible(true);

        CaptchaChecker captcha = Client.getInstance().licenseManager.getCaptcha();
        if (captcha != null) {
            this.captchaRequired = (captcha.isCaptchaRequired());
        }
    }

    private void login() {
        String user = loginUsername.getText();
        String pass = new String(loginPassword.getPassword());
        String captcha = loginCaptcha.getText();

        new Thread(() -> {
            CaptchaChecker captchaChecker = Client.getInstance().licenseManager.getCaptcha();
            if (captchaChecker != null) {
                captchaChecker.setUserAnswer(captcha);
            }

            String account = Client.getInstance().licenseManager.login(user, pass, captchaChecker);
            if (account != null) {
                VisualLogs.displayError(account, "Error");
                loginUsername.setText("");
            } else {
                VisualLogs.displayInfo("You can now login.", "Success");
            }
        }).start();
    }

    private void register() {
        String user = registerUsername.getText();
        String pass = new String(registerPassword.getPassword());
        String email = registerEmail.getText();
        String captcha = registerCaptcha.getText();

        new Thread(() -> {
            CaptchaChecker captchaChecker = Client.getInstance().licenseManager.getCaptcha();
            if (captchaChecker != null) {
                captchaChecker.setUserAnswer(captcha);
            }

            Client.getInstance().licenseManager.resetLicense();
            Client.getInstance().licenseManager.register(user, pass, email, captchaChecker);
            String account = Client.getInstance().licenseManager.login(user, pass, captchaChecker);
            if (account != null) {
                VisualLogs.displayError(account, "Error");
                loginUsername.setText("");
            } else {
                VisualLogs.displayInfo("You can now login.", "Success");
            }
        }).start();
    }

    private void decrypt() {
        String path = this.pathToFile.getText();
        File file = new File(path);

        if (file == null) {
            VisualLogs.displayError("File is null", "Error");
            return;
        }

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

    private void redeem() {
        String key = premiumKey.getText();
        String captcha = premiumCaptcha.getText();

        new Thread(() -> {
            CaptchaChecker captchaChecker = Client.getInstance().licenseManager.getCaptcha();
            if (captchaChecker != null) {
                captchaChecker.setUserAnswer(captcha);
            }

            String response = Client.getInstance().licenseManager.premium(key, Client.getInstance().licenseManager.getCaptcha());
            if (response == null) {
                response = "";
            }

            VisualLogs.displayInfo(response, "Message");

            if (Client.getInstance().licenseManager.isPremium()) {
                VisualLogs.displayInfo("You have premium.", "Success");
            }
        }).start();
    }

    private void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
