package sigma.client.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ModuleSettingInitializr implements Runnable{

    public static Thread thread = new Thread(new ModuleSettingInitializr());
    public static HashMap<Object, Integer> field_6813;

    static {
        thread.start();
    }

    @Override
    public void run() {
        field_6813 = new HashMap<Object, Integer>();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(150000L);
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            } catch (InterruptedException var8) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
