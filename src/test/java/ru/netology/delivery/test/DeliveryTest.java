package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder=\"Город\"]").setValue(validUser.getCity());
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[placeholder=\"Дата встречи\"]").setValue(firstMeetingDate);
        $("[name='name']").setValue(validUser.getName());
        $("[name='phone']").setValue(validUser.getPhone());
        $x("//*[contains(text(), 'Я соглашаюсь')]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[class=\"notification__content\"]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(10))
                .should(Condition.appear);
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[placeholder=\"Дата встречи\"]").setValue(secondMeetingDate);
        $x("//*[contains(text(), 'Запланировать')]").click();
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $("[class=\"notification__content\"]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(10))
                .should(Condition.appear);

    }
}
