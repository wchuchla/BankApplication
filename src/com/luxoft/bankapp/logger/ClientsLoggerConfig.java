package com.luxoft.bankapp.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ClientsLoggerConfig {

    public ClientsLoggerConfig() {
        try {
            Logger logger = Logger.getLogger("LogClients");
            logger.addHandler(new FileHandler("log/clients.log", true));
            logger = Logger.getLogger("LogExceptions");
            logger.addHandler(new FileHandler("log/exceptions.log", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}