package sigma.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClients;
import totalcross.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Util {

    public static Util INSTANCE = new Util();

    private static JSONArray cachedChangelog;

    public JSONArray getChangelog() {
        if (cachedChangelog != null) {
            return cachedChangelog;
        }

        try {
            HttpEntity entity = HttpClients.createDefault().execute(new HttpGet("https://jelloconnect.sigmaclient.info/changelog.php?v=5.0.0b11")).getEntity();
            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    return cachedChangelog = new JSONArray(IOUtils.toString(content, StandardCharsets.UTF_8));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
