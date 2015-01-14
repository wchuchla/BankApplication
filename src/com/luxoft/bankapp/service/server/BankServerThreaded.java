package com.luxoft.bankapp.service.server;

import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.service.BankFeedService;
import com.luxoft.bankapp.service.BankServiceImpl;
import com.luxoft.bankapp.service.ClientService;
import com.luxoft.bankapp.service.ClientServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankServerThreaded {
	public static final int POOL_SIZE = 10;
	public static final AtomicInteger CLIENT_COUNTER = new AtomicInteger(0);
	private static final Logger LOGGER = Logger.getLogger(BankServerThreaded.class.getName());
	private final Bank currentBank = new Bank("Bank");
	private final BankServerMonitor monitor = new BankServerMonitor();
	private final Thread monitorThread = new Thread(monitor);
	private volatile boolean running = true;
	private ClientService clientService = new ClientServiceImpl(currentBank, new BankServiceImpl());
	private ServerSocket serverSocket;
	private ExecutorService threadPool;

	public static void main(String[] args) {
		BankServerThreaded bankServerThreaded = new BankServerThreaded();
		bankServerThreaded.run();
	}

	public void run() {
		try {
			initialize();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}

		while (running) {
			try {
				Socket clientSocket = serverSocket.accept();
				CLIENT_COUNTER.incrementAndGet();
				threadPool.execute(new ServerThread(clientSocket, currentBank, clientService));
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void initialize() throws IOException {
		BankFeedService feedService = new BankFeedService(currentBank);
		feedService.loadFeed();
		int port = 2004;
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(20);
		threadPool = Executors.newFixedThreadPool(POOL_SIZE);
		monitorThread.setDaemon(true);
		monitorThread.start();
	}

	public void shutdown() {
		running = false;
	}

	public ClientService getClientService() {
		return clientService;
	}

	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}
}