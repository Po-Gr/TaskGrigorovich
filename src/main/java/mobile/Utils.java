package mobile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {

    public static Logger getLogger() {
        if (System.getProperty("log4j.configurationFile") == null) {
            System.setProperty("log4j.configurationFile", "src/test/resources/log4j2.xml");
        }
        return LogManager.getLogger();
    }

    public static void logInfo(String message, Object... params) {
        getLogger().info(message, params);
    }
}
