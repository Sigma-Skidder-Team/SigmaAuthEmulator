package simulator.impl;

import simulator.utils.VisualLogs;
import sigma.Client;
import sigma.auth.CaptchaChecker;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class LoginTab extends JPanel {

    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();

    private final JTextField captcha = new JTextField();
    private final JLabel captchaImage = new JLabel();

    public LoginTab(boolean needCaptcha, JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Username:"));
        add(username);

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

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        add(loginButton);

        tabs.add(this);
    }

    private void login() {
        String password = new String(this.password.getPassword());

        new Thread(() -> {
            CaptchaChecker captcha = Client.getInstance().licenseManager.getCaptcha();
            if (captcha != null) {
                captcha.setUserAnswer(this.captcha.getText());
            }

            String errorMSG = Client.getInstance().licenseManager.login(username.getText(), password, captcha);
            if (errorMSG != null) {
                VisualLogs.displayError(errorMSG, "Error");
            } else {
                VisualLogs.displayInfo("You can now login.", "Success");
            }
        }).start();
    }

}
