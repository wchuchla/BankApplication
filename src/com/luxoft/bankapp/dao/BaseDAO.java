package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;

import java.sql.Connection;

public interface BaseDAO {
	Connection openConnection() throws DAOException;

	void closeConnection();

	void createTables() throws DAOException;

	void dropTables() throws DAOException;
}