package org.blindustries;

public class Main {
    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        int port = args.length > 0 ? Integer.parseInt(args[0]): DEFAULT_PORT;

        try {
            ConfigServer server = new ConfigServer(port);
        } catch (Exception e){

        }

    }
}