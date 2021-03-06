package com.kazurayam.webdriverfactory.firefox;

import com.kazurayam.webdriverfactory.utils.OSIdentifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FirefoxDriverUtils {
    /**
     * @param logsDir TODO
     * @throws IOException TODO
     */
    public static void enableFirefoxDriverLog(Path logsDir) throws IOException {
        Files.createDirectories(logsDir);
        Path firefoxDriverLog = logsDir.resolve("firefoxdriver.log");
        System.setProperty("webdriver.gecko.logfile", firefoxDriverLog.toString());
        System.setProperty("webdriver.gecko.verboseLogging", "true");
    }

    /**
     * @return the path of the binary of Firefox browser
     */
    public static Path getFirefoxBinaryPath() {
        if (OSIdentifier.isWindows()) {
            throw new RuntimeException("TODO");
        } else if (OSIdentifier.isMac()) {
            return Paths.get("/Applications/Firefox.app/Contents/MacOS/firefox");
        } else if (OSIdentifier.isUnix()) {
            return Paths.get("/usr/bin/firefox");
        } else {
            throw new IllegalStateException("Windows, Mac, Linux are supported. Other platforms are not supported.");
        }

    }

}
