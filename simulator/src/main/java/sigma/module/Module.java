package sigma.module;

import sigma.Client;

public class Module {
    public final String name;
    public boolean enabled;

    public Module(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return this.name;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void setEnabledBasic(boolean var1) {
        this.enabled = var1;
        if (!this.enabled) {
            Client.getInstance().getEventBus().unregister(this);
        } else {
            Client.getInstance().getEventBus().register(this);
        }
    }

    public void setEnabled(boolean var1) {
        if (this.enabled != var1) {
            if (!(this.enabled = var1)) {
                Client.getInstance().getEventBus().unregister(this);
                this.onDisable();
            } else {
                Client.getInstance().getEventBus().register(this);
                this.onEnable();
            }
        }
    }

    public void setState(boolean enabled) {
        if (this.enabled != enabled) {
            if (!(this.enabled = enabled)) {
                Client.getInstance().getEventBus().unregister(this);
                this.onDisable();
            } else {
                Client.getInstance().getEventBus().register(this);
                this.onEnable();
            }
        }
    }
}
