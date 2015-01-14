package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import org.junit.Before;
import org.junit.Test;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestServiceTest {

	private Bank testBank1;
	private Bank testBank2;

	@Before
	public void initBanks() {
		testBank1 = newBank();
		testBank2 = newBank();
		testBank1.setId(1);
		testBank2.setId(2);

		Client testClient1 = newClient();
		Client testClient2 = newClient();
		testClient1.setId(1);
		testClient2.setId(2);

		CheckingAccount testCheckingAccount1 = newCheckingAccount();
		CheckingAccount testCheckingAccount2 = newCheckingAccount();
		testCheckingAccount1.setId(1);
		testCheckingAccount2.setId(2);

		testClient1.addAccount(testCheckingAccount1);
		testClient2.addAccount(testCheckingAccount2);

		testBank1.addClient(testClient1);
		testBank2.addClient(testClient2);
	}


	// test isEquals(Object o1, Object o2)
	@Test
	public void testIsEqualsInIsEquals() {
		assertTrue(TestService.isEquals(testBank1, testBank2));
	}

	@Test
	public void testIsObjectEqualToEachOtherInIsEquals() {
		assertTrue(TestService.isEquals(testBank1, testBank1));
	}

	@Test
	public void testIsObjectNotEqualToNullInIsEquals() {
		assertFalse(TestService.isEquals(testBank1, null));
	}

	@Test
	public void testIsObjectNotEqualToOtherClassObjectInEquals() {
		assertFalse(TestService.isEquals(testBank1, newClient()));
	}

}