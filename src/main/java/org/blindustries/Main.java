package org.blindustries;

import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        int port = args.length > 0 ? Integer.parseInt(args[0]): DEFAULT_PORT;

        try {
            ConfigServer server = new ConfigServer(port);
            server.start();


            Runtime.getRuntime().addShutdownHook( new Thread(server::stop));

            ConfigLogger.getInstance().LOGGER.info("Configuration server is running on port " + port);
        } catch (Exception e){
            ConfigLogger.getInstance().LOGGER.log(Level.SEVERE,"Failed to start the configuration server", e);
        }

    }
}