package team.skidma.util.system;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import team.skidma.Emulator;
import team.skidma.util.app.Level;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.X509Certificate;

public class KeystoreUtil {

    public static SSLContext getOrCreateSSLContextUsingMkcert(String domain, String keystorePassword) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        File dir = new File(".");
        File certFile = null, keyFile = null;

        for (File file : dir.listFiles()) {
            String name = file.getName();
            if (domain.contains("*")) {
                name = name.replace("*", "_wildcard");
            }

            if (name.endsWith(".pem") && !name.endsWith("-key.pem") && certFile == null) {
                certFile = file;
            } else if (name.endsWith("-key.pem") && keyFile == null) {
                keyFile = file;
            }
        }

        // Download mkcert if not found
        File mkcert = new File(System.getProperty("os.name").toLowerCase().contains("win") ? "mkcert.exe" : "mkcert");
        if (!mkcert.exists()) {
            String url = System.getProperty("os.name").toLowerCase().contains("win")
                    ? "https://github.com/FiloSottile/mkcert/releases/download/v1.4.4/mkcert-v1.4.4-windows-amd64.exe"
                    : "https://github.com/FiloSottile/mkcert/releases/download/v1.4.4/mkcert-v1.4.4-linux-amd64";
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, mkcert.toPath());
                mkcert.setExecutable(true);
            }
        }

        // Generate cert with mkcert
        if (certFile == null || keyFile == null || !certFile.exists() || !keyFile.exists()) {
            Process process = new ProcessBuilder(
                    mkcert.getAbsolutePath(),
                    domain, "localhost", "127.0.0.1"
            ).inheritIO().start();
            if (process.waitFor() != 0) throw new RuntimeException("mkcert failed");
        }

        // Load cert and key using BouncyCastle
        try (
                PEMParser certReader = new PEMParser(new FileReader(certFile));
                PEMParser keyReader = new PEMParser(new FileReader(keyFile))
        ) {
            // Read certificate
            X509CertificateHolder certHolder = (X509CertificateHolder) certReader.readObject();
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter().setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);

            // Read private key
            Object keyObject = keyReader.readObject();
            if (keyObject instanceof PrivateKeyInfo) {
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) keyObject;
                JcaPEMKeyConverter keyConverter = new JcaPEMKeyConverter().setProvider("BC");
                PrivateKey privateKey = keyConverter.getPrivateKey(privateKeyInfo);

                // Create keystore and load it with the certificate and private key
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(null, null);
                ks.setKeyEntry("emulator", privateKey, keystorePassword.toCharArray(), new X509Certificate[]{certificate});

                // Initialize KeyManagerFactory and SSLContext
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                kmf.init(ks, keystorePassword.toCharArray());

                SSLContext context = SSLContext.getInstance("TLS");
                context.init(kmf.getKeyManagers(), null, new SecureRandom());
                return context;
            } else {
                throw new IllegalArgumentException("Private key not found in the key file.");
            }
        } catch (IOException e) {
            Emulator.LOGGER.log(Level.ERROR, "Error reading the key or certificate files: " + e.getMessage());
        }
        return null;
    }

}