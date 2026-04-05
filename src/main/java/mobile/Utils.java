package mobile;

import io.appium.java_client.android.connection.ConnectionState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;

import static mobile.DriverManager.getAppiumDriver;

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

    public static void turnNetworkConnection(ConnectionMode connectionMode) throws MalformedURLException {
        switch (connectionMode) {
            case ON:
                getAppiumDriver().setConnection(new ConnectionState(6L));
                getLogger().info("Интернет соединение включено");
                break;
            case OFF:
                getAppiumDriver().setConnection(new ConnectionState(0));
                getLogger().info("Интернет соединение выключено");
                break;
        }
    }

    public static void waitExactTime(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            getLogger().warn("Ожидание прервано ({} с): {}", seconds, e.getMessage());
        }
    }

    public enum ConnectionMode {
        ON, OFF
    }
}
