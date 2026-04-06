package mobile.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import mobile.Utils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class VkvideoPage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration LOADING_GONE_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration PLAY_HIDDEN_TIMEOUT = Duration.ofSeconds(5);

    private final WebDriver driver;

    private final SelenideElement fastLoginView = $("#fast_login_view");
    private final SelenideElement fastLoginSkipBtn = $("#fast_login_tertiary_btn");
    private final SelenideElement playerContainer = $("#playerContainer");
    private final SelenideElement closeBtn = $("#close_btn_left");
    private final SelenideElement videoPlayButton = $("#video_play_button");
    private final SelenideElement currentProgress = $("#current_progress");
    private final SelenideElement seekBar = $("#seek_bar");
    private final SelenideElement progressView = $("#progress_view");

    private final ElementsCollection videoPlayButtons = $$("#video_play_button");
    private final ElementsCollection progressViews = $$("#progress_view");

    public VkvideoPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Открыть видео по deeplink: {url}")
    public VkvideoPage openVideoByDeeplink(String url) {
        ((JavascriptExecutor) driver).executeScript("mobile: deepLink", Map.of(
                "url", url,
                "package", "com.vk.vkvideo"
        ));
        closeFastLoginIfAppears();
        playerContainer.shouldBe(visible, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Закрыть быстрый логин, если показан")
    public VkvideoPage closeFastLoginIfAppears() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 30000) {
            if (fastLoginView.exists() || playerContainer.exists() || closeBtn.exists()) {
                break;
            }
            Utils.waitExactTime(1);
        }
        if (closeBtn.exists()) { // шторка при первом запуске приложения
            closeBtn.click();
        }
        if (fastLoginView.exists()) {
            fastLoginSkipBtn.shouldBe(visible).click();
        }
        playerContainer.shouldBe(visible, Duration.ofSeconds(70));
        return this;
    }

    @Step("Запустить воспроизведение видео")
    public VkvideoPage playFirstOpenedVideo() {
        if (videoPlayButton.exists()) {
            videoPlayButton.click();
            progressViews.shouldHave(size(0), LOADING_GONE_TIMEOUT);
        }
        return this;
    }

    @Step("Поставить видео на паузу (двойной тап по плееру)")
    public VkvideoPage pauseVideo() {
        playerContainer.click();
        playerContainer.click();
        videoPlayButton.shouldBe(visible);
        return this;
    }

    @Step("Прочитать текущий прогресс воспроизведения")
    public int getCurrentProgress() {
        return (int) Float.parseFloat(seekBar.getText());
    }

    @Step("Возобновить воспроизведение видео")
    public VkvideoPage playVideo() {
        videoPlayButton.click();
        videoPlayButtons.shouldHave(size(0), PLAY_HIDDEN_TIMEOUT);
        return this;
    }

    @Step("Разобрать строку прогресса в секунды: {progressText}")
    private static int parseSecondsFromProgressLabel(String progressText) {
        String left = progressText.split("/")[0].trim();
        String[] mmSs = left.split(":");
        return Integer.parseInt(mmSs[mmSs.length - 1].trim());
    }

    @Step("Проверить, что видео не воспроизводится")
    public boolean checkVideoNotPlaying() {
        if (videoPlayButton.exists()) {
            videoPlayButton.click();
        }
        Utils.waitExactTime(10);
        playerContainer.click();
        playerContainer.click();
        if (seekBar.exists()) {
            return getCurrentProgress() == 0;
        }
        playerContainer.click();
        if (seekBar.exists()) {
            return getCurrentProgress() == 0;
        }
        return true;
    }
}




































//package mobile.pageObjects;
//
//import com.codeborne.selenide.ElementsCollection;
//import com.codeborne.selenide.SelenideElement;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//
//import java.time.Duration;
//import java.util.Map;
//
//import static com.codeborne.selenide.CollectionCondition.size;
//import static com.codeborne.selenide.Condition.exist;
//import static com.codeborne.selenide.Condition.visible;
//import static com.codeborne.selenide.Selenide.$;
//import static com.codeborne.selenide.Selenide.$$;
//
///**
// * Page Object главного сценария VK Видео.
// * Поля {@link SelenideElement} / {@link ElementsCollection} — прокси Selenide, поиск при действии.
// */
//public class VkvideoMainPage {
//
//    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
//    private static final Duration LOADING_GONE_TIMEOUT = Duration.ofSeconds(10);
//
//    private final WebDriver driver;
//
//    private final SelenideElement fastLoginView = $("#fast_login_view");
//    private final SelenideElement fastLoginTertiaryBtn = $("#fast_login_tertiary_btn");
//    private final SelenideElement playerContainer = $("#playerContainer");
//    private final SelenideElement videoPlayButton = $("#video_play_button");
//    private final ElementsCollection progressViews = $$("#progress_view");
//    private final ElementsCollection videoPlayButtons = $$("#video_play_button");
//    private final SelenideElement currentProgress = $("#current_progress");
//    private final SelenideElement seekBar = $("#seek_bar");
//
//    public VkvideoMainPage(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    public VkvideoMainPage openVkVideoDeepLink(String url) {
//        ((JavascriptExecutor) driver).executeScript("mobile: deepLink", Map.of(
//                "url", url,
//                "package", "com.vk.vkvideo"
//        ));
//        return this;
//    }
//
//    public VkvideoMainPage dismissFastLoginOverlay() {
//        fastLoginView.should(exist, DEFAULT_TIMEOUT);
//        fastLoginTertiaryBtn.click();
//        return this;
//    }
//
//    public VkvideoMainPage waitForPlayerVisible() {
//        playerContainer.shouldBe(visible, DEFAULT_TIMEOUT);
//        return this;
//    }
//
//    public VkvideoMainPage startPlaybackIfStopped() {
//        if (videoPlayButtons.size() > 0) {
//            videoPlayButton.click();
//        }
//        return this;
//    }
//
//    public VkvideoMainPage waitUntilLoadingIndicatorGone() {
//        progressViews.shouldHave(size(0), LOADING_GONE_TIMEOUT);
//        return this;
//    }
//
//    public VkvideoMainPage pauseByDoubleTapPlayer() {
//        playerContainer.click();
//        playerContainer.click();
//        return this;
//    }
//
//    public VkvideoMainPage assertPausedOverlayVisible() {
//        videoPlayButton.shouldBe(visible);
//        return this;
//    }
//
//    public VkvideoMainPage resumePlayback() {
//        videoPlayButton.click();
//        return this;
//    }
//
//    public VkvideoMainPage waitUntilPlayButtonHidden(Duration timeout) {
//        videoPlayButtons.shouldHave(size(0), timeout);
//        return this;
//    }
//
//    //убрать
//    public String getCurrentProgressText() {
//        return currentProgress.getText();
//    }
//
//    public float getSeekBarValue() {
//        return Float.parseFloat(seekBar.getText());
//    }
//
//    public int getCurrentPositionSeconds() {
//        return parseSecondsFromProgressLabel(getCurrentProgressText());
//    }
//
//    private static int parseSecondsFromProgressLabel(String progressText) {
//        String left = progressText.split("/")[0].trim();
//        String[] mmSs = left.split(":");
//        return Integer.parseInt(mmSs[mmSs.length - 1].trim());
//    }
//}
