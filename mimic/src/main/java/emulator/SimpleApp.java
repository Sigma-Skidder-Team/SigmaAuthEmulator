package emulator;

import emulator.impl.*;
import sigma.Client;
import sigma.auth.CaptchaChecker;
import sigma.client.event.SecondEvent;

import javax.swing.*;

public class SimpleApp extends JFrame {
    private boolean captchaRequired = false;

    public SimpleApp() {
        setTitle("Sigma Jello auth simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        new Timer(1000, e -> Client.getInstance().eventBus.post(new SecondEvent())).start();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("License", new LicenseTab(tabs));
        tabs.add("Changelog", new ChangelogTab(tabs));
        tabs.add("Modules", new ModuleTab(tabs));
        tabs.add("Register", new RegisterTab(captchaRequired, tabs));
        tabs.add("Login", new LoginTab(captchaRequired, tabs));
        tabs.add("Redeem", new RedeemTab(captchaRequired, tabs));

        CaptchaChecker captcha = Client.getInstance().licenseManager.getCaptcha();
        if (captcha != null) {
            this.captchaRequired = captcha.isCaptchaRequired();
        }

        add(tabs);
        setVisible(true);
    }

}
