package com.luxoft.bankapp.service.server;

import org.apache.log4j.Logger;

public class BankServerMonitor implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(BankServerMonitor.class.getName());
	private volatile boolean running = true;

	@Override
	public void run() {
		while (running) {
			try {
				int awaitingClientCounter = 0;
				if (BankServerThreaded.CLIENT_COUNTER.intValue() >= BankServerThreaded.POOL_SIZE) {
					awaitingClientCounter = BankServerThreaded.CLIENT_COUNTER.intValue()
							- BankServerThreaded.POOL_SIZE;
				}
				System.out.println("Number of awaiting clients: " + awaitingClientCounter);
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				LOGGER.error(e);
			}
		}
	}

	public void shutdown() {
		running = false;
	}
}