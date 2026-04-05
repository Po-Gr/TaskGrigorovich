package mobile;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.android.connection.ConnectionState;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ThreadInfo;
import java.net.MalformedURLException;
import java.util.Arrays;

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

    public static void anyExist(int seconds, ElementsCollection ... elements) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < seconds * 1000L) {
            if (Arrays.stream(elements).anyMatch(e -> e.size() > 0)) {
                return;
            }
            Utils.waitExactTime(1);
        }
        throw new RuntimeException("На странице не найден ни один из ожидаемых элементов");
    }

    public enum ConnectionMode {
        ON, OFF
    }
}
