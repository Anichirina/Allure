package test.java;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;

public class CallBackTest {
    String name = DataGenerator.makeName();
    String phone = DataGenerator.makePhone();
    String city = DataGenerator.makeCity();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());


    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    //перенос даты и новая функция
    @Test
    void shouldSubitRequestNew() {
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a")
                + Keys.BACK_SPACE);
        $("[data-test-id=date] input").sendKeys(DataGenerator.forwardDate(3));
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible, ofSeconds(15));
        $$("[data-test-id='success-notification']").findBy(text("Встреча успешно запланирована на")).shouldBe(visible);
        $("[placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.CONTROL, "a")
                + Keys.BACK_SPACE);
        $("[placeholder='Дата встречи']").setValue(DataGenerator.forwardDate(7));
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldBe(visible);
        $("[data-test-id='replan-notification']>.notification__content").shouldHave(text("У вас уже" +
                " запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=date] input").setValue(DataGenerator.forwardDate(7));
        $("[data-test-id=replan-notification] .notification__title").shouldHave(exactText("Необходимо" +
                " подтверждение"));
        $("[data-test-id='replan-notification'] .button__text").click();

        $("[data-test-id='success-notification']>.notification__content")
                .shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(exactText("Встреча успешно запланирована на "
                        + DataGenerator.forwardDate(7)));
    }


    //правильное заполнение
    @Test
    void shouldSubitRequest() {
        $("[data-test-id=city] input").setValue(city);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a")
                + Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.forwardDate(3));
        $("[data-test-id=name] input").setValue(name);
        $("[data-test-id=phone] input").setValue(phone);
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(withText("Успешно!"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $$(".notification").findBy(text("Встреча успешно запланирована на")).shouldBe(visible);
    }

   }



