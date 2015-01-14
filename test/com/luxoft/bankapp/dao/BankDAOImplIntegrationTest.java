package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.TestService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.BANK_NAME;
import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.newBank;
import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newSecondClient;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.newSavingAccount;
import static org.junit.Assert.assertTrue;

public class BankDAOImplIntegrationTest {

	private Bank bank;
	private BankDAOImpl sut;

	@Before
	public void setUp() throws DAOException {
		bank = newBank();
		Client testClient = newClient();
		CheckingAccount testCheckingAccount = newCheckingAccount();
		testClient.addAccount(testCheckingAccount);
		testClient.setActiveAccount(testCheckingAccount);

		bank.addClient(testClient);
		sut = new BankDAOImpl();
		sut.createTables();
	}

	@After
	public void tearDown() throws DAOException {
		sut.dropTables();
	}


	// test save()
	@Test
	public void testInsert() throws IllegalAccessException, DAOException {
		sut.save(bank);

		Bank bank2 = sut.getBankByName(BANK_NAME);

		assertTrue(TestService.isEquals(bank, bank2));
	}

	@Test
	public void testUpdate() throws DAOException, IllegalAccessException {
		sut.save(bank);

		// Make changes to Bank
		Client testClient2 = newSecondClient();
		SavingAccount testSavingAccount = newSavingAccount();
		testClient2.addAccount(testSavingAccount);
		testClient2.setActiveAccount(testSavingAccount);
		bank.addClient(testClient2);
		sut.save(bank);

		Bank bank2 = sut.getBankByName(BANK_NAME);

		bank.printReport();
		bank2.printReport();

		assertTrue(TestService.isEquals(bank, bank2));
	}

}