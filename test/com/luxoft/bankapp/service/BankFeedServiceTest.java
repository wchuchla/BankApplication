package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.info.BankInfoCalc;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.newBank;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankFeedServiceTest {

	private BankFeedService sut;
	private Bank testBank;

	@Before
	public void setUp() {
		testBank = newBank();
		sut = new BankFeedService(testBank);
	}

	// test loadFeed()
	@Test
	public void testLoadFeed() {
		int numberOfClients = 3;
		int numberOfAccounts = 3;
		float depositSum = 300.0f;
		float creditSum = 0.0f;

		sut.loadFeed();

		Set<String> clients = BankInfoCalc.getClientsSet(testBank);

		assertEquals(numberOfClients, BankInfoCalc.calcNumberOfClients(testBank));
		assertEquals(numberOfAccounts, BankInfoCalc.calcNumberOfAccounts(testBank));
		assertEquals(depositSum, BankInfoCalc.calcDepositSum(testBank), 0.0f);
		assertEquals(creditSum, BankInfoCalc.calcCreditSum(testBank), 0.0f);
		assertTrue(clients.contains("Maria Nash"));
		assertTrue(clients.contains("John Washington"));
		assertTrue(clients.contains("John Smith"));
	}

}