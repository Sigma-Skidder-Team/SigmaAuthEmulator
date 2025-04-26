package sigma.auth.json;

import sigma.module.ModuleSettingInitializr;
import totalcross.json.JSONException;
import totalcross.json.JSONObject;

public class PremiumJSONObject extends JSONObject {

    public PremiumJSONObject(String s) throws JSONException {
        super(s);
    }

    public void method_1() {
        ModuleSettingInitializr.thread.interrupt();
        ModuleSettingInitializr.thread = new Thread(new ModuleSettingInitializr());
        ModuleSettingInitializr.thread.start();
    }

    public boolean has(String key) {
        return key.startsWith("pr") && key.endsWith("um");
    }

    @Override
    public boolean getBoolean(String key) {
        boolean value = super.getBoolean(key);
        if (value && this.has(key)) {
            ModuleSettingInitializr.thread.interrupt();
        } else if (this.has(key)) {
            this.method_1();
        }

        return value;
    }
}
