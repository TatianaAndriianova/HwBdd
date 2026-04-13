package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        Configuration.baseUrl = "http://localhost:9999/";
        var loginPage = open("", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromSecondCardToFirst() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int firstInitial = dashboardPage.getCardBalance(firstCard);
        int secondInitial = dashboardPage.getCardBalance(secondCard);
        int amount = 1000;

        dashboardPage.selectCardToTransfer(firstCard)
                .makeTransfer(String.valueOf(amount), secondCard);

        assertEquals(firstInitial + amount, dashboardPage.getCardBalance(firstCard),
                "Баланс карты 0001 должен увеличиться на " + amount);
        assertEquals(secondInitial - amount, dashboardPage.getCardBalance(secondCard),
                "Баланс карты 0002 должен уменьшиться на " + amount);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecond() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int firstInitial = dashboardPage.getCardBalance(firstCard);
        int secondInitial = dashboardPage.getCardBalance(secondCard);
        int amount = 500;

        dashboardPage.selectCardToTransfer(secondCard)
                .makeTransfer(String.valueOf(amount), firstCard);

        assertEquals(firstInitial - amount, dashboardPage.getCardBalance(firstCard),
                "Баланс карты 0001 должен уменьшиться на " + amount);
        assertEquals(secondInitial + amount, dashboardPage.getCardBalance(secondCard),
                "Баланс карты 0002 должен увеличиться на " + amount);
    }

    @Test
    void shouldNotTransferAmountExceedingBalance() {
        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int secondInitial = dashboardPage.getCardBalance(secondCard);
        int amount = secondInitial + 5000;

        dashboardPage.selectCardToTransfer(firstCard)
                .makeTransfer(String.valueOf(amount), secondCard);

        assertEquals(secondInitial, dashboardPage.getCardBalance(secondCard),
                "БАГ: перевод на сумму сверх баланса не должен выполняться!");
    }
}