package mobile.tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class VkvideoTest extends BaseTest {

    @Test(description = "Позитивный сценарий: видео воспроизводится",
            groups = {"Vkvideo"})
    public void shouldPlaySelectedVideo() {
//        getAndroidDriver().findElement(By.xpath("//*[contains(@resource-id,'video_display')]")).click();
        if ($$("#fast_login_view").size() > 0) {
            $("#fast_login_tertiary_btn").click(); // этот локатор мне не нравится
        }

        //найти видео - может и не надо
        $("#лупа").click();
        $("#поле поиска").sendKeys("Смешные коты");
        $("#энтер").click();

        //запустить первое видео в ленте
        $$("#видео").first().click();
        // чего то ждем


    }
}
