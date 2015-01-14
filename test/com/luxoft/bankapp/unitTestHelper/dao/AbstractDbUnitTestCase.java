package com.luxoft.bankapp.unitTestHelper.dao;

import com.luxoft.bankapp.dao.AccountDAOImpl;
import com.luxoft.bankapp.dao.BankDAOImpl;
import com.luxoft.bankapp.dao.ClientDAOImpl;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2Connection;
import org.junit.After;
import org.junit.Before;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;

import static org.junit.Assert.assertNotNull;

public class AbstractDbUnitTestCase {

	protected static final BankDAOImpl bankDAO = new BankDAOImpl();
	protected static final ClientDAOImpl clientDAO = new ClientDAOImpl();
	protected static final AccountDAOImpl accountDAO = new AccountDAOImpl();
	protected static H2Connection dbunitConnection;
	private static Connection connection;

	private static IDataSet getDataSet(String name) throws Exception {
		InputStream inputStream = AbstractDbUnitTestCase.class.getResourceAsStream(name);
		assertNotNull("file" + name + " not found in classpath", inputStream);
		Reader reader = new InputStreamReader(inputStream);
		return new FlatXmlDataSetBuilder().build(reader);
	}

	public static IDataSet getReplacedDataSet(String name, long id) throws Exception {
		IDataSet originalDataSet = getDataSet(name);
		return getReplacedDataSet(originalDataSet, id);
	}

	private static IDataSet getReplacedDataSet(IDataSet originalDataSet, long id) {
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(originalDataSet);
		replacementDataSet.addReplacementObject("[ID]", id);
		replacementDataSet.addReplacementObject("[NULL]", null);
		return replacementDataSet;
	}

	@Before
	public void setupDatabase() throws Exception {
		connection = bankDAO.openConnection();
		dbunitConnection = new H2Connection(connection, null);
		bankDAO.createTables();
	}

	@After
	public void closeDatabase() throws Exception {
		bankDAO.dropTables();
		if (connection != null) {
			connection.close();
			connection = null;
		}
		if (dbunitConnection != null) {
			dbunitConnection.close();
			dbunitConnection = null;
		}
	}

}