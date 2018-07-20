package contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import services.*;

/**
 * Created by kevin on 3/4/18.
 */

/**
 * ClearHandler class handles HTTP requests that want to clear the server database.
 */
public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            /**
             * Pretty much the only thing this does is calls the clear() method of ClearService and returns a valid statement.
             * Also has checks for bad HTTP server requests and the like.
             */
            ClearService clear = new ClearService();
            String result = clear.clear();
            if (result == "{ \"message\": \"Clear Succeeded\" }") {
                SharedServices shared = new SharedServices();
                shared.sendResponse(exchange, result);
                success = true;
            }
            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // Since the client request was invalid, they will not receive the
                // list of games, so we close the response body output stream,
                // indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }
}
