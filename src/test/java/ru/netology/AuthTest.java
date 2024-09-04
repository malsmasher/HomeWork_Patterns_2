package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.*;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setuo() {
        open("http://localhost:9999");
    }

    // проверка входа в ЛК, если пользователь зарегистрирован
    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("h2").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

    }

    // проверка входа в ЛК, если пользователь незарегистрирован
    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    //проверка входа в ЛК, если пользователь заблокирован
    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Пользователь заблокирован"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    // Проверка входа в ЛК с неверным логином
    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    // Проверка входа в ЛК с неверным паролем
    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();

        $("[data-test-id='error-notification'] .notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}
