package com.luxoft.bankapp.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ConfigLoggers {
	public ConfigLoggers() {
		try {
			Logger logger = Logger.getLogger("LogExceptions");
			logger.addHandler(new FileHandler("exceptions.log"));
			logger = Logger.getLogger("LogClients");
			logger.addHandler(new FileHandler("clients.log"));
			logger = Logger.getLogger("LogDB");
			logger.addHandler(new FileHandler("db.log"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}