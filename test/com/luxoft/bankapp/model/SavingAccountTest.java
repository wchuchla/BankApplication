package com.luxoft.bankapp.model;

import com.luxoft.bankapp.exception.NotEnoughFundsException;
import com.luxoft.bankapp.model.info.SavingAccountInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;

public class SavingAccountTest {

    private SavingAccount sut;

    @Before
    public void createSavingAccountWithBalance0AndOverdraft100() {
        sut = newSavingAccount();
    }


    // test SavingAccount()
    @Test
    public void testSetDefaultBalanceAndDefaultOverdraftInDefaultConstructor() {
        sut = new SavingAccount();

        assertEquals(0.0f, sut.getBalance(), 0);
    }


    // test SavingAccount(float balance)
    @Test
    public void testSetBalanceAndDefaultOverdraftInConstructor() {
        sut = new SavingAccount(1.0f);

        assertEquals(1.0f, sut.getBalance(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeBalanceInConstructorThrowsException() {
        sut = new SavingAccount(-1.0f);
    }

    // test SavingAccount(SavingAccount SavingAccount)
    @Test
    public void testCopyConstructor() {
        SavingAccount sut2 = new SavingAccount(sut);

        assertEquals(sut.getId(), sut2.getId());
        assertEquals(sut.getBalance(), sut2.getBalance(), 0);
        assertEquals(sut.getAccountNumber(), sut2.getAccountNumber());
    }


    // test SavingAccount(SavingAccountInfo info)
    @Test
    public void testConstructorFromInfo() {
        SavingAccountInfo savingAccountInfo = new SavingAccountInfo(sut);
        Account sut2 = new SavingAccount(savingAccountInfo);

        assertEquals(sut.getId(), sut2.getId());
        assertEquals(sut.getBalance(), sut2.getBalance(), 0);
        assertEquals(sut.getAccountNumber(), sut2.getAccountNumber());
    }


    // test withdraw(float balance)
    @Test
    public void testWithdrawDecreaseBalance() throws NotEnoughFundsException {
        float amount = 1;

        sut.withdraw(amount);

        assertEquals(SAVING_ACCOUNT_INITIAL_BALANCE - amount, sut.getBalance(), 0.00001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawNegativeAmountThrowsException() throws NotEnoughFundsException {
        sut.withdraw(-1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawZeroAmountThrowsException() throws NotEnoughFundsException {
        sut.withdraw(0.0f);
    }

    @Test(expected = NotEnoughFundsException.class)
    public void testWithdrawAmountMoreThanBalance() throws NotEnoughFundsException {
        float amount = SAVING_ACCOUNT_INITIAL_BALANCE + 1;

        sut.withdraw(amount);
    }


    // test parseFeed()
    @Test
    public void testParseFeed() {
        Map<String, String> feed = new HashMap<>();
        feed.put("balance", "1");

        sut.parseFeed(feed);

        assertEquals(1, sut.getBalance(), 0.0f);
    }


    // test printReport()
    @Test
    public void testPrintReport() {
        final int ID = sut.getId();

        final String EXPECTED_STRING = "Account type = Saving account, ID = " + ID
                + ", account number = " + SAVING_ACCOUNT_ACCOUNT_NUMBER + ", balance = " + SAVING_ACCOUNT_INITIAL_BALANCE;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printReport();
        final String printReportOutput = byteArrayOutputStream.toString();

        assertEquals("printReport() method does not produce the expected output",
                EXPECTED_STRING, printReportOutput);
    }


    // test toString()
    @Test
    public void testToString() {
        final int ID = sut.getId();

        final String EXPECTED_STRING = "SavingAccount [id=" + ID + ", accountNumber=" + SAVING_ACCOUNT_ACCOUNT_NUMBER
                + ", balance=" + SAVING_ACCOUNT_INITIAL_BALANCE + "]";

        assertEquals("toString() method does not produce the expected output",
                EXPECTED_STRING, sut.toString());
    }


    // test equalsContract
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(SavingAccount.class)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }

}