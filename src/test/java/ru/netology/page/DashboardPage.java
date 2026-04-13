package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        return cards.stream()
                .filter(el -> el.getText().contains(cardInfo.getLastFourDigits()))
                .map(el -> {
                    String text = el.getText();
                    int start = text.indexOf(balanceStart) + balanceStart.length();
                    int finish = text.indexOf(balanceFinish);
                    return Integer.parseInt(text.substring(start, finish));
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Карта не найдена: " + cardInfo.getLastFourDigits()));
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo) {
        cards.stream()
                .filter(el -> el.getText().contains(cardInfo.getLastFourDigits()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Карта не найдена"))
                .$("button")
                .click();
        return new TransferPage();
    }

    public DashboardPage() {
        heading.shouldBe(visible);
    }
}