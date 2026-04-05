package mobile.tests;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;

import static mobile.DriverManager.getAppiumDriver;
import static mobile.Utils.*;
import static mobile.Utils.ConnectionMode.OFF;
import static mobile.Utils.ConnectionMode.ON;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void createSession() throws MalformedURLException {
        logInfo("=== Создание тестовой сессии ===");
        driver = getAppiumDriver();
        WebDriverRunner.setWebDriver(driver);
        logInfo("Сессия успешно создана");
    }

    @AfterMethod(onlyForGroups = {"Vkvideo"}, alwaysRun = true)
    public void closeVkvideo() throws InterruptedException, MalformedURLException {
        getLogger().info("Завершение тестового приложения");
        getAppiumDriver().terminateApp("com.vk.vkvideo");
    }

    @AfterMethod(onlyForGroups = {"Network"}, alwaysRun = true)
    public void turnConnection() throws InterruptedException, MalformedURLException {
        turnNetworkConnection(ON);
    }


    @AfterSuite(alwaysRun = true)
    public void closeSession() {
        logInfo("=== Закрытие тестовой сессии ===");
        if (driver != null) {
            driver.quit();
            WebDriverRunner.closeWebDriver();
        }
        logInfo("Сессия успешно закрыта");
    }
}
