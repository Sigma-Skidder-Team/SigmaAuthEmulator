package sigma;

import com.google.common.eventbus.EventBus;
import sigma.auth.NetworkManager;
import sigma.module.ModuleManager;
import sigma.utils.ClientMode;
import sigma.utils.interfaces.Texture;
import sigma.auth.encryption.AESFixer;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private static Client INSTANCE;
    public static String VERSION = "5.0.0b15";

    public ClientMode clientMode = ClientMode.JELLO;
    public NetworkManager licenseManager;
    public ModuleManager moduleManager;
    public List<Texture> textureList = new ArrayList<>();
    public EventBus eventBus = new EventBus("JELLO");

    public void addTexture(Texture texture) {
        textureList.add(texture);
    }

    public static Client getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Client();
            AESFixer.enableUnlimitedAESKeyStrength();
            INSTANCE.licenseManager = new NetworkManager();
            INSTANCE.moduleManager = new ModuleManager();
            INSTANCE.licenseManager.loadLicense();
            INSTANCE.moduleManager.registerAllModules(INSTANCE.clientMode);
        }

        return INSTANCE;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

}
