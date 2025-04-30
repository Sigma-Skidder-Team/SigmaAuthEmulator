package sigma.auth;

import sigma.Client;
import sigma.utils.BufferedImageUtil;
import sigma.utils.interfaces.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author arithmo (maybe?)
 */
public class Challenge {
    private final String uid;

    private final boolean captcha;
    /**
     * replaced ResourceLocation since jello b1
     */
    private Texture captchaLoc = null; //
    private final long creationDate = System.currentTimeMillis();
    private boolean valid = true;
    private String answer = "";

    /**
     * only added in sigma x jello b1
     */
    private BufferedImage downloadedImage;

    public Challenge(String uid, boolean captcha) {
        this.uid = uid;
        this.captcha = captcha;

        if (captcha) {
            new Thread(() -> {
                try {
                    //original url was "https://sabrinaprg.sigmaclient.info/captcha/" + uid + ".png"
                    URL imageUrl = new URL("https://jelloprg.sigmaclient.info/captcha/" + uid + ".png");
                    this.downloadedImage = ImageIO.read(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    /**
     * only added in jello b1
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.captchaLoc != null) {
                Client.getInstance().addTexture(this.captchaLoc);
            }
        } finally {
            super.finalize();
        }
    }

    public Texture getCaptcha() {
        if (this.captchaLoc == null && this.downloadedImage != null) {
            try {
                this.captchaLoc = BufferedImageUtil.getTexture("", this.downloadedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.captchaLoc;
    }

    /**
     * we have 0 idea on what this was called originally,
     * let's just hope they used auto generated getters for these and go with the flow
     */
    public boolean isCaptcha() {
        return this.captcha;
    }

    public boolean isValid() {
        return this.valid && this.creationDate > System.currentTimeMillis() - 300000L;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUid() {
        return this.uid;
    }

    /**
     * not in original code, added for the simulator to get the captcha image more easily
     */
    public BufferedImage getDownloadedImage() {
        return downloadedImage;
    }
}
