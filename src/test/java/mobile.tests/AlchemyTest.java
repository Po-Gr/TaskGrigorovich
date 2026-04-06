package mobile.tests;

import mobile.pageObjects.AlchemyPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static mobile.DriverManager.getAppiumDriver;

public class AlchemyTest extends BaseTest {

    @Test(description = "Получение подсказок за просмотр рекламы",
            groups = {"Alchemy"})
    public void shouldGetHintsForAds() throws MalformedURLException {

        AlchemyPage page = new AlchemyPage(getAppiumDriver());
        int hintsBefore =
                page
                        .startGame()
                        .openYourHintsPage()
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
