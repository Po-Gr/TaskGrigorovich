package mobile.tests;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.net.MalformedURLException;

import static mobile.DriverManager.configureSession;
import static mobile.DriverManager.getAppiumDriver;
import static mobile.Utils.*;
import static mobile.Utils.ConnectionMode.ON;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeClass(alwaysRun = true)
    public void createSession(ITestContext context) throws MalformedURLException {
        logInfo("=== Создание тестовой сессии ===");
        String appPackage = parameterOrNull(context, "appPackage");
        String appActivity = parameterOrNull(context, "appActivity");
        if (appPackage == null || appActivity == null) {
            switch (getClass().getSimpleName()) {
                case "VkvideoTest":
                    appPackage = "com.vk.vkvideo";
                    appActivity = "com.vk.video.screens.main.MainActivity";
                    break;
                case "AlchemyTest":
                    appPackage = "com.ilyin.alchemy";
                    appActivity = "com.ilyin.app_google_core.GoogleAppActivity";
                    break;
                default:
                    throw new IllegalStateException(
                            "Нет параметров в suite и неизвестный класс: " + getClass().getName());
            }
        }
        configureSession(appPackage, appActivity);
        driver = getAppiumDriver();
        WebDriverRunner.setWebDriver(driver);
        logInfo("Сессия успешно создана");
    }
    private static String parameterOrNull(ITestContext context, String name) {
        if (context.getCurrentXmlTest() == null) {
            return null;
        }
        return context.getCurrentXmlTest().getParameter(name);
    }

    @BeforeMethod(onlyForGroups = {"Vkvideo"}, alwaysRun = true)
    public void closeVkApp() throws MalformedURLException {
        getLogger().info("Завершение тестового приложения");
        getAppiumDriver().terminateApp("com.vk.vkvideo");

        getAppiumDriver().activateApp("com.vk.vkvideo");
    }

    @BeforeMethod(onlyForGroups = {"Alchemy"}, alwaysRun = true)
    public void closeAlchemyApp() throws MalformedURLException {
        getLogger().info("Завершение тестового приложения");
        getAppiumDriver().terminateApp("com.ilyin.alchemy");
        getAppiumDriver().activateApp("com.ilyin.alchemy");
    }

    @AfterMethod(onlyForGroups = {"Network"}, alwaysRun = true)
    public void turnConnection() throws MalformedURLException {
        turnNetworkConnection(ON);
    }

    @AfterTest(alwaysRun = true)
    public void closeSession() {
        logInfo("=== Закрытие тестовой сессии ===");
        if (driver != null) {
            driver.quit();
            WebDriverRunner.closeWebDriver();
            driver = null;
        }
        logInfo("Сессия успешно закрыта");
    }
}
