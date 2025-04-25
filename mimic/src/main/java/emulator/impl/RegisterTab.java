package emulator.impl;

import emulator.utils.VisualLogs;
import sigma.Client;
import sigma.auth.CaptchaChecker;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class RegisterTab extends JPanel {

    private final JTextField username = new JTextField();
    private final JTextField email = new JTextField();
    private final JPasswordField password = new JPasswordField();

    private final JTextField captcha = new JTextField();
    private final JLabel captchaImage = new JLabel();

    public RegisterTab(boolean needCaptcha, JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Username:"));
        add(username);

        add(new JLabel("Email:"));
        add(email);

        add(new JLabel("Password:"));
        add(password);

        if (needCaptcha) {
            add(new JLabel("Captcha:"));

            BufferedImage img = Client.getInstance().licenseManager.getCaptcha().getDownloadedImage();
            if (img != null) {
                captchaImage.setIcon(new ImageIcon(img));
            }

            add(captchaImage);
            add(captcha);
        }

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());
        add(registerButton);

        tabs.add(this);
    }

    private void register() {
        String password = new String(this.password.getPassword());

        new Thread(() -> {
            CaptchaChecker captcha = Client.getInstance().licenseManager.getCaptcha();
            if (captcha != null) {
                captcha.setUserAnswer(this.captcha.getText());
            }

            Client.getInstance().licenseManager.resetLicense();

            Client.getInstance().licenseManager.register(
                    username.getText(),
                    password,
                    email.getText(),
                    captcha
            );

            String errorMSG = Client.getInstance().licenseManager.login(username.getText(), password, captcha);
            if (errorMSG != null) {
                VisualLogs.displayError(errorMSG, "Error");
            } else {
                VisualLogs.displayInfo("You can now login.", "Success");
            }
        }).start();
    }

}
