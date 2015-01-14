package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;

import java.util.List;

public interface ClientDAO {
	Client findClientByName(Bank bank, String name) throws DAOException;

	List<Client> getAllClients(Bank bank) throws DAOException;

	void save(Bank bank, Client client) throws DAOException;

	void remove(Bank bank, Client client) throws DAOException;
}