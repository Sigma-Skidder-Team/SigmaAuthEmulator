package sigma.auth.encryption;

import javax.crypto.Cipher;
import java.lang.reflect.Field;

public class AESFixer {
    public static void enableUnlimitedAESKeyStrength() {
        try {
            if (Cipher.getMaxAllowedKeyLength("AES") < 256) {
                final Field declaredField = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
                declaredField.setAccessible(true);
                declaredField.set(null, Boolean.FALSE);
            }
        } catch (final Exception ex) {
            System.out.println("Could not override JCE cryptography strength policy setting");
            System.out.println(ex.getMessage());
        }
    }
}
