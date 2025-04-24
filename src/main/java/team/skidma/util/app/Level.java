package team.skidma.util.app;

public enum Level {
    DEBUG("debug :: "),
    INFO("info :: "),
    WARN("warn :: "),
    ERROR("error :: ");

    public final String prefix;

    Level(String prefix) {
        this.prefix = prefix;
    }
}
