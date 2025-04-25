package team.skidma;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import team.skidma.changelog.ChangelogUtil;
import team.skidma.util.app.Level;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response; //fallback to empty json object

        String url = exchange.getRequestURI().toString();
        String type = exchange.getRequestMethod();

        String username = "unknown";
        String email = "unknown";
        String password = "unknown";

        if (Emulator.INSTANCE.account != null) {
            username = Emulator.INSTANCE.account.username;
            email = Emulator.INSTANCE.account.email;
            password = Emulator.INSTANCE.account.password;
        }

        String requestBody = inputStreamToString(exchange.getRequestBody());
        if (!requestBody.isEmpty()) {
            Emulator.LOGGER.log(Level.DEBUG, "Incoming " + type + " request: " + url);
            Emulator.LOGGER.log(Level.DEBUG, requestBody);

            String[] params = requestBody.split("&");

            label:
            for (String param : params) {
                String[] pair = param.split("=", 2);
                if (pair.length == 2) {
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = URLDecoder.decode(pair[1], "UTF-8");

                    switch (key) {
                        case "username":
                            username = value;
                            break label;
                        case "password":
                            password = value;
                            break label;
                        case "email":
                            email = value;
                            break label;
                    }
                }
            }
        }

        if (url.contains("changelog")) {
            response = ChangelogUtil.getChangelog();
        } else if (url.contains("captcha") && url.endsWith(".png")) {
            String filename = "454babc4-24ea-42a1-85b6-249292fc5a81.png";
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (imageStream != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] temp = new byte[4096];
                int read;
                while ((read = imageStream.read(temp)) != -1) {
                    buffer.write(temp, 0, read);
                }
                byte[] imageBytes = buffer.toByteArray();

                exchange.getResponseHeaders().add("Content-Type", "image/png");
                exchange.sendResponseHeaders(200, imageBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(imageBytes);
                os.close();
                imageStream.close();
            } else {
                byte[] err = "Image not found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, err.length);
                exchange.getResponseBody().write(err);
                exchange.getResponseBody().close();
            }
            return;
        } else if (url.contains("login")) {
            JsonObject jsonResponse = getJsonObject(username);

            response = jsonResponse.toString();
        } else if (url.contains("register")) {
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("session", "yessir");

            jsonResponse.addProperty("username", Emulator.INSTANCE.account != null ? Emulator.INSTANCE.account.username : username);

            jsonResponse.addProperty("auth_token", "d4612b25-4101-44cd-badd-d9234a6ad8c6");
            jsonResponse.addProperty("agora_token", "e42cacf9-12a9-4cc0-b860-533915a1ba35");


            Emulator.INSTANCE.account = new SigmaAccount(username, email, password);
            response = jsonResponse.toString();
        } else if (url.contains("claim_premium")) {
            JsonObject jsonResponse = new JsonObject();

            if (Emulator.INSTANCE.account == null) {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("error", "Not logged in!");
            } else {
                Emulator.INSTANCE.account.premium = true;
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("session", "yessir");

                jsonResponse.addProperty("username", Emulator.INSTANCE.account.username);
                jsonResponse.addProperty("auth_token", "d4612b25-4101-44cd-badd-d9234a6ad8c6");
                jsonResponse.addProperty("agora_token", "e42cacf9-12a9-4cc0-b860-533915a1ba35");
            }

            response = jsonResponse.toString();
        } else {
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            //we later send 454babc4-24ea-42a1-85b6-249292fc5a81.png so don't change this
            jsonResponse.addProperty("uid", "454babc4-24ea-42a1-85b6-249292fc5a81");
            jsonResponse.addProperty("captcha", false);

            response = jsonResponse.toString();
        }

        if (!response.contains("5.0.0")) {
            Emulator.LOGGER.log(Level.DEBUG, response);
        } else {
            Emulator.LOGGER.log(Level.DEBUG, "sending changelog, not printing it all out");
        }

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static JsonObject getJsonObject(String username) {
        JsonObject jsonResponse = new JsonObject();
        if (Emulator.INSTANCE.account == null) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Not registered!");
        } else {
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("session", "yessir");

            if (Emulator.INSTANCE.account.premium) {
                jsonResponse.addProperty("premium", true);
            }

            jsonResponse.addProperty("username", username);
            jsonResponse.addProperty("auth_token", "d4612b25-4101-44cd-badd-d9234a6ad8c6");
            jsonResponse.addProperty("agora_token", "e42cacf9-12a9-4cc0-b860-533915a1ba35");
        }
        return jsonResponse;
    }

    private String inputStreamToString(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[256];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, len);
        }
        return sb.toString();
    }
}