package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.BankNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.unitTestHelper.dao.AbstractDbUnitTemplateTestCase;
import com.luxoft.bankapp.unitTestHelper.dao.DataSets;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.luxoft.bankapp.unitTestHelper.entity.BankEntityHelper.*;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class BankDAOImplTest extends AbstractDbUnitTemplateTestCase {

    // test getBankByName(String name)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml")
    public void testGetBankByName() throws Exception {
        Bank testBank = bankDAO.getBankByName(BANK_NAME);

        assertBank(testBank);
    }

    @Test(expected = BankNotFoundException.class)
    @DataSets(assertDataSet = "/DBUnit/one-bank.xml")
    public void testNotExistingBankInGetBankByNameThrowsException() throws DAOException, AccountExistsException {
        bankDAO.getBankByName(BANK_INVALID_NAME);
    }


    // test save(Bank bank)
    @Test
    @DataSets(assertDataSet = "/DBUnit/one-bank.xml")
    public void testSave() throws DAOException {
        Bank bank = newBank();
        bankDAO.save(bank);
    }


    // test remove(Bank bank)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml", assertDataSet = "/DBUnit/empty.xml")
    public void testRemove() throws DAOException {
        Bank testBank = newBank();
        bankDAO.remove(testBank);
    }


    // test remove(Bank bank)
    @Test(expected = BankNotFoundException.class)
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml", assertDataSet = "/DBUnit/one-bank.xml")
    public void testNotExistingBankInRemoveThrowsException() throws DAOException {
        Bank testBank = new Bank(BANK_INVALID_NAME);
        bankDAO.remove(testBank);
    }
}