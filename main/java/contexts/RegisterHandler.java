package contexts;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

import models.*;
import data_access.*;
import services.EventService;
import services.RegisterService;
import services.SharedServices;

/**
 * Created by kevin on 3/4/18.
 */

public class RegisterHandler implements HttpHandler{

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
        }
        catch (IOException e) {

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
        try {
            SharedServices shared = new SharedServices();
            InputStream requestBody = exchange.getRequestBody();
            String json = shared.readString(requestBody);
            JsonObject requestObject;
            try {
                RegisterService parser = new RegisterService();
                requestObject = parser.processJson(json);
            } catch (JsonSyntaxException e) {
                String error = "{ \"message\": \"Invalid JSON Request\" }";
                return error;
            }
            RegisterService registration = new RegisterService();
            String response = registration.buildUser(requestObject);
            return response;

        }catch(IOException e){
            String error = "{ \"message\": \"An IO Exception Occurred\" }";
            return error;
        }
    }
}
