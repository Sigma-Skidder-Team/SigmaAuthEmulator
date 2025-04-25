package sigma.auth;

import sigma.Client;
import sigma.utils.BufferedImageUtil;
import sigma.utils.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CaptchaChecker {

    private final String uid;
    private final long timestamp = System.currentTimeMillis();
    private final boolean needCaptcha;

    private boolean captchaIsStillValid = true;

    private String userAnswer = "";
    private BufferedImage downloadedImage;
    private Texture texture;

    public CaptchaChecker(String uid, boolean needCaptcha) {
        this.uid = uid;
        this.needCaptcha = needCaptcha;

        if (needCaptcha) {
            new Thread(() -> {
                try {
                    URL imageUrl = new URL("https://jelloprg.sigmaclient.info/captcha/" + uid + ".png");
                    this.downloadedImage = ImageIO.read(imageUrl);
                    System.out.println(this.downloadedImage != null ? "got " + uid + ".png" : "image null");
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (this.texture != null) {
                Client.getInstance().addTexture(this.texture);
            }
        } finally {
            super.finalize();
        }
    }

    public Texture getTexture() {
        if (this.texture == null && this.downloadedImage != null) {
            try {
                this.texture = BufferedImageUtil.getTexture("", this.downloadedImage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return this.texture;
    }

    public boolean isCaptchaRequired() {
        return this.needCaptcha;
    }

    public boolean isCaptchaStillValid() {
        return this.captchaIsStillValid && this.timestamp > System.currentTimeMillis() - 300000L;
    }

    public void setCaptchaValidity(boolean valid) {
        this.captchaIsStillValid = valid;
    }

    public String getUserAnswer() {
        return this.userAnswer;
    }

    public void setUserAnswer(String answer) {
        this.userAnswer = answer;
    }

    public String getChallengeUid() {
        return this.uid;
    }

    public BufferedImage getDownloadedImage() {
        return downloadedImage;
    }
}
