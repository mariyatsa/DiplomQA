package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.PaymentFormBuyPage;
import page.MainPage;
import page.PaymentFormBuyByCreditPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreditCardPayTest {
    private MainPage mainPage;
    private PaymentFormBuyByCreditPage paymentFormBuyByCreditPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    public void setup() {
        mainPage = open("http://localhost:8080/", MainPage.class);
    }

    @AfterEach
    void clean() {
        SQLHelper.clear();
    }

    @Test
    public void shouldPurchaseWithApprovedCard() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitSuccessfulNotification();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDenyWithDiclinedCard() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getSecondCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitErrorNotification();
        var expected = DataHelper.getSecondCardStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shoulDenyWithEmptyCardField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getEmptyCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitEmptyField();
    }

    @Test
    public void shouldDenyWithEmptyMonthField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getEmptyMonth();
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitEmptyField();
    }

    @Test
    public void shouldDenyWithEmptyYearField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getEmptyYear();
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitEmptyField();
    }

    @Test
    public void shouldDenyWithEmptyOwnerField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getEmptyOwner();
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitEmptyField();
    }

    @Test
    public void shouldDenyEmptyCVCField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEmptyCVC();
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitEmptyField();
    }

    @Test
    public void shouldDenyWithInvalidDateMonth() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateInvalidMonthDate(2);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongCardDate();
    }

    @Test
    public void shouldBeEmptyCardFieldWithCyrillicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("RU");
        paymentFormBuyByCreditPage.onlyCardField(cardNumber);
        paymentFormBuyByCreditPage.emptyCardField();
    }

    @Test
    public void shouldBeEmptyCardFieldWithLatinChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("EN");
        paymentFormBuyByCreditPage.onlyCardField(cardNumber);
        paymentFormBuyByCreditPage.emptyCardField();
    }

    @Test
    public void shouldBeEmptyCardFieldWithHieroglyphsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("ja");
        paymentFormBuyByCreditPage.onlyCardField(cardNumber);
        paymentFormBuyByCreditPage.emptyCardField();
    }

    @Test
    public void shouldBeEmptyCardFieldWithArabicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getGenerateInvalidCardInfo("ar");
        paymentFormBuyByCreditPage.onlyCardField(cardNumber);
        paymentFormBuyByCreditPage.emptyCardField();
    }

    @Test
    public void shouldBeEmptyCardFieldWithSpecialsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getSpecialSimbolsCardInfo();
        paymentFormBuyByCreditPage.onlyCardField(cardNumber);
        paymentFormBuyByCreditPage.emptyCardField();
    }

    @Test
    public void shouldBeEmptyMonthFieldWithCyrillicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var month = DataHelper.getGenerateInvalidMonthInfo("RU");
        paymentFormBuyByCreditPage.onlyMonthField(month);
        paymentFormBuyByCreditPage.emptyMonthField();
    }

    @Test
    public void shouldBeEmptyMonthFieldWithLatinChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var month = DataHelper.getGenerateInvalidMonthInfo("EN");
        paymentFormBuyByCreditPage.onlyMonthField(month);
        paymentFormBuyByCreditPage.emptyMonthField();
    }

    @Test
    public void shouldBeEmptyMonthFieldWithHieroglyphsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var month = DataHelper.getGenerateInvalidMonthInfo("ja");
        paymentFormBuyByCreditPage.onlyMonthField(month);
        paymentFormBuyByCreditPage.emptyMonthField();
    }

    @Test
    public void shouldBeEmptyMonthFieldWithArabicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var month = DataHelper.getGenerateInvalidMonthInfo("ar");
        paymentFormBuyByCreditPage.onlyMonthField(month);
        paymentFormBuyByCreditPage.emptyMonthField();
    }

    @Test
    public void shouldBeEmptyMonthFieldWithSpecialsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var month = DataHelper.getSpecialsSymbolMonth();
        paymentFormBuyByCreditPage.onlyMonthField(month);
        paymentFormBuyByCreditPage.emptyMonthField();
    }

    @Test
    public void shouldDineWithSingleNumberInMonthField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getInvalidAmountNumbersMonths(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldEmptyYearFieldWithCyrillicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var year = DataHelper.getGenerateInvalidYearInfo("RU");
        paymentFormBuyByCreditPage.onlyYearField(year);
        paymentFormBuyByCreditPage.emptyYearField();
    }

    @Test
    public void shouldEmptyYearFieldWithLatinChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var year = DataHelper.getGenerateInvalidYearInfo("EN");
        paymentFormBuyByCreditPage.onlyYearField(year);
        paymentFormBuyByCreditPage.emptyYearField();
    }

    @Test
    public void shouldEmptyYearFieldWithHieroglyphsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var year = DataHelper.getGenerateInvalidYearInfo("ja");
        paymentFormBuyByCreditPage.onlyYearField(year);
        paymentFormBuyByCreditPage.emptyYearField();
    }

    @Test
    public void shouldEmptyYearFieldWithArabicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var year = DataHelper.getGenerateInvalidYearInfo("ar");
        paymentFormBuyByCreditPage.onlyYearField(year);
        paymentFormBuyByCreditPage.emptyYearField();
    }

    @Test
    public void shouldEmptyYearFieldWithSpecialsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var year = DataHelper.getSpecialsSymbolsYearInfo();
        paymentFormBuyByCreditPage.onlyYearField(year);
        paymentFormBuyByCreditPage.emptyYearField();
    }

    @Test
    public void shouldDenyWithSingleNumberInYearField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getInvalidAmountNumbersYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldDenyWithSuchNumberInYearField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(40);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongCardDate();
    }

    @Test
    public void shouldDenyWithInPreviousYearField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.getGenerateInvalidYearDate(2);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitCardExpired();
    }

    @Test
    public void shouldDenyOwnerFieldWithCyrillicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("RU");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldDenyOwnerFieldWithHieroglyphsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("ja");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldDenyOwnerFieldWithArabicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("ar");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldDenyOwnerFieldWithSpecialsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getSpecialsSymbolsOwner();
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldDenyOwnerFieldWithNumbersChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.getGenerateNumberOwner(8);
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldEmptyCVCFieldWithCyrillicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cvc = DataHelper.getGenerateInvalidCvcCode("RU");
        paymentFormBuyByCreditPage.onlyCVCField(cvc);
        paymentFormBuyByCreditPage.emptyCVCField();
    }

    @Test
    public void shouldEmptyCVCFieldWithLatinChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cvc = DataHelper.getGenerateInvalidCvcCode("EN");
        paymentFormBuyByCreditPage.onlyCVCField(cvc);
        paymentFormBuyByCreditPage.emptyCVCField();
    }

    @Test
    public void shouldEmptyCVCFieldWithHieroglyphsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cvc = DataHelper.getGenerateInvalidCvcCode("ja");
        paymentFormBuyByCreditPage.onlyCVCField(cvc);
        paymentFormBuyByCreditPage.emptyCVCField();
    }

    @Test
    public void shouldEmptyCVCFieldWithArabicChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cvc = DataHelper.getGenerateInvalidCvcCode("ar");
        paymentFormBuyByCreditPage.onlyCVCField(cvc);
        paymentFormBuyByCreditPage.emptyCVCField();
    }

    @Test
    public void shouldEmptyCVCFieldWithSpecialsChars() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cvc = DataHelper.getSpecialSymbolsCvcCode();
        paymentFormBuyByCreditPage.onlyCVCField(cvc);
        paymentFormBuyByCreditPage.emptyCVCField();
    }

    @Test
    public void shouldRemovedMinusInMonthField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getEnterMonth("-2");
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldRemovedZeroInMonthField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getEnterMonth("00");
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongCardDate();
    }

    @Test
    public void shouldRemovedUpInMonthField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getEnterMonth("13");
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongCardDate();
    }

    @Test
    public void shouldRemovedMinusInCvcField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEnterCVC("-1");
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldRemovedZeroInCvcField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(0);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEnterCVC("000");
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitWrongFormat();
    }

    @Test
    public void shouldRemovedUpInCVCField() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.getEnterCVC("999");
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        paymentFormBuyByCreditPage.waitSuccessfulNotification();
        var expected = DataHelper.getFirstCardStatus();
        var actual = SQLHelper.getCreditPaymentStatus();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddCreditInOrderEntry() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getFirstCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        var expected = SQLHelper.getCreditRequestReEntryId();
        var actual = SQLHelper.getCreditOrderEntryId();
        assertEquals(expected, actual);
    }


    @Test
    public void shouldDonTAddCreditInOrderEntryStatusDeclined() {
        paymentFormBuyByCreditPage = mainPage.payWithCreditCard();
        var cardNumber = DataHelper.getSecondCardInfo();
        var month = DataHelper.getGenerateMonth(1);
        var year = DataHelper.generateYear(1);
        var owner = DataHelper.generateOwner("EN");
        var cvc = DataHelper.generateCVCCode(3);
        paymentFormBuyByCreditPage.filledForm(cardNumber, month, year, owner, cvc);
        var expected = SQLHelper.getCreditRequestReEntryId();
        var actual = SQLHelper.getCreditOrderEntryId();
        assertNotEquals(expected, actual);
    }
}
