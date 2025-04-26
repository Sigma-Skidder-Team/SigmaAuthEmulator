package sigma.module.impl;

import com.google.common.eventbus.Subscribe;
import sigma.event.SecondEvent;
import sigma.module.Module;

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
