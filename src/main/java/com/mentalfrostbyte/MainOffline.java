package com.mentalfrostbyte;

import com.mentalfrostbyte.emulator.SigmaHandler;
import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.Config;
import com.mewebstudio.captcha.GeneratedCaptcha;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class MainOffline {

    public static Captcha captcha;
    public static GeneratedCaptcha generatedCaptcha;

    public static void main(String[] args) throws Exception {
        System.out.println(
                "     _______. __    _______ .___  ___.      ___      \n" +
                        "    /       ||  |  /  _____||   \\/   |     /   \\     \n" +
                        "   |   (----`|  | |  |  __  |  \\  /  |    /  ^  \\    \n" +
                        "    \\   \\    |  | |  | |_ | |  |\\/|  |   /  /_\\  \\   \n" +
                        ".----)   |   |  | |  |__| | |  |  |  |  /  _____  \\  \n" +
                        "|_______/    |__|  \\______| |__|  |__| /__/     \\__\\ \n"
        );
        System.out.println("Starting Sigma Emulator V3 made by lexi");

        final Config config = new Config();
        config.setWidth(70);
        config.setHeight(70);
        captcha = new Captcha(config);
        generatedCaptcha = captcha.generate();

        System.out.println("Generated a new captcha with the code: " + generatedCaptcha.getCode());

        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/", new SigmaHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Server is running on http://localhost");

        synchronized (MainOffline.class) {
            try {
                MainOffline.class.wait();
            } catch (InterruptedException e) {
                System.err.println("Server interrupted: " + e.getMessage());
            }
        }
    }
}