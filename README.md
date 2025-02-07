# SigmaAuthEmulator

A kinda simple authentication emulator for Sigma Jello. Except the setup part.

## Usage

> [!IMPORTANT]
> Make sure you're using Java 8 or higher. This is most likely the case.
> See [How to set Java Runtime? (Windows)](#how-to-set-java-runtime-windows)

1. First download [mkcert.dev](https://github.com/FiloSottile/mkcert/releases/tag/v1.4.4)
2. Create a certificate for these domains:
   - `jelloprg.sigmaclient.info`
   - `jelloconnect.sigmaclient.info`
   - `sigmaclient.info`
   - `wsprg.sigmaclient.info`
   - `agora.sigmaclient.info`
3. Go to `C:\Windows\System32\drivers\etc\hosts` on Windows or `/etc/hosts` on Unix-like OSes/Linux, and add these:
    - `127.0.0.1 jelloprg.sigmaclient.info`
    - `127.0.0.1 jelloconnect.sigmaclient.info`
    - `127.0.0.1 sigmaclient.info`
    - `127.0.0.1 wsprg.sigmaclient.info`
    - `127.0.0.1 agora.sigmaclient.info`
4. Flush your DNS on Windows in CMD with `ipconfig /flushdns`.
5. Using [OpenSSL](https://slproweb.com/products/Win32OpenSSL.html) generate a keystore ``.p12`` file with this command:
   ```shell
   openssl pkcs12 -export -in firstPemFile.pem -inkey secondPemFile-key.pem -out keystore.p12 -name "server" -password pass:123456`
   ```
6. Move the keystore.p12 file next to the jar file of this project or in your set [working directory](https://stackoverflow.com/questions/19838334/what-is-a-working-directory-in-intellij-idea)
7. Run the emulator.
8. Profit $$$

## Known issues

1. Problem: No premium modules work Solution: none, Sigma itself is broken which makes the modules unusable, we suggest using [Sigma Rebase](https://github.com/Sigma-Skidder-Team/SigmaRebase/).
2. Problem: Stuck on "Login" screen spinning loading circle. Solution: Press escape, the HWID getter is broken inside Sigma and nulls itself so it doesn't escape out of the menu, but doing it manually doesn't break anything.
3. Problem: Music player doesn't play/load music. Solution: Either replace the youtube-dl.exe file under `.minecraft\sigma5\music\` with [yt-dlp renamed as youtube-dl](https://github.com/yt-dlp/yt-dlp) or use [Sigma Rebase](https://github.com/Sigma-Skidder-Team/SigmaRebase/)).

## How to set Java runtime? (Windows)

> [!NOTE]
> You can use [SDKMAN!](https://sdkman.io/) if you don't want to do this, and it's also cross-platform.

1. **Install Java (if not installed)**
2. Type/paste ``SystemPropertiesAdvanced.exe`` into Win + R dialog.
3. Click on ``Environment Variables`` and then in ``System variables`` table find ``JAVA_HOME``.
4. In ``JAVA_HOME`` you set the ``Variable value`` to the main folder of the Java runtime, for example: ``C:\Program Files\Java\jdk8-u432``.
5. In ``System variables`` table find ``Path`` variable. Inside it locate or create if not already existing Java\bin folder. Example: ``C:\Program Files\Java\jdk8-u432\bin``.