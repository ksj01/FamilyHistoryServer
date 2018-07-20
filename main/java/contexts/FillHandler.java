package contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

import services.*;

/**
 * Created by kevin on 3/4/18.
 */

public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        try {
            SharedServices shared = new SharedServices();
            String response = processRequest(exchange);
            shared.sendResponse(exchange, response);
            success = true;

            if (!success) {

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        } catch (IOException e) {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * Processes the fill request and returns a response to the client
     * @param exchange  HTTP exchange
     * @return          Response to be sent back to the client
     */
    private String processRequest(HttpExchange exchange) {
        String pathInfo = exchange.getRequestURI().getPath();
        FillService fill = new FillService();
        String[] pathParts = fill.getPathParts(pathInfo);
        String response = fill.checkPathParameters(pathParts);
        if (!response.equals("continue")) {
            return response;
        }
        String username = pathParts[2];
        response = fill.checkUsername(username);
        if (!response.equals("continue")) {
            return response;
        }
            int Generations = 4;
            response = fill.checkGenerationsParameter(pathParts);
            if (response.equals("generations")) {
                Generations = Integer.parseInt(pathParts[3]);
            }
            else if(!response.equals("continue")) {
                return response;
            }
            response = fill.fill(username, Generations);
            return response;
    }

}
