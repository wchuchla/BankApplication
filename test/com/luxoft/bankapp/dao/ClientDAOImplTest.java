package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.unitTestHelper.dao.AbstractDbUnitTemplateTestCase;
import com.luxoft.bankapp.unitTestHelper.dao.DataSets;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.BANK_NAME;
import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.*;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class ClientDAOImplTest extends AbstractDbUnitTemplateTestCase {

    // test Client findClientByName
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-two-accounts.xml")
    public void testFindClientByName() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        Client testClient = clientDAO.findClientByName(bank, CLIENT_NAME);

        assertClient(testClient);
    }

    @Test(expected = ClientNotFoundException.class)
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-two-accounts.xml")
    public void testNotExistingClientInFindClientByNameThrowsException() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        clientDAO.findClientByName(bank, CLIENT_INVALID_NAME);
    }


    // test getAllClients(Bank bank)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client-two-accounts.xml")
    public void testGetAllClients() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        List<Client> clients = clientDAO.getAllClients(bank);

        clients.forEach(com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper::assertClient);
    }


    // test void save(Bank bank, Client client)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml", assertDataSet = "/DBUnit/one-bank-one-client.xml")
    public void testInsertInSave() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        Client testClient = newClient();
        clientDAO.save(bank, testClient);

        Client selectClient = clientDAO.findClientByName(bank, CLIENT_NAME);
        assertClient(selectClient);
    }

    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet = "/DBUnit/one-bank-one-client.xml")
    public void testUpdateInSave() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        Client testClient = clientDAO.findClientByName(bank, CLIENT_NAME);
        clientDAO.save(bank, testClient);

        Client selectClient = clientDAO.findClientByName(bank, CLIENT_NAME);
        assertClient(selectClient);
    }


    // test remove(Bank bank, Client client)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet = "/DBUnit/one-bank.xml")
    public void testRemove() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        Client testClient = clientDAO.findClientByName(bank, CLIENT_NAME);
        clientDAO.remove(bank, testClient);
    }

    @Test(expected = ClientNotFoundException.class)
    @DataSets(setUpDataSet = "/DBUnit/one-bank-one-client.xml", assertDataSet = "/DBUnit/one-bank-one-client.xml.xml")
    public void testNotExistingClientInRemove() throws DAOException, AccountExistsException {
        Bank bank = bankDAO.getBankByName(BANK_NAME);

        Client testClient = newClient();

        clientDAO.remove(bank, testClient);
    }
}