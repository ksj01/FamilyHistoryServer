package contexts;

import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import java.io.*;
import data_access.*;
import services.*;


/**
 * Created by kevin on 3/4/18.
 */

/**
 * Handles all HTTP requests for Event information. This includes checking authorization when checking for events
 */
public class EventHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        try {
            //The first step is to process the request from the client.
            SharedServices shared = new SharedServices();
            //The bulk of the work is in the processRequest() function
            String response = processRequest(exchange);
            //Response from the processRequest() function is sent to the client.
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
     * Handles the processing of request headers and body, and returns an appropriate response.
     * @param exchange  HTTP Exchange
     * @return  String in Json Format with the response.
     */
    private String processRequest(HttpExchange exchange) {
        //Scrape the URI path to know if we should get all events or just a specific event.
        Headers reqHeaders = exchange.getRequestHeaders();
        String pathInfo = exchange.getRequestURI().getPath();
        EventService eventService = new EventService();

        //Splits the path parameters into individual params.
        String[] pathParts = eventService.getPathParts(pathInfo);
        String response;

        //Checks to make sure the parameters are correct, and returns an error response if not.
        response = eventService.checkPathParameters(pathParts);
        if (!response.equals("continue")) {
            return response;
        }

        //Checks to make sure the authToken is in the request header. If not, exits with an error response.
        response = eventService.checkForAuthToken(reqHeaders);
        if (!response.equals("continue")) {
            return response;
        }

        //Verifies that the passed authToken in the header actually grants authorization to the requested event.
        String authToken = eventService.getAuthToken(reqHeaders);
        response = eventService.verifyAuthorization(authToken);
        if (!response.equals("continue")) {
            return response;
        }

        //Gets the necessary userId and username so we can request the right events.
        String userId = eventService.getUserId(authToken);
        String username = new UserAccess().getUserName(userId);

        //If we are looking for a specific event, get the eventId from the pathParts array. Otherwise, it remains null.
        String eventId = null;
        if (pathParts.length > 2) {
            eventId = pathParts[2];
        }

        //If no eventId, we should get all events for the user.
        if (eventId == null) {
            response = eventService.getAllEvents(username);
            return response;
        }

        //If there is an eventId, we'll only grab the one event requested.
        else {
            response = eventService.getOneEvent(username, eventId);
            return response;
        }
    }

}
