package sigma.auth.hwid;

import com.sun.jna.Platform;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HardwareIDGenerator {
   public static byte[] generateHardwareID(String var0) {
      SerialNumberRetriever var3 = getSystemSerialNumberRetriever();
      String serialData = var0 + var3 == null ? "Unknown" : SerialNumberRetriever.field_35448 + var3.getSerialNumber();

      try {
         MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
         return sha256.digest(serialData.getBytes(StandardCharsets.UTF_8));
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
         byte[] fallbackHash = new byte[256];

         for (int i = 0; i < 256; i++) {
            fallbackHash[i] = 15;
         }

         return fallbackHash;
      }
   }

   public static SerialNumberRetriever getSystemSerialNumberRetriever() {
      if (!Platform.isMac()) {
         if (!Platform.isWindows()) {
            return !Platform.isLinux() ? null : new MacSerialNumberRetriever();
         } else {
            return new WindowsSerialNumberRetriever();
         }
      } else {
         return new LinuxSerialNumberRetriever();
      }
   }
}