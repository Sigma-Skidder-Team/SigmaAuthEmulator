package sigma.auth;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import sigma.auth.encryption.Encryptor;
import sigma.auth.json.PremiumJSONObject;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NetworkManager {

    public HttpClient httpClient;
    public Challenge captcha;
    public Encryptor encryptor;

    public String mainURL;
    public String loginUrl;
    public String registerUrl;
    public String claimPremiumUrl;
    public String challengeUrl;
    public String session;
    public String token;

    public static boolean premium = false;
    public static boolean field25696 = false;

    public NetworkManager() {
        this.mainURL = "https://jelloprg.sigmaclient.info/";
        this.loginUrl = this.mainURL + "/login";
        this.registerUrl = this.mainURL + "/register";
        this.claimPremiumUrl = this.mainURL + "/claim_premium";
        this.challengeUrl = this.mainURL + "/challenge";
        this.token = UUID.randomUUID().toString().replaceAll("-", "");
        this.httpClient = HttpClients.createDefault();
    }

    public String login(String username, String password, Challenge challenge) {
        String errorMSG = "Unexpected error";

        try {
            HttpPost request = new HttpPost(this.loginUrl);
            List<BasicNameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            pairs.add(new BasicNameValuePair("challengeUid", challenge.getUid()));
            pairs.add(new BasicNameValuePair("challengeAnswer", challenge.getAnswer()));
            pairs.add(new BasicNameValuePair("token", this.token));

            challenge.setValid(false);
            request.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));

            HttpResponse response = this.httpClient.execute(request);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    String formattedContent = IOUtils.toString(content, StandardCharsets.UTF_8);
                    PremiumJSONObject jsonObject = new PremiumJSONObject(formattedContent);

                    if (jsonObject.getBoolean("success")) {
                        if (jsonObject.has("premium")) {
                            boolean isPremium = jsonObject.getBoolean("premium");
                            new Thread(new PremiumChecker(isPremium)).start();
                        }

                        this.handleLoginResponse(jsonObject);
                        return null;
                    }

                    if (jsonObject.has("error")) {
                        errorMSG = jsonObject.getString("error");
                    }
                }
            }
        } catch (Exception e) {
            errorMSG = e.getMessage();
            e.printStackTrace();
        }

        return errorMSG;
    }

    public String register(String username, String password, String email, Challenge challenge) {
        if (email == null) {
            email = "";
        }

        String errorMSG = "Unexpected error";

        try {
            HttpPost request = new HttpPost(this.registerUrl);
            List<BasicNameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            pairs.add(new BasicNameValuePair("email", email));
            pairs.add(new BasicNameValuePair("challengeUid", challenge.getUid()));
            pairs.add(new BasicNameValuePair("challengeAnswer", challenge.getAnswer()));
            pairs.add(new BasicNameValuePair("token", this.token));

            challenge.setValid(false);
            request.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));

            HttpEntity entity = this.httpClient.execute(request).getEntity();
            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    PremiumJSONObject jsonObject = new PremiumJSONObject(IOUtils.toString(content, StandardCharsets.UTF_8));

                    if (jsonObject.getBoolean("success")) {
                        this.handleLoginResponse(jsonObject);
                        return null;
                    }

                    if (jsonObject.has("error")) {
                        errorMSG = jsonObject.getString("error");
                    }
                }
            }
        } catch (IOException exc) {
            errorMSG = exc.getMessage();
            exc.printStackTrace();
        }

        return errorMSG;
    }

    public String validateToken() {
        String errorMSG = "Unexpected error";

        try {
            HttpPost request = new HttpPost(this.loginUrl);
            List<BasicNameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("auth_token", this.encryptor.authToken));
            pairs.add(new BasicNameValuePair("token", this.token));
            request.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));

            HttpResponse response = this.httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    String formattedContent = IOUtils.toString(content, StandardCharsets.UTF_8);
                    PremiumJSONObject jsonObject = new PremiumJSONObject(formattedContent);

                    if (jsonObject.getBoolean("success")) {
                        if (jsonObject.has("premium")) {
                            new Thread(new PremiumChecker(jsonObject.getBoolean("premium"))).start();
                        }

                        this.handleLoginResponse(jsonObject);
                        return null;
                    }

                    if (jsonObject.has("error")) {
                        errorMSG = jsonObject.getString("error");
                    }
                }
            }
        } catch (IOException exc) {
            errorMSG = exc.getMessage();
            exc.printStackTrace();
        }

        return errorMSG;
    }

    public void loadLicense() {
        if (this.encryptor != null) {
            return;
        }

        File file = new File("jello/jello.lic");
        if (file.exists()) {
            try {
                this.encryptor = new Encryptor(Files.readAllBytes(file.toPath()));
                if (this.encryptor.username == null || this.encryptor.username.isEmpty()) {
                    this.encryptor = null;
                }

                if (this.validateToken() != null) {
                    this.encryptor = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String premium(String key, Challenge challenge) {
        String errorMSG = "Unexpected error";

        try {
            HttpPost request = new HttpPost(this.claimPremiumUrl);
            List<BasicNameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair("key", key));
            pairs.add(new BasicNameValuePair("challengeUid", challenge.getUid()));
            pairs.add(new BasicNameValuePair("challengeAnswer", challenge.getAnswer()));
            pairs.add(new BasicNameValuePair("token", this.token));
            challenge.setValid(false);

            request.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
            HttpEntity entity = this.httpClient.execute(request).getEntity();

            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    PremiumJSONObject jsonObject = new PremiumJSONObject(IOUtils.toString(content, StandardCharsets.UTF_8));

                    if (jsonObject.getBoolean("success")) {
                        this.handleLoginResponse(jsonObject);
                        return null;
                    }

                    if (jsonObject.has("error")) {
                        errorMSG = jsonObject.getString("error");
                    }
                    return errorMSG;
                }
            }
        } catch (IOException exc) {
            errorMSG = exc.getMessage();
            exc.printStackTrace();
        }
        return errorMSG;
    }

    public void validateSession(final String session) {
        if (!session.equals("error")) {
            this.session = session;
        } else {
            this.session = null;
        }
    }

    public Encryptor getEncryptor() {
        return this.encryptor;
    }

    public void resetLicense() {
        this.session = null;
        this.encryptor = null;
        premium = false;

        final File file = new File("jello/jello.lic");
        if (file.exists()) {
            file.delete();
        }
    }

    public Challenge getCaptcha() {
        if (this.captcha != null && this.captcha.isValid()) {
            return this.captcha;
        }

        try {
            HttpPost request = new HttpPost(this.challengeUrl);
            List<BasicNameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("token", this.token));
            request.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));

            HttpEntity entity = this.httpClient.execute(request).getEntity();
            if (entity != null) {
                try (InputStream content = entity.getContent()) {
                    PremiumJSONObject jsonObject = new PremiumJSONObject(IOUtils.toString(content, StandardCharsets.UTF_8));

                    if (jsonObject.getBoolean("success")) {
                        String uid = jsonObject.getString("uid");
                        boolean captcha = false;

                        if (jsonObject.has("captcha")) {
                            captcha = jsonObject.getBoolean("captcha");
                        }

                        this.captcha = new Challenge(uid, captcha);
                        return this.captcha;
                    }
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleLoginResponse(final JSONObject json) throws JSONException {
        String authToken = null;
        String username = null;
        String agoraToken = null;
        if (json.has("username")) {
            System.out.println("has username");
            username = json.getString("username");
        }
        if (json.has("auth_token")) {
            System.out.println("has auth token");
            authToken = json.getString("auth_token");
        }
        if (json.has("agora_token")) {
            System.out.println("has agora token");
            agoraToken = json.getString("agora_token");
        }
        if (authToken != null && username != null && agoraToken != null) {
            try {
                this.encryptor = new Encryptor(username, authToken, agoraToken);
                FileUtils.writeByteArrayToFile(new File("jello/jello.lic"), this.encryptor.encrypt());
            } catch (final IOException ex) {
            }
        }
        if (json.has("session")) {
            this.validateSession(json.getString("session"));
        }
    }

    public boolean isPremium() {
        return premium;
    }
}