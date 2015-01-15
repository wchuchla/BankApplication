package com.luxoft.bankapp.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by wchuchla on 2015-01-15.
 */
public class ExceptionsLoggerConfig {

    public ExceptionsLoggerConfig() {
        try {
            Logger logger = Logger.getLogger("LogExceptions");
            logger.addHandler(new FileHandler("log/exceptions.log", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
