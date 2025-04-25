package team.skidma.changelog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ChangelogUtil {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String getChangelog() {
        List<UpdateEntry> updates = new ArrayList<>();
        updates.add(Updates.BETA_15);
        updates.add(Updates.BETA_14);
        updates.add(Updates.BETA_13);
        updates.add(Updates.BETA_12);
        updates.add(Updates.BETA_10_and_11);
        updates.add(Updates.BETA_9);
        updates.add(Updates.BETA_8);
        updates.add(Updates.BETA_7);
        updates.add(Updates.BETA_6);
        updates.add(Updates.BETA_5);
        updates.add(Updates.BETA_4);
        updates.add(Updates.BETA_3);
        updates.add(Updates.BETA_2);
        updates.add(Updates.BETA_1);

        return GSON.toJson(updates);
    }
}