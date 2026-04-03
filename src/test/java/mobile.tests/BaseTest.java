package mobile.tests;

import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;

import static mobile.Utils.logInfo;
import static mobile.DriverManager.getAppiumDriver;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void createSession() throws MalformedURLException {
        logInfo("=== Создание тестовой сессии ===");
        driver = getAppiumDriver();
        logInfo("Сессия успешно создана");
    }

    @AfterSuite(alwaysRun = true)
    public void closeSession() {
        logInfo("=== Закрытие тестовой сессии ===");
        if (driver != null) {
            driver.quit();
        }
        logInfo("Сессия успешно закрыта");
    }
}
