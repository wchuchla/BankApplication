package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.BankNotFoundException;
import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.info.BankInfo;
import com.luxoft.bankapp.model.info.BankInfoCalc;
import com.luxoft.bankapp.service.BankCommander;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BankDAOImpl extends BaseDAOImpl implements BankDAO {

	private static final String GET_BANK_BY_NAME_SELECT = "SELECT ID, NAME FROM BANK WHERE name=?";
	private static final String SAVE_INSERT = "INSERT INTO BANK (name) VALUES (?)";
	private static final String SAVE_SELECT = "SELECT ID FROM BANK WHERE NAME=?";
	private static final String SAVE_UPDATE = "UPDATE BANK SET NAME=? WHERE ID=?";
	private static final String REMOVE_DELETE = "DELETE FROM BANK WHERE name=?";
	private static final Logger LOGGER = Logger.getLogger(BankDAOImpl.class.getName());

	@Override
	public Bank getBankByName(String name) throws DAOException {
		Bank bank = new Bank(name);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = openConnection();
			statement = connection.prepareStatement(GET_BANK_BY_NAME_SELECT);
			statement.setString(1, name);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("ID");
				bank.setId(id);
				ClientDAOImpl clientDAO = new ClientDAOImpl();
				List<Client> clients = clientDAO.getAllClients(bank);
				clients.forEach(bank::addClient);
			} else {
				throw new BankNotFoundException(name);
			}
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException();
		} finally {
			close(resultSet, statement);
			closeConnection();
		}
		return bank;
	}

	@Override
	public void save(Bank bank) throws DAOException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		if (bank.getId() == 0) {
			try {
				openConnection();
				statement = connection.prepareStatement(SAVE_INSERT);
				statement.setString(1, bank.getName());
				statement.executeUpdate();

				statement = connection.prepareStatement(SAVE_SELECT);
				statement.setString(1, bank.getName());
				resultSet = statement.executeQuery();
				resultSet.next();
				bank.setId(resultSet.getInt("ID"));

				ClientDAOImpl clientDAO = new ClientDAOImpl();
				Map<String, Client> clients = bank.getClients();
				for (Client client : clients.values()) {
					clientDAO.save(bank, client);
				}
			} catch (SQLException e) {
				LOGGER.error(e);
				throw new DAOException();
			} finally {
				close(resultSet, statement);
				closeConnection();
			}
		} else {
			try {
				openConnection();
				statement = connection.prepareStatement(SAVE_UPDATE);
				statement.setString(1, bank.getName());
				statement.setInt(2, bank.getId());
				int rows = statement.executeUpdate();
				if (rows == 0) {
					throw new DAOException();
				}

				ClientDAOImpl clientDAO = new ClientDAOImpl();
				Map<String, Client> clients = bank.getClients();
				for (Client client : clients.values()) {
					clientDAO.save(bank, client);
				}
			} catch (SQLException e) {
				LOGGER.error(e);
				throw new DAOException();
			} finally {
				close(resultSet, statement);
				closeConnection();
			}
		}
	}

	@Override
	public void remove(Bank bank) throws DAOException {
		PreparedStatement statement = null;
		try {
			openConnection();
			statement = connection.prepareStatement(REMOVE_DELETE);
			statement.setString(1, bank.getName());
			int rows = statement.executeUpdate();
			if (rows == 0) {
				throw new BankNotFoundException(bank.getName());
			}
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException();
		} finally {
			close(statement);
			closeConnection();
		}
	}

	@Override
	public BankInfo getBankInfo() {
		BankInfo bankInfo = new BankInfo();
		bankInfo.setName(BankInfoCalc.getName(BankCommander.activeBank));
		bankInfo.setNumberOfClients(BankInfoCalc.calcNumberOfClients(BankCommander.activeBank));
		bankInfo.setNumberOfAccounts(BankInfoCalc.calcNumberOfAccounts(BankCommander.activeBank));
		bankInfo.setClientsSet(BankInfoCalc.getClientsSet(BankCommander.activeBank));
		bankInfo.setDepositSum(BankInfoCalc.calcDepositSum(BankCommander.activeBank));
		bankInfo.setCreditSum(BankInfoCalc.calcCreditSum(BankCommander.activeBank));
		bankInfo.setClientsByCity(BankInfoCalc.clientsByCity(BankCommander.activeBank));
		return bankInfo;
	}

}