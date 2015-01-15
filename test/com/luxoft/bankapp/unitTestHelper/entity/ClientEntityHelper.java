package com.luxoft.bankapp.unitTestHelper.entity;

import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.enums.Gender;

import static org.junit.Assert.*;

public class ClientEntityHelper {

    public static final int CLIENT_ID = 1;
    public static final int CLIENT_SECOND_CLIENT_ID = 2;
    public static final String CLIENT_NAME = "First Client";
    public static final String CLIENT_SECOND_CLIENT_NAME = "Second Client";
    public static final String CLIENT_INVALID_NAME = "invalidName";
    public static final Gender CLIENT_GENDER = Gender.MALE;
    public static final Gender CLIENT_SECOND_CLIENT_GENDER = Gender.FEMALE;
    public static final String CLIENT_EMAIL = "firstclient@gmail.com";
    public static final String CLIENT_SECOND_CLIENT_EMAIL = "secondclient@gmail.com";
    public static final String CLIENT_PHONE_NUMBER = "+001-123-456-789";
    public static final String CLIENT_SECOND_CLIENT_PHONE_NUMBER = "+001-987-654-321";
    public static final String CLIENT_CITY = "First City";
    public static final String CLIENT_SECOND_CLIENT_CITY = "Second City";
    public static final float CLIENT_INITIAL_OVERDRAFT = 1000;
    public static final float CLIENT_SECOND_CLIENT_INITIAL_OVERDRAFT = 1000;


    private ClientEntityHelper() {
        throw new UnsupportedOperationException("This class is a helper");
    }

    public static Client newClient() {
        return new Client(CLIENT_NAME, CLIENT_GENDER, CLIENT_EMAIL, CLIENT_PHONE_NUMBER, CLIENT_CITY,
                CLIENT_INITIAL_OVERDRAFT);
    }

    public static Client newSecondClient() {
        return new Client(CLIENT_SECOND_CLIENT_NAME, CLIENT_SECOND_CLIENT_GENDER, CLIENT_SECOND_CLIENT_EMAIL,
                CLIENT_SECOND_CLIENT_PHONE_NUMBER, CLIENT_SECOND_CLIENT_CITY, CLIENT_SECOND_CLIENT_INITIAL_OVERDRAFT);
    }

    public static void assertClient(Client client) {
        assertNotNull(client);
        assertEquals(CLIENT_NAME, client.getName());
        assertEquals(CLIENT_GENDER, client.getGender());
        assertEquals(CLIENT_EMAIL, client.getEmail());
        assertEquals(CLIENT_PHONE_NUMBER, client.getPhoneNumber());
        assertEquals(CLIENT_CITY, client.getCity());
        assertEquals(CLIENT_INITIAL_OVERDRAFT, client.getInitialOverdraft(), 0.0f);
    }

    public static void assertSecondClient(Client client) {
        assertNotNull(client);
        assertTrue(client.getId() > 0);
        assertEquals(CLIENT_SECOND_CLIENT_NAME, client.getName());
        assertEquals(CLIENT_SECOND_CLIENT_GENDER, client.getGender());
        assertEquals(CLIENT_SECOND_CLIENT_EMAIL, client.getEmail());
        assertEquals(CLIENT_SECOND_CLIENT_PHONE_NUMBER, client.getPhoneNumber());
        assertEquals(CLIENT_SECOND_CLIENT_CITY, client.getCity());
        assertEquals(CLIENT_SECOND_CLIENT_INITIAL_OVERDRAFT, client.getInitialOverdraft(), 0.0f);
    }
}