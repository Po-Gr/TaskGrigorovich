package mobile.tests;

import mobile.Utils;
import mobile.pageObjects.VkvideoPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static mobile.DriverManager.getAppiumDriver;
import static mobile.Utils.ConnectionMode.OFF;
import static mobile.Utils.turnNetworkConnection;

public class VkvideoTest extends BaseTest {

//    @Test(description = "Позитивный сценарий: видео воспроизводится",
//            groups = {"Vkvideo"})
//    public void shouldPlaySelectedVideo() throws InterruptedException, MalformedURLException {
//
////        getAppiumDriver().get("https://vkvideo.ru/video-211229778_456240419");
//
//        ((JavascriptExecutor) driver).executeScript("mobile: deepLink", Map.of(
//                "url", "https://vkvideo.ru/video-211229778_456240419",
//                "package", "com.vk.vkvideo"
//        ));
//
////        if ($$("#fast_login_view").size() > 0) {
////            $("#fast_login_tertiary_btn").click(); // этот локатор мне не нравится
////        }
//
//        $("#fast_login_view").should(exist, Duration.ofSeconds(20));
//        $("#fast_login_tertiary_btn").click(); // этот локатор мне не нравится
//
//        $("#playerContainer").shouldBe(visible);
//
//        if ($$("#video_play_button").size() > 0) {
//            System.out.println("ВИДЕО ИЗНАЧАЛЬНО БЫЛО НЕ ЗАПУЩЕНО");
//            $("#video_play_button").click();
//            System.out.println("Запустили");
//        }
//        $$("#progress_view").shouldHave(size(0), Duration.ofSeconds(10));
//
//        $("#playerContainer").click();
//        $("#playerContainer").click();
//        System.out.println("Остановили 2 раза нажали");
//        $("#video_play_button").shouldBe(visible);
//        String progress = $("#current_progress").getText();
//        System.out.println(progress);
//        float seekBar = Float.parseFloat($("#seek_bar").getText());
//        $("#video_play_button").click();
//        System.out.println("запустили");
//        $$("#video_play_button").shouldHave(size(0), Duration.ofSeconds(5)); // was not correct
//        Thread.sleep(10000);
//        $("#playerContainer").click();
//        $("#playerContainer").click();
//        System.out.println("Остановили 2 раза нажали");
//        String progress2 = $("#current_progress").getText();
//        float seekBar2 = Float.parseFloat($("#seek_bar").getText());
//        System.out.println(progress2);
//
//        int seconds = Integer.parseInt(progress.split("/")[0].trim().split(":")[1]);
//        int seconds2 = Integer.parseInt(progress2.split("/")[0].trim().split(":")[1]);
//        Assert.assertTrue(seconds2 - seconds > 2 & seekBar2 - seekBar > 2);
//    }

    @Test(description = "Позитивный сценарий: видео воспроизводится",
            groups = {"Vkvideo"})
    public void shouldPlaySelectedVideo() throws MalformedURLException {

        VkvideoPage page = new VkvideoPage(getAppiumDriver());

        int progressBefore =
                page
                        .openVideoByDeeplink("https://vkvideo.ru/video-211229778_456240419")
                        .playFirstOpenedVideo()
                        .pauseVideo()
                        .getCurrentProgress();

        page.playVideo();

        Utils.waitExactTime(10);

        int progressAfter =
                page
                        .pauseVideo()
                        .getCurrentProgress();

        Assert.assertTrue(progressAfter - progressBefore > 1);
    }


    @Test(description = "Негативный сценарий: видео не воспроизводится",
            groups = {"Vkvideo", "Network"})
    public void shouldNotPlaySelectedVideo() throws InterruptedException, MalformedURLException {
        $("#fast_login_view").should(exist, Duration.ofSeconds(20));
        $("#fast_login_tertiary_btn").click(); // этот локатор мне не нравится

        $$("#preview").first().shouldBe(visible);

        turnNetworkConnection(OFF);
        Thread.sleep(3000);
        $$("#preview").first().click();

        $("#playerContainer").shouldBe(visible);
        $("#progress_view").should(exist, Duration.ofSeconds(5));
        Thread.sleep(5000);
        $("#progress_view").should(exist);
        $("#playerContainer").click();
//        $("#video_play_button").shouldNot(exist);
        $("#playerContainer").click();
        $("#video_play_button").shouldNot(exist);
        $("#current_progress").shouldNot(exist);
    }
}
