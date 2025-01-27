package com.mentalfrostbyte.emulator;

import com.mentalfrostbyte.utils.FileUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SigmaHandler implements HttpHandler {

    private boolean got = false;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("\nNew connection received - Type: " + exchange.getRequestMethod());
        System.out.println("Remote address: " + exchange.getRemoteAddress());
        System.out.println("Request URI: " + exchange.getRequestURI());
        System.out.println("Headers: " + exchange.getRequestHeaders().values());
        InputStream requestBodyStream = exchange.getRequestBody();
        String requestBody = FileUtil.readInputStream(requestBodyStream);
        System.out.println("Request Body: " + requestBody);

        String uid = "5380fed69bda466ca41a9d16d8cf38e5";

        String uri = exchange.getRequestURI().toString();
        String response = "";

        JSONObject jsonResponse = new JSONObject();

        switch (uri) {
            case "/changelog.php?v=5.0.0b15":
                File changelogFile = new File("changelog.json");
                if (changelogFile.exists()) {
                    response = FileUtil.readFile(changelogFile);
                    exchange.sendResponseHeaders(200, response.length());
                }
                break;
            case "/challenge":
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);
                jsonResponse.put("uid", uid);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(200, response.length());
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
                exchange.sendResponseHeaders(200, response.length());
                break;
            case "/register":
                jsonResponse.put("username", "username");
                jsonResponse.put("password", "123");
                jsonResponse.put("email", "emailxD@gmail.com");
                jsonResponse.put("token", uid);
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(200, response.length());
                break;
            default:
                jsonResponse.put("success", true);
                jsonResponse.put("premium", true);

                response = jsonResponse.toString();
                exchange.sendResponseHeaders(200, response.length());
                break;
        }

        System.out.println("Sending response: " + response);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
