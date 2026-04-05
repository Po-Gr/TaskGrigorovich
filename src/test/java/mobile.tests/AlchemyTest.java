package mobile.tests;

import mobile.pageObjects.AlchemyPage;
import mobile.pageObjects.VkvideoPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static mobile.DriverManager.getAppiumDriver;
import static mobile.Utils.ConnectionMode.OFF;
import static mobile.Utils.turnNetworkConnection;

public class AlchemyTest extends BaseTest {

    @Test(description = "",
            groups = {"Alchemy"})
    public void shouldNotPlaySelectedVideo() throws InterruptedException, MalformedURLException {

        AlchemyPage page = new AlchemyPage(getAppiumDriver());
        int hintsBefore =
                page
                        .startGame()
                        .openYourHintsPage() // уязвимое место из за плохого локатора
                        .getCountOfCurrentHints();

        System.out.println(hintsBefore);

        int hintsAfter =
                page
                        .getHintsForWatchingAds()
                        .getCountOfCurrentHints();

        int hintsForAds = hintsAfter - hintsBefore;
        Assert.assertEquals(hintsForAds, 2, "За просмотр рекламы было получено: " + hintsForAds + " подсказки (ожидали: 2)");
    }
}
