package sigma.module;

import sigma.Client;
import sigma.utils.ClientMode;
import sigma.utils.class_890;
import sigma.module.impl.FreeTestModule;
import sigma.module.impl.TestModule;

import java.util.*;

public class ModuleManager {
    private final Map<Class<? extends Module>, Module> moduleMap = new LinkedHashMap<>();
    private List<Module> modules;

    public void registerAllModules(ClientMode clientMode) {
        this.initModuleList();
        addModule(new TestModule());
        addModule(new FreeTestModule());

        sortModules();
    }

    private void initModuleList() {
        this.modules = new ArrayList<>();
    }

    private void addModule(Module var1) {
        this.modules.add(var1);
    }

    private void sortModules() {
        this.modules.sort(new ModuleNameComparator());

        for (Module mod : this.modules) {
            Client.getInstance().getEventBus().register(mod);
            this.moduleMap.put(mod.getClass(), mod);
        }

        class_890.field_4585 = true;
    }

    public List<Module> getModules() {
        return modules;
    }
}
