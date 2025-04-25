package sigma.client.module.impl;

import com.google.common.eventbus.Subscribe;
import sigma.client.event.SecondEvent;
import sigma.client.module.PremiumModule;

public class TestModule extends PremiumModule {
    public TestModule() {
        super("Test");
    }

    @Subscribe
    public void test(SecondEvent event) {
        if (isEnabled()) {
            System.out.println(System.currentTimeMillis() + " - " + this.getName() + "Premium");
        }
    }
}
