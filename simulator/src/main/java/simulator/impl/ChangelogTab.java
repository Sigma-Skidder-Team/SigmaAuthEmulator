package simulator.impl;

import sigma.Client;
import sigma.utils.Util;
import totalcross.json.CJsonUtils;

import javax.swing.*;

public class ChangelogTab extends JPanel {

    private final JTextArea changelogArea = new JTextArea();

    public ChangelogTab(JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("You're currently using Sigma " + Client.VERSION));
        add(new JLabel("Changelog:"));
        add(changelogArea);

        JButton changelogButton = new JButton("Get changelog");
        changelogButton.addActionListener(e -> changelogArea.setText(CJsonUtils.formatJsonArray(Util.INSTANCE.getChangelog())));
        add(changelogButton);

        tabs.add(this);
    }

}
