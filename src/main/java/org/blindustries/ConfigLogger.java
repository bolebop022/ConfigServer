package org.blindustries;

import java.util.logging.Logger;

public class ConfigLogger {
    private static ConfigLogger configLogger;
    public Logger LOGGER;

    private ConfigLogger(){
         LOGGER = Logger.getLogger(ConfigServer.class.getName());
    }

    public static synchronized ConfigLogger getInstance(){
        if (configLogger== null){
            configLogger = new ConfigLogger();
        }
        return configLogger;
    }
}
