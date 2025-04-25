package sigma.client.module.impl;

import com.google.common.eventbus.Subscribe;
import sigma.client.event.SecondEvent;
import sigma.client.module.Module;

public class FreeTestModule extends Module {
    public FreeTestModule() {
        super("FreeTest");
    }

    @Subscribe
    public void test(SecondEvent event) {
        if (isEnabled()) {
            System.out.println(System.currentTimeMillis() + " - " + this.getName() + "Free");
        }
    }
}
