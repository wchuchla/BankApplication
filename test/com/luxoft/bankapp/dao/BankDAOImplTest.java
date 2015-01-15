package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.BankNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.helper.dao.AbstractDbUnitTemplateTestCase;
import com.luxoft.bankapp.helper.dao.DataSets;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.luxoft.bankapp.helper.entity.BankEntityHelper.*;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class BankDAOImplTest extends AbstractDbUnitTemplateTestCase {

    // test getBankByName(String name)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml")
    public void testGetBankByName() throws Exception {
        Bank testBank = BANK_DAO.getBankByName(BANK_NAME);

        assertBank(testBank);
    }

    @Test(expected = BankNotFoundException.class)
    @DataSets(assertDataSet = "/DBUnit/one-bank.xml")
    public void testNotExistingBankInGetBankByNameThrowsException() throws DAOException, AccountExistsException {
        BANK_DAO.getBankByName(BANK_INVALID_NAME);
    }


    // test save(Bank bank)
    @Test
    @DataSets(assertDataSet = "/DBUnit/one-bank.xml")
    public void testSave() throws DAOException {
        Bank bank = newBank();
        BANK_DAO.save(bank);
    }


    // test remove(Bank bank)
    @Test
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml", assertDataSet = "/DBUnit/empty.xml")
    public void testRemove() throws DAOException {
        Bank testBank = newBank();
        BANK_DAO.remove(testBank);
    }


    // test remove(Bank bank)
    @Test(expected = BankNotFoundException.class)
    @DataSets(setUpDataSet = "/DBUnit/one-bank.xml", assertDataSet = "/DBUnit/one-bank.xml")
    public void testNotExistingBankInRemoveThrowsException() throws DAOException {
        Bank testBank = new Bank(BANK_INVALID_NAME);
        BANK_DAO.remove(testBank);
    }
}