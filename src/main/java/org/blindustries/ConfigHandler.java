package org.blindustries;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ConfigHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        // Handle when path returned is invalid
        if (pathParts.length < 3){
            sendResponse(exchange, 400, "Invalid request. Use /config/{appName}");
            return;
        }

        String appName = pathParts[2];
        Map<String, String> config = ConfigServer.getAppConfigs().get(appName);

        if (config == null){
            sendResponse(exchange, 404, "Configuration not found for application: " + appName);
            return;
        }

        StringBuilder jsonBuilder = new StringBuilder("{\n");
        config.forEach((key, value) ->
                jsonBuilder.append("  \"").append(key).append("\": \"")
                        .append(value).append("\",\n"));
        String json = jsonBuilder.substring(0, jsonBuilder.length() - 2) + "\n}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, 200, json);;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode,responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()){
            os.write(responseBytes);
        }
    }
}
