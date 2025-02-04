package com.mentalfrostbyte.emulator;

import com.mentalfrostbyte.utils.FileUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
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

        String uid = "5380fed69bda466ca41a9d16d8cf38e5";

        String uri = exchange.getRequestURI().toString();
        String response = "";

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
        }

        switch (uri) {
            case "/challenge":
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);
                jsonResponse.put("uid", uid);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/login":
                jsonResponse.put("username", params.getOrDefault("username", "username"));
                jsonResponse.put("password", params.getOrDefault("password", "123"));

                jsonResponse.put("auth_token", uid);
                jsonResponse.put("agora_token", uid);

                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/register":
                jsonResponse.put("username", params.getOrDefault("username", "username"));
                jsonResponse.put("password", params.getOrDefault("password", "123"));
                jsonResponse.put("email", params.getOrDefault("email", "emailxD@gmail.com"));
                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

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

}
