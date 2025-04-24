package team.skidma;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import team.skidma.util.IEmulator;
import team.skidma.util.system.EtcHostsUtil;
import team.skidma.util.app.Level;
import team.skidma.util.app.Logger;
import team.skidma.util.system.KeystoreUtil;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;

public class Emulator implements IEmulator {

    public static final Emulator INSTANCE = new Emulator();
    public static final Logger LOGGER = new Logger();
    public static final EtcHostsUtil ETCHOSTS = new EtcHostsUtil();

    public SigmaAccount account;

    public static void main(String[] args) throws Exception {
        INSTANCE.run();
    }

    public void run() throws Exception {
        LOGGER.log(Level.INFO, "Skidma emulator - powered by fent reactor hq");
        LOGGER.log(Level.INFO, "Sponsored by microsoft nigga link ai");

        ETCHOSTS.removeExistingBindings("127.0.0.1", "jelloprg.sigmaclient.info", "skidma emulator");
        ETCHOSTS.removeExistingBindings("127.0.0.1", "jelloconnect.sigmaclient.info", "skidma emulator");
        ETCHOSTS.addToEtcHosts("127.0.0.1", "jelloprg.sigmaclient.info", "skidma emulator");
        ETCHOSTS.addToEtcHosts("127.0.0.1", "jelloconnect.sigmaclient.info", "skidma emulator");

        SSLContext sslContext = KeystoreUtil.getOrCreateSSLContextUsingMkcert("*.sigmaclient.info", "changeit");

        HttpsServer server = HttpsServer.create(new InetSocketAddress(443), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                params.setSSLParameters(sslContext.getDefaultSSLParameters());
            }
        });

        server.createContext("/", new Handler());

        server.setExecutor(null);
        server.start();

        LOGGER.log(Level.INFO, "Emulator listening on port " + 443 + "!");
    }

}
