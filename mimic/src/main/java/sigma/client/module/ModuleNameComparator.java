package sigma.client.module;

import java.util.Comparator;

public class ModuleNameComparator implements Comparator<Module> {
    public int compare(Module mod1, Module mod2) {
        return mod1.getName().compareTo(mod2.getName());
    }
}
