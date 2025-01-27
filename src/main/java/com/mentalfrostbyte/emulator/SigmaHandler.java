package com.mentalfrostbyte.emulator;

import com.mentalfrostbyte.utils.FileUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;

public class SigmaHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("\nNew connection received - Type: " + exchange.getRequestMethod());
        System.out.println("Remote address: " + exchange.getRemoteAddress());
        System.out.println("Request URI: " + exchange.getRequestURI());
        System.out.println("Headers: " + exchange.getRequestHeaders().values());
        System.out.println("Request Body: " + FileUtil.readInputStream(exchange.getRequestBody()));

        String uid = "5380fed69bda466ca41a9d16d8cf38e5";

        String uri = exchange.getRequestURI().toString();
        String response;

        JSONObject jsonResponse = new JSONObject();

        switch (uri) {
            case "/changelog.php?v=5.0.0b15":
                InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("changelog.json");
                String content = getChangelog();

                if (inputStream != null) {
                    content = FileUtil.toString_ByteArrayOutputStream(inputStream);
                }

                response = content;
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/challenge":
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);
                jsonResponse.put("uid", uid);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/login":
                jsonResponse.put("username", "username");
                jsonResponse.put("password", "123");

                jsonResponse.put("auth_token", uid);
                jsonResponse.put("agora_token", uid);

                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            case "/register":
                jsonResponse.put("username", "username");
                jsonResponse.put("password", "123");
                jsonResponse.put("email", "emailxD@gmail.com");
                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(202, response.length());
                break;
            default:
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

}
