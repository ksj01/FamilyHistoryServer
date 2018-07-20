package contexts;

import com.google.gson.JsonArray;
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

import models.*;

import models.UserModel;
import services.*;

/**
 * Created by kevin on 3/4/18.
 */


public class LoadHandler implements HttpHandler {
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
            LoadService load = new LoadService();
            InputStream requestBody = exchange.getRequestBody();
            SharedServices shared = new SharedServices();
            String json = shared.readString(requestBody);
            String response = load.verifyJson(json);
            return response;
        }catch (IOException e) {
            String error = "{ \"message\": \"An IO Exception Occurred\" }";
            return error;
        }
    }


}
