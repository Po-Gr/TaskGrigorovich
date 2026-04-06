package mobile.pageObjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import io.appium.java_client.android.AndroidDriver;
import mobile.Utils;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static mobile.Utils.logInfo;

public class AlchemyPage {

    private final WebDriver driver;

    private final SelenideElement playButton = $x("//*[contains(@text, 'Play')]");
    private final SelenideElement hintsCountOnMainPage = $x("(//*[@text='4'])[1]/parent::*"); // явно указываю индекс тк предполагается что подсказки вверху экрана, а других опор в дереые не нашла
    private final SelenideElement hintsCountOnMainPage2 = $x("//*[@text='2']"); // явно указываю индекс тк предполагается что подсказки вверху экрана, а других опор в дереые не нашла

    //    private final ElementsCollection progressBar = $$x("//*[contains(@text, 'For watching ads')]//android.widget.ProgressBar"); // неверный локатор
    private final ElementsCollection progressBar = $$x("//*[contains(@text, 'For watching ads')]/following-sibling::android.view.View//android.widget.ProgressBar");
    private final SelenideElement watchAdsButton = $x("//*[contains(@text, 'Watch')]/../following-sibling::android.widget.Button");
    private final SelenideElement hints = $x("//*[contains(@text, 'Your hints')]/following-sibling::android.view.View//android.widget.TextView");
    private final SelenideElement hints2 = $x("//*[contains(@text,'Your hints')]/parent::android.view.View/android.view.View//android.widget.TextView");
    private final SelenideElement yourHintsText = $x("//*[contains(@text, 'Your hints')]");

    private final SelenideElement runForwardAdsButton = $("#m-playable-skip");
    private final SelenideElement closeAdsButton = $("#bigo_ad_btn_close");

    private final ElementsCollection runForwardAdsButtons = $$("#m-playable-skip");
    private final ElementsCollection closeAdsButtons = $$("#bigo_ad_btn_close");
    private final ElementsCollection yourHintsTexts = $$x("//*[contains(@text, 'Your hints')]");

    public AlchemyPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Начать игру (Play)")
    public AlchemyPage startGame() {
        playButton.shouldBe(visible, Duration.ofSeconds(15)).click();
        return this;
    }

    @Step("Открыть экран Your hints")
    public AlchemyPage openYourHintsPage() {
        hintsCountOnMainPage.shouldBe(visible).click();
        tapTopLeftCorner(hintsCountOnMainPage, 30);
        hints2.shouldBe(visible, Duration.ofSeconds(20));
        return this;
    }

    /**
     * Тап по верхнему левому углу области элемента, чтобы обойти перекрытие центра
     * всплывающим окном
     */
    @Step("Тап по верхнему левому углу элемента (inset={insetPx})")
    private void tapTopLeftCorner(SelenideElement element, int insetPx) {
        Rectangle r = element.getRect();
        int x = r.getX() + Math.min(insetPx, Math.max(1, r.getWidth() / 4));
        int y = r.getY() + Math.min(insetPx, Math.max(1, r.getHeight() / 4));
        AndroidDriver androidDriver = (AndroidDriver) driver;
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        androidDriver.perform(Collections.singletonList(tap));
        logInfo("x = " + x + ", y = " + y);
    }

    @Step("Получить текущее число подсказок")
    public int getCountOfCurrentHints() {
        return Integer.parseInt(hints2.getText());
    }

    @Step("Получить подсказки за просмотр рекламы")
    public AlchemyPage getHintsForWatchingAds() {
        progressBar.shouldHave(size(0), Duration.ofSeconds(60)); // shouldNot не работает
        watchAdsButton.shouldBe(clickable, Duration.ofSeconds(10)).click(); // не происходит нажатие без ожидания пока лоадер пропадет
        closeAds();
        hints2.shouldBe(visible, Duration.ofSeconds(30));
        return this;
    }

    @Step("Закрыть рекламу")
    public void closeAds() {
//        $("#m-playable-skip, #bigo_ad_btn_close").shouldBe(visible, Duration.ofSeconds(100)).click(); // к сожалению не сработало

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 100000) {
            if (closeAdsButton.exists() || runForwardAdsButton.exists()) {
                break;
            }
            Utils.waitExactTime(1);
        }
        if (closeAdsButton.exists()) {
            closeAdsButton.click();
        } else {
            runForwardAdsButton.click();
        }

        for (int i = 0; i < 4; i++) {
            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 30000) {
                if (yourHintsText.exists() || closeAdsButton.exists() || runForwardAdsButton.exists()) { //вынести дублирование
                    break;
                }
                Utils.waitExactTime(1);
            }
            if (closeAdsButton.exists()) {
                closeAdsButton.click();
            }
            else if (runForwardAdsButton.exists()) {
                runForwardAdsButton.click();
            }
            i++;
        }
    }

    @Step("Закрыть рекламу")
    public void closeAds2() {
//        $("#m-playable-skip, #bigo_ad_btn_close").shouldBe(visible, Duration.ofSeconds(100)).click(); // к сожалению не сработало

        Utils.anyExist(100, closeAdsButtons, runForwardAdsButtons);
        if (closeAdsButton.exists()) {
            closeAdsButton.click();
        }
        else {
            runForwardAdsButton.click();
        }

        for (int i = 0; i < 4; i++) {
            Utils.anyExist(30, closeAdsButtons, runForwardAdsButtons, yourHintsTexts);
            if (closeAdsButton.exists()) {
                closeAdsButton.click();
            }
            else if (runForwardAdsButton.exists()) {
                runForwardAdsButton.click();
            }
            i++;
        }
    }
}
