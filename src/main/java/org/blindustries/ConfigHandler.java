package org.blindustries;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode,responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()){
            os.write(responseBytes);
        }
    }
}
