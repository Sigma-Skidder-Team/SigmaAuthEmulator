package team.skidma.changelog;

import java.util.List;

public class UpdateEntry {
    public String title;
    public List<String> changes;

    public UpdateEntry(String title, List<String> changes) {
        this.title = title;
        this.changes = changes;
    }
}