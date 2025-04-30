package simulator;

import simulator.impl.*;
import sigma.Client;
import sigma.auth.Challenge;
import sigma.event.SecondEvent;

import javax.swing.*;

public class AuthSimulator extends JFrame {
    private boolean captchaRequired = false;

    public AuthSimulator() {
        setTitle("Sigma Jello auth simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        new Timer(1000, e -> Client.getInstance().eventBus.post(new SecondEvent())).start();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Changelog", new ChangelogTab(tabs));
        tabs.add("License", new LicenseTab(tabs));
        tabs.add("Modules", new ModuleTab(tabs));
        tabs.add("Register", new RegisterTab(captchaRequired, tabs));
        tabs.add("Login", new LoginTab(captchaRequired, tabs));
        tabs.add("Redeem", new RedeemTab(captchaRequired, tabs));

        Challenge challenge = Client.getInstance().licenseManager.getCaptcha();
        if (challenge != null) {
            this.captchaRequired = challenge.isCaptcha();
        }

        add(tabs);
        setVisible(true);
    }

}
