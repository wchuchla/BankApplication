package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.info.BankInfo;

public interface BankDAO {

    Bank getBankByName(String name) throws DAOException, AccountExistsException;

    void save(Bank bank) throws DAOException;

    void remove(Bank bank) throws DAOException;

    BankInfo getBankInfo();
}