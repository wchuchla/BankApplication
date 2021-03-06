package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;
import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountDAOImpl extends BaseDAOImpl implements AccountDAO {

    private static final String SAVE_INSERT =
            "INSERT INTO ACCOUNT (CLIENT_ID, ACCOUNT_NUMBER, TYPE, BALANCE, OVERDRAFT) VALUES (?,?,?,?,?)";
    private static final String SAVE_SELECT = "SELECT ID FROM ACCOUNT WHERE CLIENT_ID=? AND ACCOUNT_NUMBER=?";
    private static final String SAVE_UPDATE =
            "UPDATE ACCOUNT SET CLIENT_ID=?, ACCOUNT_NUMBER=?, TYPE=?, BALANCE=?, OVERDRAFT=? WHERE ID=?";
    private static final String REMOVE_BY_CLIENT_ID_DELETE = "DELETE FROM ACCOUNT WHERE CLIENT_ID=?";
    private static final String GET_CLIENTS_ACCOUNTS_SELECT = "SELECT * FROM ACCOUNT WHERE CLIENT_ID=?";

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + AccountDAOImpl.class.getName());
    private static final Logger DB_LOGGER = Logger.getLogger("LogDB." + AccountDAOImpl.class.getName());

    @Override
    public void save(Client client, Account account) throws DAOException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        if (account.getId() == 0) {
            DB_LOGGER.log(Level.INFO, "BankDB.ACCOUNT: Saving the account");
            try {
                openConnection();
                statement = connection.prepareStatement(SAVE_INSERT);
                statement.setInt(1, client.getId());
                statement.setInt(2, account.getAccountNumber());
                if (account instanceof SavingAccount) {
                    statement.setString(3, "S");
                    statement.setFloat(5, 0);
                } else if (account instanceof CheckingAccount) {
                    statement.setString(3, "C");
                    statement.setFloat(5, client.getInitialOverdraft());
                }
                statement.setFloat(4, account.getBalance());
                int rows = statement.executeUpdate();
                if (rows == 0) {
                    throw new DAOException();
                }
                statement = connection.prepareStatement(SAVE_SELECT);
                statement.setInt(1, client.getId());
                statement.setInt(2, account.getAccountNumber());
                resultSet = statement.executeQuery();
                resultSet.next();
                account.setId(resultSet.getInt("ID"));
            } catch (SQLException e) {
                EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
                DB_LOGGER.log(Level.SEVERE, "BankDB.ACCOUNT: An error occurred when saving the account ", e);
                throw new DAOException();
            } finally {
                close(resultSet, statement);
                closeConnection();
            }
        } else {
            DB_LOGGER.log(Level.INFO, "BankDB.ACCOUNT: Updating the account");
            try {
                openConnection();
                statement = connection.prepareStatement(SAVE_UPDATE);
                statement.setInt(1, client.getId());
                statement.setInt(2, account.getAccountNumber());
                if (account instanceof SavingAccount) {
                    statement.setString(3, "S");
                    statement.setFloat(5, 0);
                } else if (account instanceof CheckingAccount) {
                    statement.setString(3, "C");
                    statement.setFloat(5, client.getInitialOverdraft());
                }
                statement.setFloat(4, account.getBalance());
                statement.setInt(6, account.getId());
                int rows = statement.executeUpdate();
                if (rows == 0) {
                    throw new DAOException();
                }
            } catch (SQLException e) {
                EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
                DB_LOGGER.log(Level.SEVERE, "BankDB.ACCOUNT: An error occurred when updating the account ", e);
                throw new DAOException();
            } finally {
                close(resultSet, statement);
                closeConnection();
            }
        }
    }

    @Override
    public void removeByClientId(int clientId) throws DAOException {
        DB_LOGGER.log(Level.INFO, "BankDB.ACCOUNT: Removing all account's of the client. Client's ID: " + clientId);
        String delete = REMOVE_BY_CLIENT_ID_DELETE;
        PreparedStatement statement = null;
        try {
            openConnection();
            statement = connection.prepareStatement(delete);
            statement.setInt(1, clientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
            DB_LOGGER.log(Level.SEVERE, "BankDB.ACCOUNT: An error occurred when removing all accounts of the client. "
                    + "Client's ID: " + clientId, e);
            throw new DAOException();
        } finally {
            close(statement);
            closeConnection();
        }
    }

    @Override
    public List<Account> getClientAccounts(int clientId) throws DAOException {
        DB_LOGGER.log(Level.INFO, "BankDB.ACCOUNT: Getting all accounts of the client. Client's ID: " + clientId);
        List<Account> accounts = new ArrayList<>();
        String select = GET_CLIENTS_ACCOUNTS_SELECT;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            openConnection();
            statement = connection.prepareStatement(select);
            statement.setInt(1, clientId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if ("S".equals(resultSet.getString("TYPE"))) {
                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setId(resultSet.getInt("ID"));
                    savingAccount.setAccountNumber(resultSet.getInt("ACCOUNT_NUMBER"));
                    savingAccount.setBalance(resultSet.getFloat("BALANCE"));
                    accounts.add(savingAccount);
                } else if ("C".equals(resultSet.getString("TYPE"))) {
                    CheckingAccount checkingAccount = new CheckingAccount();
                    checkingAccount.setId(resultSet.getInt("ID"));
                    checkingAccount.setAccountNumber(resultSet.getInt("ACCOUNT_NUMBER"));
                    checkingAccount.setOverdraft(resultSet.getFloat("OVERDRAFT"));
                    checkingAccount.setBalance(resultSet.getFloat("BALANCE"));
                    accounts.add(checkingAccount);
                }
            }
        } catch (SQLException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
            DB_LOGGER.log(Level.SEVERE, "BankDB.ACCOUNT: An error occurred when getting all accounts of the client. " +
                    "Client's ID: " + clientId, e);
            throw new DAOException();
        } finally {
            close(resultSet, statement);
            closeConnection();
        }
        return accounts;
    }
}