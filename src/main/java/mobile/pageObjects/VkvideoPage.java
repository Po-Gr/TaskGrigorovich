package mobile.pageObjects;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
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
    private final ElementsCollection fastLoginViews = $$("#fast_login_view");
    private final SelenideElement fastLoginTertiaryBtn = $("#fast_login_tertiary_btn");
    private final SelenideElement playerContainer = $("#playerContainer");
    private final ElementsCollection playerContainers = $$("#playerContainer");
    private final SelenideElement videoPlayButton = $("#video_play_button");
    private final ElementsCollection videoPlayButtons = $$("#video_play_button");
    private final ElementsCollection progressViews = $$("#progress_view");
    private final SelenideElement currentProgress = $("#current_progress");
    private final SelenideElement seekBar = $("#seek_bar");


    public VkvideoPage(WebDriver driver) {
        this.driver = driver;
    }

    public VkvideoPage openVideoByDeeplink(String url) {
        ((JavascriptExecutor) driver).executeScript("mobile: deepLink", Map.of(
                "url", url,
                "package", "com.vk.vkvideo"
        ));
        // тут что то должно быть ожидание
        closeFastLoginIfAppears();
        playerContainer.shouldBe(visible, DEFAULT_TIMEOUT);
        return this;
    }

    public VkvideoPage closeFastLoginIfAppears() {
//        $$(fastLoginView, playerContainer)
//                .filter(visible)
//                .shouldHave(CollectionCondition.sizeGreaterThan(0));
//        $("#fast_login_view, #playerContainer").shouldBe(visible, DEFAULT_TIMEOUT);

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 30000) {
            if (fastLoginViews.size() > 0 || playerContainers.size() > 0) { //вынести дублирование
                break;
            }
            Utils.waitExactTime(1);
        }

        if (fastLoginViews.size() > 0) {
            fastLoginTertiaryBtn.click();
        }
        playerContainer.shouldBe(visible, DEFAULT_TIMEOUT);


        if ($$("#fast_login_view").size() > 0) { // заменить
            fastLoginTertiaryBtn.click();
        }
        return this;
    }

    public VkvideoPage playFirstOpenedVideo() {
        if (videoPlayButtons.size() > 0) { // ifDisplayed??
            videoPlayButton.click();
            progressViews.shouldHave(size(0), LOADING_GONE_TIMEOUT);
        }
        return this;
    }

    public VkvideoPage pauseVideo() {
        playerContainer.click();
        playerContainer.click();
        videoPlayButton.shouldBe(visible);
        return this;
    }

    public int getCurrentProgress() {
        return (int) Float.parseFloat(seekBar.getText());
//        return parseSecondsFromProgressLabel(currentProgress.getText());
    }

    public VkvideoPage playVideo() {
        videoPlayButton.click();
        videoPlayButtons.shouldHave(size(0), PLAY_HIDDEN_TIMEOUT);
        return this;
    }

    private static int parseSecondsFromProgressLabel(String progressText) {
        String left = progressText.split("/")[0].trim();
        String[] mmSs = left.split(":");
        return Integer.parseInt(mmSs[mmSs.length - 1].trim());
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
