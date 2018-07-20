package contexts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import data_access.authAccess;
import data_access.UserAccess;
import services.LoginService;
import services.SharedServices;

/**
 * Created by kevin on 3/4/18.
 */

public class LoginHandler implements HttpHandler{

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
        SharedServices shared = new SharedServices();
        InputStream requestBody = exchange.getRequestBody();
        JsonObject requestObject;
        LoginService loginService = new LoginService();
        try {
            String json = shared.readString(requestBody);
            try {
                requestObject = loginService.processJson(json);
            } catch (JsonSyntaxException e) {
                String error = "{ \"message\": \"Invalid Json Request\" }";
                return error;
            }
        } catch (IOException e) {
            String error = "{ \"message\": \"An IO Exception Occurred\" }";
            return error;
        }
        String response = loginService.verifyCredentials(requestObject);
        return response;
    }
}
