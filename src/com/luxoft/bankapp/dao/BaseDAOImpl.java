package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseDAOImpl implements BaseDAO {
	private static final String CONNECTION = "jdbc:h2:tcp://localhost/~/BankDB";
	private static final String LOGIN = "admin";
	private static final String PASSWORD = "admin";
	private static final String DRIVER = "org.h2.Driver";
	Connection connection;

	private static final Logger LOGGER = Logger.getLogger(BaseDAOImpl.class.getName());

	@Override
	public Connection openConnection() throws DAOException {
		try {
			Class.forName(DRIVER);
			connection = DriverManager.getConnection(CONNECTION, LOGIN,
					PASSWORD);
			return connection;
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DAOException();
		}
	}

	@Override
	public void closeConnection() throws DAOException {
		try {
			connection.close();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DAOException();
		}
	}

	public void close(ResultSet resultSet, Statement statement) throws DAOException {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				throw new DAOException();
			}
		}
		close(statement);
	}

	public void close(Statement statement) throws DAOException {
		try {
			statement.close();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DAOException();		}
	}

	@Override
	public void createTables() throws DAOException {
		Statement statement = null;
		try {
			openConnection();
			statement = connection.createStatement();
			statement.execute("CREATE TABLE BANK (\n" +
					"    ID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,\n" +
					"    NAME VARCHAR(255) NOT NULL,\n" +
					");");

			statement.execute("CREATE TABLE CLIENT (\n" +
					"\tID INT AUTO_INCREMENT NOT NULL PRIMARY KEY,\n" +
					"\tNAME VARCHAR(50) NOT NULL,\n" +
					"\tGENDER VARCHAR(6) NOT NULL, \n" +
					"\tEMAIL VARCHAR(40) NOT NULL,\n" +
					"\tPHONE_NUMBER VARCHAR(20) NOT NULL,\n" +
					"\tCITY VARCHAR(50) NOT NULL,\n" +
					"\tINITIAL_OVERDRAFT REAL NOT NULL,\n" +
					"\tBANK_ID INT NOT NULL,\n" +
					"\tFOREIGN KEY (BANK_ID)\n" +
					"\tREFERENCES BANK(ID)\n" +
					");");

			statement.execute("CREATE TABLE ACCOUNT ( \n" +
					"\tID INT AUTO_INCREMENT NOT NULL PRIMARY KEY , \n" +
					"\tCLIENT_ID INT NOT NULL,\n" +
					"\tACCOUNT_NUMBER INT NOT NULL,\n" +
					"    TYPE VARCHAR(1) NOT NULL,\n" +
					"\tBALANCE REAL NOT NULL,\n" +
					"    OVERDRAFT REAL,\n" +
					"\tFOREIGN KEY (CLIENT_ID)\n" +
					"\tREFERENCES CLIENT(ID)\n" +
					");");
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DAOException();
		} finally {
			close(statement);
			closeConnection();
		}

	}

	@Override
	public void dropTables() throws DAOException {
		Statement statement = null;
		try {
			openConnection();
			statement = connection.createStatement();
			statement.execute("DROP TABLE ACCOUNT;");
			statement.execute("DROP TABLE CLIENT;");
			statement.execute("DROP TABLE BANK;");

		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new DAOException();
		} finally {
			close(statement);
			closeConnection();
		}
	}
}