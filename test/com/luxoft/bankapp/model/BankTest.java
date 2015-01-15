package com.luxoft.bankapp.model;

import com.luxoft.bankapp.service.ClientRegistrationListener;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.BANK_NAME;
import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.*;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.*;
import static org.junit.Assert.*;

public class BankTest {

    private Bank sut;
    private Client testClient;
    private SavingAccount testSavingAccount;

    @Before
    public void CreateBankAndClient() {
        sut = newBank();
        testClient = newClient();
        testSavingAccount = newSavingAccount();
        testClient.addAccount(testSavingAccount);
        sut.addClient(testClient);
    }


    // test Bank(String name)
    @Test
    public void testSetNameInConstructor() {
        Bank sut = new Bank(BANK_NAME);

        assertEquals(BANK_NAME, sut.getName());
    }


    // test parseFeed(Map<String, String> feed)
    @Test
    public void testParseFeed() {
        Map<String, String> feed = new HashMap<>();

        feed.put("name", "feedClient");
        feed.put("gender", "M");
        feed.put("email", "feedClient@gmail.com");
        feed.put("phonenumber", "123456789");
        feed.put("city", "City");
        feed.put("initialoverdraft", "100");
        feed.put("accounttype", "saving");
        feed.put("balance", "1");

        sut.parseFeed(feed);

        assertTrue(sut.getClients().containsKey("feedClient"));
    }


    // test addClient(Client client)
    @Test
    public void testAddClient() {
        Client testClient2 = newSecondClient();

        sut.addClient(testClient2);

        assertTrue(sut.getClients().containsKey(testClient2.getName()));
    }


    // test removeClient(Client client)
    @Test
    public void testRemoveClient() {
        assertTrue(sut.getClients().containsKey(testClient.getName()));

        sut.removeClient(testClient);

        assertFalse(sut.getClients().containsKey(testClient.getName()));
    }


    // test addClientRegistrationListener(ClientRegistrationListener listener)
    @Test
    public void testAddClientRegistrationListener() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method addClientRegistrationListener = sut.getClass().getDeclaredMethod("addClientRegistrationListener",
                ClientRegistrationListener.class);
        addClientRegistrationListener.setAccessible(true);

        ClientRegistrationListener testListener = c -> {
        };

        addClientRegistrationListener.invoke(sut, testListener);

        Field listeners = sut.getClass().getDeclaredField("listeners");
        listeners.setAccessible(true);
        List listenerList = (List<ClientRegistrationListener>) listeners.get(sut);

        assertTrue(listenerList.contains(testListener));
    }


    // test printReport()
    @Test
    @Ignore
    public void testPrintReport() {
        final int CLIENT_ID = testClient.getId();

        final int ACCOUNT_ID = testSavingAccount.getId();

        final String EXPECTED_STRING = "Bank name: " + BANK_NAME + "\n\nList of clients: \n\n" + "ID: "
                + CLIENT_ID + ", " + "Name: Mr " + CLIENT_NAME + ", Gender: " + CLIENT_GENDER + ", Email: "
                + CLIENT_EMAIL + ", Phone number: " + CLIENT_PHONE_NUMBER + ", City: "
                + CLIENT_CITY + "\nList of accounts:\n" + "Account type = " + "Saving account,"
                + " ID = " + ACCOUNT_ID + ", account number = " + SAVING_ACCOUNT_ACCOUNT_NUMBER + ", balance = " +
                SAVING_ACCOUNT_INITIAL_BALANCE +
                "\r\n\r\n";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printReport();
        final String printReportOutput = byteArrayOutputStream.toString();

        assertEquals("printReport() method does not produce the expected output",
                EXPECTED_STRING, printReportOutput);
    }

}