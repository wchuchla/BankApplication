package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.*;
import com.luxoft.bankapp.helper.dao.AbstractDbUnitTemplateTestCase;
import com.luxoft.bankapp.helper.dao.DataSets;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.BANK_NAME;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.assertCheckingAccount;
import static com.luxoft.bankapp.helper.entity.CheckingAccountEntityHelper.newCheckingAccount;
import static com.luxoft.bankapp.helper.entity.ClientEntityHelper.CLIENT_NAME;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.assertSavingAccount;
import static com.luxoft.bankapp.helper.entity.SavingAccountEntityHelper.newSavingAccount;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class AccountDAOImplTest extends AbstractDbUnitTemplateTestCase {

    // test save(Client client, Account account)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-checking-account.xml")
    public void testInsertCheckingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);

        CheckingAccount testCheckingAccount = newCheckingAccount();
        ACCOUNT_DAO.save(testClient, testCheckingAccount);

        List<Account> accounts = ACCOUNT_DAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertCheckingAccount((CheckingAccount) account);
        }
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-checking-account.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-checking-account.xml")
    public void testUdpateCheckingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);
        Account testCheckingAccount = ACCOUNT_DAO.getClientAccounts(testClient.getId()).get(0);

        ACCOUNT_DAO.save(testClient, testCheckingAccount);

        List<Account> accounts = ACCOUNT_DAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertCheckingAccount((CheckingAccount) account);
        }
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-saving-account.xml")
    public void testInsertSavingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);

        SavingAccount testSavingAccount = newSavingAccount();
        ACCOUNT_DAO.save(testClient, testSavingAccount);

        List<Account> accounts = ACCOUNT_DAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertSavingAccount((SavingAccount) account);
        }
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-saving-account.xml", assertDataSet =
            "/DBUnit/one-bank-one-client-one-saving-account.xml")
    public void testUdpateSavingAccountInSave() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);
        Account testSavingAccount = ACCOUNT_DAO.getClientAccounts(testClient.getId()).get(0);

        ACCOUNT_DAO.save(testClient, testSavingAccount);

        List<Account> accounts = ACCOUNT_DAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertSavingAccount((SavingAccount) account);
        }
    }


    // test removeByClientId(int idClient)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-saving-account.xml", assertDataSet = "/DBUnit/one-bank-one-client.xml")
    public void removeSavingAccount() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);

        ACCOUNT_DAO.removeByClientId(testClient.getId());
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-checking-account.xml",
            assertDataSet = "/DBUnit/one-bank-one-client.xml")
    public void removeCheckingAccount() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);

        ACCOUNT_DAO.removeByClientId(testClient.getId());
    }


    // test getClientAccounts(int idClient)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-one-saving-account.xml")
    public void testGetClientAccounts() throws DAOException, AccountExistsException {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);
        Client testClient = CLIENT_DAO.findClientByName(testBank, CLIENT_NAME);

        List<Account> accounts = ACCOUNT_DAO.getClientAccounts(testClient.getId());

        for (Account account : accounts) {
            assertSavingAccount((SavingAccount) account);
        }
    }
}