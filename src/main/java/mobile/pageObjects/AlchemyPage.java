package mobile.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import mobile.Utils;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AlchemyPage {

    private final WebDriver driver;

    private final SelenideElement playButton = $x("//*[contains(@text, 'Play')]"); // добавить русские
    private final SelenideElement hintsCountOnMainPage = $x("//*[contains(@text, '2')]"); // плохо
//    private final ElementsCollection progressBar = $$x("//*[contains(@text, 'For watching ads')]//android.widget.ProgressBar"); // неверный локатор
    private final ElementsCollection progressBar = $$x("//*[contains(@text, 'For watching ads')]/following-sibling::android.view.View//android.widget.ProgressBar"); //
    private final SelenideElement watchAdsButton = $x("//*[contains(@text, 'Watch')]/../following-sibling::android.widget.Button"); // надо смотреть по кнопке спуститься вниз
    private final SelenideElement hints = $x("//*[contains(@text, 'Your hints')]/following-sibling::android.view.View//android.widget.TextView");
    private final SelenideElement hints2 = $x("//*[contains(@text,'Your hints')]/parent::android.view.View/android.view.View//android.widget.TextView");
    private final SelenideElement yourHintsText = $x("//*[contains(@text, 'Your hints')]");

    private final SelenideElement runForwardAdsButton = $("#m-playable-skip"); // надо смотреть по кнопке спуститься вниз
    private final SelenideElement closeAdsButton = $("#bigo_ad_btn_close"); // надо смотреть по кнопке спуститься вниз


    private final ElementsCollection runForwardAdsButtons = $$("#m-playable-skip"); // надо смотреть по кнопке спуститься вниз
    private final ElementsCollection closeAdsButtons = $$("#bigo_ad_btn_close"); // надо смотреть по кнопке спуститься вниз
    private final ElementsCollection yourHintsTexts = $$x("//*[contains(@text, 'Your hints')]");


    public AlchemyPage(WebDriver driver) {
        this.driver = driver;
    }

    public AlchemyPage startGame() {
        playButton.shouldBe(visible).click();
        return this;
    }

    public AlchemyPage openYourHintsPage() {
        hintsCountOnMainPage.shouldBe(visible).click();
        hints2.shouldBe(visible, Duration.ofSeconds(20));
        return this;
    }

    public int getCountOfCurrentHints() {
        return Integer.parseInt(hints2.getText());
    }

    public AlchemyPage getHintsForWatchingAds() {
        progressBar.shouldHave(size(0), Duration.ofSeconds(60)); // shouldNot не работает
        watchAdsButton.shouldBe(clickable, Duration.ofSeconds(10)).click(); // не происходит нажатие без ожидания пока лоадер пропадет
        closeAds();
        hints2.shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    public void closeAds2() {
//        $("#m-playable-skip, #bigo_ad_btn_close").shouldBe(visible, Duration.ofSeconds(100)).click(); // к сожалению не сработало

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 100000) {
            if (closeAdsButtons.size() > 0 || runForwardAdsButtons.size() > 0) {
                break;
            }
            Utils.waitExactTime(1);
        }
        if (closeAdsButtons.size() > 0) {
            closeAdsButton.click();
        } else {
            runForwardAdsButton.click();
        }

        for (int i = 0; i < 4; i++) {
            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 30000) {
                if (yourHintsTexts.size() > 0 || closeAdsButtons.size() > 0 || runForwardAdsButtons.size() > 0) { //вынести дублирование
                    break;
                }
                Utils.waitExactTime(1);
            }
            if (closeAdsButtons.size() > 0) {
                closeAdsButton.click();
            }
            else if (runForwardAdsButtons.size() > 0) {
                runForwardAdsButton.click();
            }
            i++;
        }
    }

    public void closeAds() {
//        $("#m-playable-skip, #bigo_ad_btn_close").shouldBe(visible, Duration.ofSeconds(100)).click(); // к сожалению не сработало

        Utils.anyExist(100, closeAdsButtons, runForwardAdsButtons);
        if (closeAdsButtons.size() > 0) {
            closeAdsButton.click();
        }
        else {
            runForwardAdsButton.click();
        }

        for (int i = 0; i < 4; i++) {
            Utils.anyExist(30, closeAdsButtons, runForwardAdsButtons, yourHintsTexts);
            if (closeAdsButtons.size() > 0) {
                closeAdsButton.click();
            }
            else if (runForwardAdsButtons.size() > 0) {
                runForwardAdsButton.click();
            }
            i++;
        }
    }
}
