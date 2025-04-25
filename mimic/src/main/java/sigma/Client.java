package sigma;

import sigma.auth.NetworkManager;
import sigma.utils.Texture;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private static Client INSTANCE;

    public ClientMode clientMode = ClientMode.JELLO;
    public NetworkManager licenseManager;
    public List<Texture> textureList = new ArrayList<>();

    public void addTexture(Texture texture) {
        textureList.add(texture);
    }

    public static Client getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Client();
            INSTANCE.licenseManager = new NetworkManager();
            INSTANCE.licenseManager.loadLicense();
        }

        return INSTANCE;
    }

}
