package team.skidma.util.system;

import team.skidma.Emulator;
import team.skidma.util.app.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class EtcHostsUtil {

    public void addToEtcHosts(
            final String ip,
            final String hostname,
            final String comment
    ) {
        if (ip.contains("http")) {
            Emulator.LOGGER.log(Level.ERROR, "IP: [" + ip + "] is not correct");
            return;
        }

        if (!canWrite()) {
            Emulator.LOGGER.log(Level.ERROR, "Etc hosts is not writable!");
            return;
        }

        if (hostname.trim().isEmpty()) {
            Emulator.LOGGER.log(Level.ERROR, "Hostname is empty");
            return;
        }

        String entryToAdd = ip + " " + hostname;

        boolean doesNotContainThisIpHostnamePair = !readEtcHostsLinesClean().contains(entryToAdd);

        if (doesNotContainThisIpHostnamePair) {
            //remove all existing bindings to this host name
            removeExistingBindings(hostname);

            ArrayList<String> etcHostsLines = readEtcHostsLinesClean();

            Emulator.LOGGER.log(Level.INFO, "Added [" + entryToAdd + "] to [" + etcHostsPath() + "]");

            etcHostsLines.add("#" + comment);
            etcHostsLines.add(entryToAdd);

            writeNewLinesToEtcHosts(etcHostsLines);
        }
    }

    private boolean canWrite() {
        return new File(etcHostsPath()).canWrite();
    }

    public void removeExistingBindings(String hostname) {
        ArrayList<String> linesWithoutBinding = readEtcHostsLinesClean().stream()
                .filter(line -> !line.matches(matchHostnameButExcludeInword(hostname)))
                .collect(Collectors.toCollection(ArrayList::new));

        writeNewLinesToEtcHosts(linesWithoutBinding);
    }

    public void removeBindingsByIp(String ip) {
        if (ip.contains("http")) {
            Emulator.LOGGER.log(Level.ERROR, "IP [" + ip + "] is not valid");
            return;
        }

        ArrayList<String> cleanedLines = readEtcHostsLinesClean().stream()
                .filter(line -> {
                    // Ignore comment lines
                    String trimmed = line.trim();
                    if (trimmed.startsWith("#") || trimmed.isEmpty()) return true;

                    // Match if the line starts with the IP followed by at least one space
                    return !trimmed.matches("^" + ip.replace(".", "\\.") + "\\s+.*");
                })
                .collect(Collectors.toCollection(ArrayList::new));

        writeNewLinesToEtcHosts(cleanedLines);
    }

    private void writeNewLinesToEtcHosts(ArrayList<String> etcHostsEntries) {
        try {
            Files.write(
                    Paths.get(etcHostsPath()),

                    String.join("\n", etcHostsEntries).getBytes(),//leave a comment so others know who changed it

                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.SYNC);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> readEtcHostsLinesClean() {
        try {
            return Files.readAllLines(Paths.get(etcHostsPath())).stream().map(String::trim).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String matchHostnameButExcludeInword(String hostname) {
        return ".*\\s" + hostname + "$";
    }

    public String etcHostsPath() {
        return isWindows() ? getWindowsFolderLocation() + etcHostsWindowsRelativePath() : linuxPathToEtcHosts();
    }

    private String linuxPathToEtcHosts() {
        return "/etc/hosts";
    }

    private String etcHostsWindowsRelativePath() {
        return "/System32/Drivers/etc/hosts";
    }

    private String getWindowsFolderLocation() {
        return System.getenv("WINDIR");
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public void removeExistingBindings(String ip, String testhostname, String comment) {
        ArrayList<String> lines = readEtcHostsLinesClean();

        int etcHostsIndex = lines.indexOf(ip + " " + testhostname);

        if (etcHostsIndex != -1) {
            lines.remove(etcHostsIndex);
            if (etcHostsIndex - 1 > 0) {
                String commentCandidate = lines.get(etcHostsIndex - 1);
                if (commentCandidate.startsWith("#") && commentCandidate.contains(comment)) {
                    lines.remove(etcHostsIndex - 1);
                }
            }
        }

        writeNewLinesToEtcHosts(lines);
    }
}