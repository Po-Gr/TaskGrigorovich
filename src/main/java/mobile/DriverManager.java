package mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static mobile.Utils.logInfo;

public class DriverManager {
    private static AndroidDriver appiumDriver;
    private static String appPackage;
    private static String appActivity;

    private DriverManager() {}

    public static synchronized AndroidDriver getAppiumDriver() throws MalformedURLException {
        if (appiumDriver == null) {
            if (appPackage == null || appActivity == null) {
                throw new IllegalStateException("Для сессии не установлены необходимые параметры appPackage, appActivity");
            }
            appiumDriver = createDriver(appPackage, appActivity);
        }
        return appiumDriver;
    }

    public static synchronized void configureSession(String newAppPackage, String newAppActivity) {
        if (newAppPackage == null || newAppPackage.isBlank() ||
                newAppActivity == null || newAppActivity.isBlank()) {
            throw new IllegalArgumentException("Для сессии не установлены необходимые параметры appPackage, appActivity");
        }
        boolean changed = !newAppPackage.equals(appPackage) || !newAppActivity.equals(appActivity);
        appPackage = newAppPackage;
        appActivity = newAppActivity;
        if (changed && appiumDriver != null) {
            appiumDriver.quit();
            appiumDriver = null;
        }
    }

    public static synchronized void closeDriver() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            appiumDriver = null;
        }
    }

    private static AndroidDriver createDriver(String appPackage, String appActivity) throws MalformedURLException {

//        URL appiumURL = new URL("http://127.0.0.1:4723");
        URL appiumURL = AppiumServerLauncher.start().getUrl();
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");

        capabilities.setCapability("appium:appActivity", appActivity);
        capabilities.setCapability("appium:appPackage", appPackage);

        capabilities.setCapability("appium:automationName", "uiautomator2");
        capabilities.setCapability("appium:platformVersion", "11.0");
        capabilities.setCapability("appium:udid", "emulator-5554");
        capabilities.setCapability("appium:noReset", true);
        capabilities.setCapability("appium:dontStopAppOnReset", true);
        capabilities.setCapability("appium:newCommandTimeout", 300);
        capabilities.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        capabilities.setCapability("appium:androidInstallTimeout", 90000);

        logInfo("Создание сессии с capabilities: {}", capabilities);
        appiumDriver = new AndroidDriver(appiumURL, capabilities);

        return appiumDriver;
    }
}
