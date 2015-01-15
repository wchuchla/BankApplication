package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.*;
import com.luxoft.bankapp.unitTestHelper.dao.AbstractDbUnitTemplateTestCase;
import com.luxoft.bankapp.unitTestHelper.dao.DataSets;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.BANK_NAME;
import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.assertCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.CLIENT_NAME;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.assertSavingAccount;
import static com.luxoft.bankapp.unitTestHelper.entity.SavingAccountEntityHelper.newSavingAccount;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class AccountDAOImplTest extends AbstractDbUnitTemplateTestCase {

    // test save(Client client, Account account)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-checking-account.xml")
    public void testInsertCheckingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);

        CheckingAccount testCheckingAccount = newCheckingAccount();
        accountDAO.save(testClient, testCheckingAccount);

        List<Account> accounts = accountDAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertCheckingAccount((CheckingAccount) account);
        }
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-checking-account.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-checking-account.xml")
    public void testUdpateCheckingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);
        Account testCheckingAccount = accountDAO.getClientAccounts(testClient.getId()).get(0);

        accountDAO.save(testClient, testCheckingAccount);

        List<Account> accounts = accountDAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertCheckingAccount((CheckingAccount) account);
        }
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-saving-account.xml")
    public void testInsertSavingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);

        SavingAccount testSavingAccount = newSavingAccount();
        accountDAO.save(testClient, testSavingAccount);

        List<Account> accounts = accountDAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertSavingAccount((SavingAccount) account);
        }
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-saving-account.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-saving-account.xml")
    public void testUdpateSavingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);
        Account testSavingAccount = accountDAO.getClientAccounts(testClient.getId()).get(0);

        accountDAO.save(testClient, testSavingAccount);

        List<Account> accounts = accountDAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertSavingAccount((SavingAccount) account);
        }
    }


    // test removeByClientId(int idClient)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-saving-account.xml", assertDataSet = "/DBUnit/one-bank-one-client.xml")
    public void removeSavingAccount() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);

        accountDAO.removeByClientId(testClient.getId());
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-checking-account.xml",
            assertDataSet = "/DBUnit/one-bank-one-client.xml")
    public void removeCheckingAccount() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);

        accountDAO.removeByClientId(testClient.getId());
    }


    // test getClientAccounts(int idClient)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-saving-account.xml")
    public void testGetClientAccounts() throws DAOException, AccountExistsException {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);
        Client testClient = clientDAO.findClientByName(testBank, CLIENT_NAME);

        List<Account> accounts = accountDAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertSavingAccount((SavingAccount) account);
        }
    }
}