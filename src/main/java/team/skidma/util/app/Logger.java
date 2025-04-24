package team.skidma.util.app;

/**
 * Logs stuff to the console, acts as a replacement to println
 * @author marie
 */
public class Logger {
    public void log(Level level, String str) {
        System.out.println(level.prefix + str);
    }
}