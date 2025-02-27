package com.mentalfrostbyte.emulator;

import com.mentalfrostbyte.Main;
import com.mentalfrostbyte.utils.FileUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SigmaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("\nNew connection received - Type: " + exchange.getRequestMethod());
        System.out.println("Remote address: " + exchange.getRemoteAddress());
        System.out.println("Request URI: " + exchange.getRequestURI());
        System.out.println("Headers: " + exchange.getRequestHeaders().values());
        String requestBody = FileUtil.readInputStream(exchange.getRequestBody());
        System.out.println("Request Body: " + requestBody);

        String uid = "name";

        String uri = exchange.getRequestURI().toString();
        String response = "";
        String username = "niceperson";

        JSONObject jsonResponse = new JSONObject();

        Map<String, String> params = parseFormData(requestBody);

        if (uri.contains("/changelog.php?v=")) {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("changelog.json");
            String content = getChangelog();

            if (inputStream != null) {
                content = FileUtil.toString_ByteArrayOutputStream(inputStream);
            }

            response = content;
            exchange.sendResponseHeaders(202, response.length());
        } else if (uri.contains("/captcha/") && uri.endsWith(".png")) {
            BufferedImage captchaImage = Main.generatedCaptcha.getImage();
            if (captchaImage != null) {
                exchange.getResponseHeaders().set("Content-Type", "image/png");
                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                ImageIO.write(captchaImage, "png", os);
                os.close();
                return;
            } else {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
        } else if (uri.contains("/profiles.php?v=")) {
            response = "[\"Intave\", \"BlocksMC\", \"LibreCraft\", \"Mineland\", \"Minemenclub\", \"NoRules\", \"Vulcan\", \"Old Hypixel\"]";
            exchange.sendResponseHeaders(202, response.length());
        } else {
            if (uri.contains("/profiles/") && uri.contains(".profile")) {
                String[] servers = {"Intave", "BlocksMC", "LibreCraft", "Mineland", "Minemenclub", "NoRules", "Vulcan", "Old Hypixel"};

                for (String server : servers) {
                    if (uri.contains(server)) {
                        response = getConfig(server);
                        exchange.sendResponseHeaders(202, response.length());
                        break;
                    }
                }
            }
        }

        switch (uri) {
            case "/challenge":
                jsonResponse.put("success", true);
                jsonResponse.put("uid", uid);
                jsonResponse.put("captcha", true);
                jsonResponse.put("challengeAnswer", Main.generatedCaptcha.getCode());

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/login":
                username = params.getOrDefault("username", "username");
                jsonResponse.put("username", username);
                jsonResponse.put("password", params.getOrDefault("password", "123"));

                jsonResponse.put("auth_token", uid);
                jsonResponse.put("agora_token", uid);
                jsonResponse.put("session", username);

                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/register":
                username = params.getOrDefault("username", "username");

                jsonResponse.put("username", username);
                jsonResponse.put("password", params.getOrDefault("password", "123"));
                jsonResponse.put("email", params.getOrDefault("email", "emailxD@gmail.com"));
                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/claim_premium":
                jsonResponse.put("success", true);
                jsonResponse.put("session", username);
                jsonResponse.put("username", username);
                jsonResponse.put("auth_token", uid);
                jsonResponse.put("agora_token", uid);
                jsonResponse.put("challengeAnswer", Main.generatedCaptcha.getCode());

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
        }

        System.out.println("Sending response: " + response);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String getChangelog() {
        return "[{\"title\": \"5.0.0 Beta 15 (1.16.4) Update\", \"changes\": [\"[Error] Failed to find changelog.json\"]}]";
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name()) : "";
            params.put(key, value);
        }
        return params;
    }

    private String getConfig(String configName) {
        try {
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("profiles/" + configName + ".profile");
            String content = getChangelog();

            if (inputStream != null) {
                content = FileUtil.toString_ByteArrayOutputStream(inputStream);
            }

            return content;
        } catch (IOException e) {
            return "{\"error\": \"Invalid request\"}";
        }
    }

}
