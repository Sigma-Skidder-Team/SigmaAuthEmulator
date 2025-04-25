package sigma.client.module;

import sigma.Client;

public class PremiumModule extends Module{
    public PremiumModule(String name) {
        super(name);
    }

    @Override
    public boolean isEnabled() {
        return Client.getInstance().licenseManager.isPremium() ? super.isEnabled() : false;
    }

    @Override
    public void setState(boolean enabled) {
        if (Client.getInstance().licenseManager.isPremium()) {
            super.setState(enabled);
        } else {
            if (this.enabled != enabled) {
                System.out.println("No premium, sry");
            }
        }
    }

    @Override
    public void setEnabledBasic(boolean var1) {
        if (Client.getInstance().licenseManager.isPremium()) {
            super.setEnabledBasic(var1);
        } else {
            if (this.enabled != var1 && var1) {
                System.out.println("No premium, sry");
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (Client.getInstance().licenseManager.isPremium()) {
            super.setEnabled(enabled);
        } else {
            if (this.isEnabled() != enabled) {
                System.out.println("No premium, sry");
            }
        }
    }
}
