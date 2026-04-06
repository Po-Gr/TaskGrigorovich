package mobile.tests;

import mobile.Utils;
import mobile.pageObjects.VkvideoPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import static mobile.DriverManager.getAppiumDriver;


public class VkvideoTest extends BaseTest {

    @Test(description = "Позитивный сценарий: видео воспроизводится",
            groups = {"Vkvideo"})
    public void shouldPlaySelectedVideo() throws MalformedURLException {

        VkvideoPage page = new VkvideoPage(getAppiumDriver());

        int progressBefore =
                page
                        .openVideoByDeeplink("https://vkvideo.ru/video-217020759_456240585")
                        .playFirstOpenedVideo()
                        .pauseVideo()
                        .getCurrentProgress();

        page.playVideo();

        Utils.waitExactTime(6);

        int progressAfter =
                page
                        .pauseVideo()
                        .getCurrentProgress();

        Assert.assertTrue(progressAfter - progressBefore > 1);
    }


    @Test(description = "Негативный сценарий: видео не воспроизводится по некорректной ссылке",
            groups = {"Vkvideo"})
    public void shouldNotPlayVideo() throws MalformedURLException {

        VkvideoPage page = new VkvideoPage(getAppiumDriver());

        boolean videoIsNotPlaying =
                page
                        .openVideoByDeeplink("https://vkvideo.ru/video-217020759_4562405")
                        .checkVideoNotPlaying();

        Assert.assertTrue(videoIsNotPlaying);
    }
}
