# SigmaAuthEmulator
A kind of simple auth emulator Sigma Jello

## Usage
1. First download [mkcert.dev](https://github.com/FiloSottile/mkcert/releases/tag/v1.4.4)
2. Make sure your Java runtime is set to the same one [Sigma uses](https://i.imgur.com/QOMp99S.png) and make sure it's java 8(in CMD type ``java -version``). Read [Java Runtime Tutorial](#how-to-set-java-runtime-windows)
3. Create a certificate for these domains: ``jelloprg.sigmaclient.info`` ``jelloconnect.sigmaclient.info`` ``sigmaclient.info`` ``wsprg.sigmaclient.info`` and ``agora.sigmaclient.info``
4. Go to ``C:\Windows\System32\drivers\etc`` on Windows and edit the hosts file so it looks the same as in the [image](https://i.imgur.com/LfPLUjc.png).
5. Flush your DNS in CMD with ``ipconfig /flushdns``.
6. Using [OpenSSL](https://slproweb.com/products/Win32OpenSSL.html) generate a keystore ``.p12`` file with this command ```openssl pkcs12 -export -in firstPemFile.pem -inkey secondPemFile-key.pem -out keystore.p12 -name "server" -password pass:123456```
7. Move the keystore.p12 file next to the jar file of this project or in your set [working directory](https://stackoverflow.com/questions/19838334/what-is-a-working-directory-in-intellij-idea)
8. Run the emulator.
9. Profit $$$

## Known issues
1. Problem: No premium modules work Solution: none, Sigma itself is broken which makes the modules unusable, we suggest using [Sigma Rebase](https://github.com/Sigma-Skidder-Team/SigmaRebase/).
2. Problem: Stuck on "Login" screen spinning loading circle. Solution: Press escape, the HWID getter is broken inside Sigma and nulls itself so it doesn't escape out of the menu, but doing it manually doesn't break anything.
3. Problem: Music player doesn't play/load music. Solution: Either replace the youtube-dl.exe file under ``.minecraft\sigma5\music\`` with [yt-dlp renamed as youtube-dl](https://github.com/yt-dlp/yt-dlp) or use [Sigma Rebase](https://github.com/Sigma-Skidder-Team/SigmaRebase/)).

## How to set Java runtime? (Windows)
1. **Install Java (if not installed)**
2. Type/paste ``SystemPropertiesAdvanced.exe`` into Win + R dialog.
3. Click on ``Environment Variables`` and then in ``System variables`` table find ``JAVA_HOME``.
4. In ``JAVA_HOME`` you set the ``Variable value`` to the main folder of the Java runtime, for example: ``C:\Program Files\Java\jdk8-u432``.
5. In ``System variables`` table find ``Path`` variable. Inside it locate or create if not already existing Java\bin folder. Example: ``C:\Program Files\Java\jdk8-u432\bin``.