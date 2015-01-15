package com.luxoft.bankapp.dao;

import com.luxoft.bankapp.exception.daoexception.DAOException;

import java.sql.Connection;

public interface BaseDAO {
    Connection openConnection() throws DAOException;

    void closeConnection() throws DAOException;

    void createTables() throws DAOException;

    void dropTables() throws DAOException;
}