package mobile;

import java.net.MalformedURLException;
import java.net.URL;


import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;;

import static java.lang.String.format;
import static mobile.Utils.logInfo;

public class DriverManager {
    private static AndroidDriver appiumDriver;

    private DriverManager() {
    }

    public static AndroidDriver getAppiumDriver() throws MalformedURLException {
        if (appiumDriver == null) {
            appiumDriver = createDriver();
        }
        return appiumDriver;
    }

    private static AndroidDriver createDriver() throws MalformedURLException {

        URL appiumURL = new URL("http://127.0.0.1:4723");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:appActivity", "com.vk.video.screens.main.MainActivity"); // мб вынести в параметры
        capabilities.setCapability("appium:appPackage", "com.vk.vkvideo");
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
