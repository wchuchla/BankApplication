package com.luxoft.bankapp.service;

import com.luxoft.bankapp.model.Client;

public interface ClientRegistrationListener {
	void onClientAdded(Client c);
}