package sigma.agora;

import javax.net.ssl.HttpsURLConnection;
import java.net.URISyntaxException;
import java.util.HashMap;

public class AgoraManager {
    private Class959 field23930;
    private static Thread field23931;
    public Class7679 field23932;

    public void method17548() {
        this.method17549();
    }

    public void method17549() {
        HashMap<String, String> neaders = new HashMap<>();
        neaders.put("Cookie", "agoratk=00");
        neaders.put("X-Forwarded-For", "1.1.1.1");
        neaders.put("User-Agent", "Agora client");
        neaders.put("Referer", "https://agora.sigmaclient.info");
        final String replaceAll = Client.getInstance().getNetworkManager().getToken().replaceAll("-", "");
        try {
            (this.field23930 = new Class959(this, "wss://wsprg.sigmaclient.info/ws/agora/" + replaceAll, neaders)).method5487(HttpsURLConnection.getDefaultSSLSocketFactory());
            this.field23930.method5453();
        } catch (final URISyntaxException exc) {
            exc.printStackTrace();
        }
    }
}
