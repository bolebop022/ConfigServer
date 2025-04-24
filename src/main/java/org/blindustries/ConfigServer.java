package org.blindustries;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigServer {


    private static final String CONFIG_DIR = "config";

    private final HttpServer server;

    public static Map<String, Map<String, String>> getAppConfigs() {
        return appConfigs;
    }

    private static final Map<String, Map<String, String>> appConfigs = new HashMap<>();

    public ConfigServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/config", new ConfigHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));

        loadConfigurations();

        ConfigLogger.getInstance().LOGGER.info("Configuration server initialized on port " + port);
    }

    private void loadConfigurations(){
        try{
            File configDir = new File(CONFIG_DIR);
            if(!configDir.exists()){
                configDir.mkdir();
                createSampleConfig(configDir);
            }

            // Lists all the subdirectories that are in the config folder
            File[] appDirs = configDir.listFiles(File::isDirectory);
            if(appDirs != null){
                //Scans through config folder to find subdirectories
                for(File appDir : appDirs){
                    String appName = appDir.getName();
                    Map<String, String> appConfig = new HashMap<>();

                    // Scans through the app directory to find the property files
                    File[] configFiles = appDir.listFiles(((dir, name) -> name.endsWith(".properties")));
                    // If they exist the properties are then loaded into appConfig
                    if(configFiles != null){
                        for(File configFile: configFiles){
                            Properties props = new Properties();
                            try (FileInputStream fis = new FileInputStream(configFile)){
                                props.load(fis);
                                props.forEach((key, value) -> appConfig.put(key.toString(), value.toString()));
                            }
                        }
                    }

                    appConfigs.put(appName, appConfig);
                    ConfigLogger.getInstance().LOGGER.info("Loaded configuration for application: " + appName);

                }
            }
        } catch (IOException e){
            ConfigLogger.getInstance().LOGGER.log(Level.SEVERE, "Failed to load configuration files", e);
        }

    }
    public void start(){
        server.start();
        ConfigLogger.getInstance().LOGGER.info("Configuration server started");
    }

    public  void stop(){
        server.stop(0);
        ConfigLogger.getInstance().LOGGER.info("Configuration server stopped");
    }

    private void createSampleConfig(File configDir){

    }
}
