package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.AccountExistsException;
import com.luxoft.bankapp.exception.daoexception.ClientNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.enums.Gender;
import com.luxoft.bankapp.service.BankServiceImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAOImpl extends BaseDAOImpl implements ClientDAO {

	private static final String FIND_CLIENT_BY_NAME_SELECT = "SELECT * FROM CLIENT WHERE BANK_ID=? AND NAME=?";
	private static final String GET_ALL_CLIENTS_SELECT = "SELECT * FROM CLIENT WHERE BANK_ID=?";
	private static final String REMOVE_DELETE = "DELETE FROM CLIENT WHERE ID=?";
	private static final String SAVE_INSERT =
			"INSERT INTO CLIENT (NAME, GENDER, EMAIL, PHONE_NUMBER, CITY, INITIAL_OVERDRAFT, BANK_ID) VALUES (?,?,?,?,?,?,?)";
	private static final String SAVE_SELECT = "SELECT ID FROM CLIENT WHERE NAME=?";
	private static final String SAVE_UPDATE =
			"UPDATE CLIENT SET NAME=?, GENDER=?, EMAIL=?, PHONE_NUMBER=?, CITY=?, INITIAL_OVERDRAFT=?, BANK_ID=? WHERE ID=?";

	private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + ClientDAOImpl.class.getName());
	private static final Logger DB_LOGGER = Logger.getLogger("LogDB." + ClientDAOImpl.class.getName());

	@Override
	public Client findClientByName(Bank bank, String name) throws DAOException, AccountExistsException {
		DB_LOGGER.log(Level.INFO, "BankDB.CLIENT: Getting the client by his name. Client's name: " + name);
		Client client = new Client(name);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			openConnection();
			statement = connection.prepareStatement(FIND_CLIENT_BY_NAME_SELECT);
			statement.setInt(1, bank.getId());
			statement.setString(2, name);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				client.setId(resultSet.getInt("ID"));
				client.setGender(Gender.valueOf(resultSet.getString("GENDER")));
				client.setEmail(resultSet.getString("EMAIL"));
				client.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
				client.setCity(resultSet.getString("CITY"));
				client.setInitialOverdraft(resultSet.getFloat("INITIAL_OVERDRAFT"));
				AccountDAOImpl accountDAO = new AccountDAOImpl();
				List<Account> accounts = accountDAO.getClientAccounts(client.getId());
				BankServiceImpl bankService = new BankServiceImpl();
				for (Account account : accounts) {
					bankService.addAccount(client, account);
				}
			} else {
				DB_LOGGER.log(Level.WARNING, "BankDB.CLIENT: Unable to get client. Client " + name + " not found.");
				throw new ClientNotFoundException(name);
			}
		} catch (SQLException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
			DB_LOGGER.log(Level.SEVERE, "BankDB.CLIENT: An error occurred when getting the client " + name, e);
			throw new DAOException();
		} finally {
			close(resultSet, statement);
			closeConnection();
		}
		return client;
	}

	@Override
	public List<Client> getAllClients(Bank bank) throws DAOException, AccountExistsException {
		DB_LOGGER.log(Level.INFO, "BankDB.CLIENT: Getting all clients of the bank. Bank's name: " + bank.getName());
		List<Client> clients = new ArrayList<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			openConnection();
			statement = connection.prepareStatement(GET_ALL_CLIENTS_SELECT);
			statement.setInt(1, bank.getId());
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Client client = new Client(resultSet.getString("NAME"), Gender.valueOf(resultSet
						.getString("Gender")), resultSet.getString("EMAIL"),
						resultSet.getString("PHONE_NUMBER"), resultSet.getString("CITY"),
						resultSet.getFloat("INITIAL_OVERDRAFT"));
				client.setId(resultSet.getInt("ID"));
				AccountDAOImpl accountDAO = new AccountDAOImpl();
				List<Account> accounts = accountDAO.getClientAccounts(client.getId());
				BankServiceImpl bankService = new BankServiceImpl();
				for (Account account : accounts) {
					bankService.addAccount(client, account);
				}
				clients.add(client);
			}
		} catch (SQLException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
			DB_LOGGER.log(Level.SEVERE, "BankDB.CLIENT: An error occurred when getting all client's. Bank name: " +
					bank.getName(), e);
			throw new DAOException();
		} finally {
			close(resultSet, statement);
			closeConnection();
		}
		return clients;
	}

	@Override
	public void save(Bank bank, Client client) throws DAOException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		if (client.getId() == 0) {
			DB_LOGGER.log(Level.INFO, "BankDB.CLIENT: Saving the client. Client's name: " + client.getName());
			try {
				openConnection();
				statement = connection.prepareStatement(SAVE_INSERT);
				statement.setString(1, client.getName());
				statement.setString(2, client.getGender().toString());
				statement.setString(3, client.getEmail());
				statement.setString(4, client.getPhoneNumber());
				statement.setString(5, client.getCity());
				statement.setFloat(6, client.getInitialOverdraft());
				statement.setInt(7, bank.getId());
				int rows = statement.executeUpdate();
				if (rows == 0) {
					throw new DAOException();
				}

				statement = connection.prepareStatement(SAVE_SELECT);
				statement.setString(1, client.getName());
				resultSet = statement.executeQuery();
				resultSet.next();
				client.setId(resultSet.getInt("ID"));

				AccountDAOImpl accountDAO = new AccountDAOImpl();
				Set<Account> accounts = client.getAccounts();
				for (Account account : accounts) {
					accountDAO.save(client, account);
				}

			} catch (SQLException e) {
				EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
				DB_LOGGER.log(Level.SEVERE, "BankDB.CLIENT: An error occurred when saving the client "
						+ client.getName(),	e);
				throw new DAOException();
			} finally {
				close(resultSet, statement);
				closeConnection();
			}
		} else {
			DB_LOGGER.log(Level.INFO, "BankDB.CLIENT: Updating the client. Client's name: " + client.getName());
			try {
				openConnection();
				statement = connection.prepareStatement(SAVE_UPDATE);
				statement.setString(1, client.getName());
				statement.setString(2, client.getGender().toString());
				statement.setString(3, client.getEmail());
				statement.setString(4, client.getPhoneNumber());
				statement.setString(5, client.getCity());
				statement.setFloat(6, client.getInitialOverdraft());
				statement.setInt(7, bank.getId());
				statement.setInt(8, client.getId());
				int rows = statement.executeUpdate();
				if (rows == 0) {
					throw new DAOException();
				}

				AccountDAOImpl accountDAO = new AccountDAOImpl();
				Set<Account> accounts = client.getAccounts();
				for (Account account : accounts) {
					accountDAO.save(client, account);
				}
			} catch (SQLException e) {
				EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
				DB_LOGGER.log(Level.SEVERE, "BankDB.CLIENT: An error occurred when updating the client "
						+ client.getName(), e);
				throw new DAOException();
			} finally {
				close(resultSet, statement);
				closeConnection();
			}
		}
	}

	@Override
	public void remove(Bank bank, Client client) throws DAOException {
		DB_LOGGER.log(Level.INFO, "BankDB.CLIENT: Removing the client. Client's name: " + client.getName());
		PreparedStatement statement = null;
		try {
			openConnection();
			AccountDAOImpl accountDAOImpl = new AccountDAOImpl();
			accountDAOImpl.removeByClientId(client.getId());
			statement = connection.prepareStatement(REMOVE_DELETE);
			statement.setInt(1, client.getId());
			int rows = statement.executeUpdate();
			if (rows == 0) {
				throw new ClientNotFoundException(client.getName());
			}
		} catch (SQLException e) {
			EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DAOException();
		} finally {
			close(statement);
			closeConnection();
		}
	}
}