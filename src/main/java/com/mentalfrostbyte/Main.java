package com.mentalfrostbyte;

import com.mentalfrostbyte.emulator.SigmaHandler;
import com.mentalfrostbyte.utils.SSLUtil;
import com.sun.net.httpserver.*;

import javax.net.ssl.*;
import java.net.InetSocketAddress;

public class Main {

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

        SSLContext sslContext = SSLUtil.createSSLContext("keystore.p12", "123456");
        HttpsServer server = HttpsServer.create(new InetSocketAddress(443), 0);
        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                SSLContext context = getSSLContext();
                SSLEngine engine = context.createSSLEngine();
                params.setNeedClientAuth(false);
                params.setCipherSuites(engine.getEnabledCipherSuites());
                params.setProtocols(engine.getEnabledProtocols());
                params.setSSLParameters(context.getDefaultSSLParameters());
            }
        });

        server.createContext("/", new SigmaHandler());
        server.start();
        System.out.println("Server is running on https://localhost:433");

        synchronized (Main.class) {
            try {
                Main.class.wait();
            } catch (InterruptedException e) {
                System.err.println("Server interrupted: " + e.getMessage());
            }
        }
    }
}