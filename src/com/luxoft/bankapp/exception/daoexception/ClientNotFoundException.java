package com.luxoft.bankapp.exception.daoexception;

public class ClientNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;
	private final String name;

	public ClientNotFoundException(String name) {
		this.name = name;
	}

	@Override
	public String getMessage() {
		return "Client " + name + " not found";
	}

}