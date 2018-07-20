package contexts;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.HttpURLConnection;
import data_access.*;
import services.PersonService;
import services.SharedServices;

/**
 * Created by kevin on 3/4/18.
 */

public class PersonHandler implements HttpHandler {

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
        Headers reqHeaders = exchange.getRequestHeaders();
        String pathInfo = exchange.getRequestURI().getPath();
        PersonService personService = new PersonService();
        String[] pathParts = personService.getPathParts(pathInfo);
        String response;
        response = personService.checkPathParameters(pathParts);
        if (!response.equals("continue")) {
            return response;
        }

        response = personService.checkForAuthToken(reqHeaders);
        if (!response.equals("continue")) {
            return response;
        }

        String authToken = personService.getAuthToken(reqHeaders);
        response = personService.verifyAuthorization(authToken);
        if (!response.equals("continue")) {
            return response;
        }


        String userId = personService.getUserId(authToken);
        String username = new UserAccess().getUserName(userId);

        String personId = null;
        if (pathParts.length > 2) {
            personId = pathParts[2];
        }

        if (personId == null) {
            response = personService.getAllPersons(username);
            return response;
        }
        else {
            response = personService.getOnePerson(username, personId);
            return response;
        }
    }
}
