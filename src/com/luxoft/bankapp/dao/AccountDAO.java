package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Client;

import java.util.List;

public interface AccountDAO {
    public void save(Client client, Account account) throws DAOException;

    public void removeByClientId(int idClient) throws DAOException;

    public List<Account> getClientAccounts(int idClient) throws DAOException;
}