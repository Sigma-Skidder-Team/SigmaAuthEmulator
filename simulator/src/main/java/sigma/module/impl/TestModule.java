package sigma.module.impl;

import com.google.common.eventbus.Subscribe;
import sigma.event.SecondEvent;
import sigma.module.PremiumModule;

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
