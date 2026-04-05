package mobile;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.LOG_LEVEL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.SESSION_OVERRIDE;
import static mobile.Utils.logInfo;
public class AppiumServerLauncher {

    public static final String ERROR_LOG_LEVEL = "error";
    private static final ThreadLocal<AppiumDriverLocalService> appiumService = new ThreadLocal<>();

    private AppiumServerLauncher() {
    }

    public static AppiumDriverLocalService start() {
        if (!isAppiumRunning()) {
            AppiumServiceBuilder builder = new AppiumServiceBuilder();
            setAppiumService(builder
                    .withIPAddress("127.0.0.1")
                    .usingAnyFreePort()
                    .withArgument(() -> "--base-path", "/wd/hub")
                    .withArgument(SESSION_OVERRIDE)
                    .withArgument(LOG_LEVEL, ERROR_LOG_LEVEL)
                    .build());
            getAppiumService().start();
            logInfo("Appium сервер запущен на {}", getAppiumService().getUrl());
        }
        return getAppiumService();
    }

    private static AppiumDriverLocalService getAppiumService() {
        return appiumService.get();
    }

    private static void setAppiumService(AppiumDriverLocalService appiumDriverLocalService) {
        appiumService.set(appiumDriverLocalService);
    }

    public static void stop() {
        if (isAppiumRunning()) {
            getAppiumService().stop();
            logInfo("Appium сервер остановлен");
        }
        setAppiumService(null);
    }

    private static boolean isAppiumRunning() {
        return getAppiumService() != null && getAppiumService().isRunning();
    }
}
