package emulator.impl;

import emulator.utils.VisualLogs;
import sigma.Client;
import sigma.auth.CaptchaChecker;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class RedeemTab extends JPanel {

    private final JTextField key = new JTextField();
    private final JTextField captcha = new JTextField();
    private final JLabel captchaImage = new JLabel();

    public RedeemTab(boolean needCaptcha, JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Key:"));
        add(key);

        if (needCaptcha) {
            add(new JLabel("Captcha:"));

            BufferedImage img = Client.getInstance().licenseManager.getCaptcha().getDownloadedImage();
            if (img != null) {
                captchaImage.setIcon(new ImageIcon(img));
            }

            add(captchaImage);
            add(captcha);
        }

        JButton redeemButton = new JButton("Redeem key");
        redeemButton.addActionListener(e -> redeem());
        add(redeemButton);

        tabs.add("Premium", this);
    }

    private void redeem() {
        if (Client.getInstance().licenseManager.isPremium()) {
            VisualLogs.displayInfo("You already have premium.", "Information");
            return;
        }

        new Thread(() -> {
            CaptchaChecker captcha = Client.getInstance().licenseManager.getCaptcha();
            if (captcha != null) {
                captcha.setUserAnswer(this.captcha.getText());
            }

            String response = Client.getInstance().licenseManager.premium(key.getText(), Client.getInstance().licenseManager.getCaptcha());

            if (response == null) {
                response = "";
            }

            if (!response.isEmpty()) {
                VisualLogs.displayInfo(response, "Message");
            }
        }).start();
    }

}
