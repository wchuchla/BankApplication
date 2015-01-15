package com.luxoft.bankapp.model.info;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.*;
import static org.junit.Assert.assertEquals;

public class ClientInfoTest {

    private ClientInfo sut;
    private Client testClient;

    @Before
    public void createTestClient() {
        testClient = newClient();
    }

    // test ClientInfo(Client client)
    @Test
    public void testConstructorWithClientParameter() {
        sut = new ClientInfo(testClient);

        assertEquals(CLIENT_NAME, sut.getName());
        assertEquals(CLIENT_GENDER, sut.getGender());
        assertEquals(CLIENT_EMAIL, sut.getEmail());
        assertEquals(CLIENT_PHONE_NUMBER, sut.getPhoneNumber());
        assertEquals(CLIENT_CITY, sut.getCity());
        assertEquals(CLIENT_INITIAL_OVERDRAFT, sut.getInitialOverdraft(), 0.0f);
    }


    // test ClientInfo(String name, Gender gender, String email, String phoneNumber, String city, float initialOverdraft)
    @Test
    public void testConstructorWithAccountInfoParameters() {
        sut = new ClientInfo(CLIENT_NAME, CLIENT_GENDER, CLIENT_EMAIL, CLIENT_PHONE_NUMBER, CLIENT_CITY,
                CLIENT_INITIAL_OVERDRAFT);

        assertEquals(CLIENT_NAME, sut.getName());
        assertEquals(CLIENT_GENDER, sut.getGender());
        assertEquals(CLIENT_EMAIL, sut.getEmail());
        assertEquals(CLIENT_PHONE_NUMBER, sut.getPhoneNumber());
        assertEquals(CLIENT_CITY, sut.getCity());
        assertEquals(CLIENT_INITIAL_OVERDRAFT, sut.getInitialOverdraft(), 0.0f);
    }


    // test toString()
    @Test
    public void testToString() {
        sut = new ClientInfo(testClient);

        final Set<Account> accounts = sut.getAccounts();

        final String expectedString = "ClientInfo [name=" + CLIENT_NAME + ", gender=" + CLIENT_GENDER + ", email="
                + CLIENT_EMAIL + ", phoneNumber=" + CLIENT_PHONE_NUMBER + ", city=" + CLIENT_CITY + ", accounts="
                + accounts + ", " + "initialOverdraft=" + CLIENT_INITIAL_OVERDRAFT + "]";

        assertEquals("toString() method does not produce the expected output",
                expectedString, sut.toString());
    }

}