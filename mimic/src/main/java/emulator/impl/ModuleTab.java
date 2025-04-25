package emulator.impl;

import sigma.Client;
import sigma.client.module.Module;

import javax.swing.*;

public class ModuleTab extends JPanel {

    public ModuleTab(JTabbedPane tabs) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (Module module : Client.getInstance().moduleManager.getModules()) {
            JCheckBox moduleBox = new JCheckBox(module.getName());
            moduleBox.addActionListener(e -> module.setEnabled(moduleBox.isSelected()));
            add(moduleBox);
        }

        tabs.add(this);
    }

}
