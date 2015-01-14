package com.luxoft.bankapp.service.server.mock;

import com.luxoft.bankapp.model.Client;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.luxoft.bankapp.unitTestHelper.entity.ClientEntityHelper.newClient;

public class BankClientMock implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(BankClientMock.class.getName());
	private final Client client;

	public BankClientMock(Client client) {
		this.client = client;
	}

	public static void main(String[] args) {
		BankClientMock bankClientMock = new BankClientMock(newClient());
		bankClientMock.run();
	}

	@Override
	public void run() {
		String server = "localhost";
		int port = 2004;
		try (Socket requestSocket = new Socket(server, port);
			 ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());
			 ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream())) {
			out.flush();
			serverCommunication(in, out);
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("You are trying to connect to an unknown host!");
		}
	}

	private void serverCommunication(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
		String clientType = "atm";
		sendMessage(out, clientType);
		in.readObject();
		sendMessage(out, client.getName());
		in.readObject();
		sendMessage(out, "1");
		in.readObject();
		sendMessage(out, "saving");
		in.readObject();
		sendMessage(out, "20");
		in.readObject();
		sendMessage(out, "no");
		in.readObject();
		sendMessage(out, "\n");
	}

	void sendMessage(ObjectOutputStream out, final String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
}