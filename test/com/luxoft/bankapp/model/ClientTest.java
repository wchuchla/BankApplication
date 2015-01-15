package com.luxoft.bankapp.model;

import com.luxoft.bankapp.model.enums.Gender;
import com.luxoft.bankapp.model.info.ClientInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.*;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    private Client sut;
    private Account testAccount;

    @Before
    public void setUp() {
        sut = newClient();
        testAccount = newSavingAccount();
        sut.addAccount(testAccount);
    }


    // test Client(String name, Gender gender, String email, String phoneNumber, String city, float initialOverdraft)
    @Test
    public void testSetVariablesInConstructor() {
        sut = new Client(CLIENT_NAME, CLIENT_GENDER, CLIENT_EMAIL, CLIENT_PHONE_NUMBER, CLIENT_CITY, CLIENT_INITIAL_OVERDRAFT);

        assertClient(sut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialOverdraftInConstructor() {
        sut = new Client(CLIENT_NAME, CLIENT_GENDER, CLIENT_EMAIL, CLIENT_PHONE_NUMBER, CLIENT_CITY, -1.0f);
    }


    // test Client(String name)
    @Test
    public void testSetNameInConstructor() {
        sut = new Client(CLIENT_NAME);

        assertEquals(CLIENT_NAME, sut.getName());
    }


    // test Client(ClientInfo info)
    @Test
    public void testConstructorFromInfo() {
        ClientInfo clientInfo = new ClientInfo(sut);
        Client sut2 = new Client(clientInfo);

        assertClient(sut2);
    }


    // test parseFeed(Map<String, String> feed)
    @Test
    public void parseFeed() {
        Map<String, String> feed = new HashMap<>();

        feed.put("gender", "M");
        feed.put("email", CLIENT_EMAIL);
        feed.put("phonenumber", CLIENT_PHONE_NUMBER);
        feed.put("city", CLIENT_CITY);
        feed.put("initialoverdraft", "1000");
        feed.put("accounttype", "saving");
        feed.put("balance", "1");

        sut.parseFeed(feed);

        assertClient(sut);
    }


    // test getAccountByType(String accountType)
    @Test
    public void getAccountByTypeIfAccountExists() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getAccountByType = sut.getClass().getDeclaredMethod("getAccountByType", String.class);
        getAccountByType.setAccessible(true);

        Account account = (Account) getAccountByType.invoke(sut, "Saving");

        assertTrue(testAccount == account);
        assertTrue(sut.getAccounts().size() == 1);
    }

    @Test
    public void getAccountByTypeIfAccountNotExists() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getAccountByType = sut.getClass().getDeclaredMethod("getAccountByType",
                String.class);
        getAccountByType.setAccessible(true);

        Account account = (Account) getAccountByType.invoke(sut, "Checking");

        assertTrue(testAccount != account);
        assertTrue(sut.getAccounts().contains(account));
        assertTrue(sut.getAccounts().contains(testAccount));
        assertEquals("Checking", account.getAccountType());
    }


    // test createAccountByType(String accountType)
    @Test
    public void testSetAccountTypeInCreateAccountByType() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method createAccountByType = sut.getClass().getDeclaredMethod("createAccountByType",
                String.class);
        createAccountByType.setAccessible(true);

        Account account = (Account) createAccountByType.invoke(sut, "Checking");

        assertEquals("Checking", account.getAccountType());
    }

    @Test(expected = Exception.class)
    public void testInvalidAccountTypeInCreateAccountByTypeThrowsException() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method createAccountByType = sut.getClass().getDeclaredMethod("createAccountByType",
                String.class);
        createAccountByType.setAccessible(true);

        createAccountByType.invoke(sut, "Invalid type");
    }


    // setInitialOverdraft(float initialOverdraft)
    @Test
    public void testSetInitialOverdraftInSetInitialOverdraft() {
        sut.setInitialOverdraft(1.0f);

        assertEquals(1.0f, sut.getInitialOverdraft(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialOverdraftInSetInitialOverdraftThrowsException() {
        sut.setInitialOverdraft(-1.0f);
    }


    // test getClientSalutation()
    @Test
    public void testGetClientSalutation() {
        final String prefix = sut.getGender().getPrefix();

        final String expectedString = prefix + " " + CLIENT_NAME;

        assertEquals(expectedString, sut.getClientSalutation());
    }


    // test printReport()
    @Test
    @Ignore
    public void testPrintReport() {
        final int id = sut.getId();
        final int accountId = testAccount.getId();

        final String expectedString = "ID: " + id + ", Name: Mr " + CLIENT_NAME + ", Gender: " + CLIENT_GENDER
                + ", Email: " + CLIENT_EMAIL + ", Phone number: " + CLIENT_PHONE_NUMBER + ", City: "
                + CLIENT_CITY + "\nList of accounts:\n" + "Account type = " + "Saving account,"
                + " ID = " + accountId + ", account number = " + SAVING_ACCOUNT_ACCOUNT_NUMBER + ", balance = "
                + SAVING_ACCOUNT_INITIAL_BALANCE + "\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printReport();
        final String printReportOutput = byteArrayOutputStream.toString();

        assertEquals("printReport() method does not produce the expected output",
                expectedString, printReportOutput);
    }


    // test equalsContract
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Client.class)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

}