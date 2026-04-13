package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement amountField = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transferBtn = $("[data-test-id=action-transfer]");

    public DashboardPage makeTransfer(String amount, DataHelper.CardInfo fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getNumber());
        transferBtn.click();
        return new DashboardPage();
    }
}