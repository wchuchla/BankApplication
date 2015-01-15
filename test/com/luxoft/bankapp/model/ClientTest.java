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

import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.*;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientTest {

    private Client sut;
    private Account testAccount;

    @Before
    public void createClientAndAccount() {
        sut = newClient();
        testAccount = newSavingAccount();
        sut.addAccount(testAccount);
    }


    // test Client(String name, Gender gender, String email, String phoneNumber, String city, float initialOverdraft)
    @Test
    public void testSetVariablesInConstructor() {
        sut = new Client("sut", Gender.MALE, "sut@gmail.com", "123456789", "City", 1.0f);

        assertEquals("sut", sut.getName());
        assertEquals(Gender.MALE, sut.getGender());
        assertEquals("sut@gmail.com", sut.getEmail());
        assertEquals("123456789", sut.getPhoneNumber());
        assertEquals("City", sut.getCity());
        assertEquals(1.0f, sut.getInitialOverdraft(), 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialOverdraftInConstructor() {
        sut = new Client("sut", Gender.MALE, "sut@gmail.com",
                "123456789", "City", -1.0f);
    }


    // test Client(String name)
    @Test
    public void testSetNameInConstructor() {
        sut = new Client("sut");

        assertEquals("sut", sut.getName());
    }


    // test Client(ClientInfo info)
    @Test
    public void testConstructorFromInfo() {
        ClientInfo clientInfo = new ClientInfo(sut);
        Client sut2 = new Client(clientInfo);

        assertEquals(CLIENT_NAME, sut2.getName());
        assertEquals(CLIENT_GENDER, sut2.getGender());
        assertEquals(CLIENT_EMAIL, sut2.getEmail());
        assertEquals(CLIENT_PHONE_NUMBER, sut2.getPhoneNumber());
        assertEquals(CLIENT_CITY, sut2.getCity());
        assertEquals(CLIENT_INITIAL_OVERDRAFT, sut2.getInitialOverdraft(), 0.0f);
    }


    // test parseFeed(Map<String, String> feed)
    @Test
    public void parseFeed() {
        Map<String, String> feed = new HashMap<>();

        feed.put("gender", "M");
        feed.put("email", "feedClient@gmail.com");
        feed.put("phonenumber", "123456789");
        feed.put("city", "City");
        feed.put("initialoverdraft", "100");
        feed.put("accounttype", "saving");
        feed.put("balance", "1");

        sut.parseFeed(feed);

        assertEquals(Gender.MALE, sut.getGender());
        assertEquals("feedClient@gmail.com", sut.getEmail());
        assertEquals("123456789", sut.getPhoneNumber());
        assertEquals("City", sut.getCity());
        assertEquals(100.0f, sut.getInitialOverdraft(), 0.0f);
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
        final String PREFIX = sut.getGender().getPrefix();

        final String EXPECTED_STRING = PREFIX + " " + CLIENT_NAME;

        assertEquals(EXPECTED_STRING, sut.getClientSalutation());
    }


    // test printReport()
    @Test
    @Ignore
    public void testPrintReport() {
        final int ID = sut.getId();
        final int ACCOUNT_ID = testAccount.getId();

        final String EXPECTED_STRING = "ID: " + ID + ", Name: Mr " + CLIENT_NAME + ", Gender: " + CLIENT_GENDER
                + ", Email: " + CLIENT_EMAIL + ", Phone number: " + CLIENT_PHONE_NUMBER + ", City: "
                + CLIENT_CITY + "\nList of accounts:\n" + "Account type = " + "Saving account,"
                + " ID = " + ACCOUNT_ID + ", account number = " + SAVING_ACCOUNT_ACCOUNT_NUMBER + ", balance = "
                + SAVING_ACCOUNT_INITIAL_BALANCE + "\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printReport();
        final String printReportOutput = byteArrayOutputStream.toString();

        assertEquals("printReport() method does not produce the expected output",
                EXPECTED_STRING, printReportOutput);
    }


    // test equalsContract
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Client.class)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }

}