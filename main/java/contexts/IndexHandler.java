package contexts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Created by kevin on 3/4/18.
 */

public class IndexHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        try {

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            String pathInfo = exchange.getRequestURI().getPath();
            File result = getRequestedFile(exchange, pathInfo);
            sendResponse(exchange, result);

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

    private void sendResponse(HttpExchange exchange, File result) throws IOException {
        OutputStream respBody = exchange.getResponseBody();
        FileInputStream fs = new FileInputStream(result);
        final byte[] buffer = new byte[0x10000];
        int count = 0;
        while ((count = fs.read(buffer)) >= 0) {
            respBody.write(buffer, 0, count);
        }
        fs.close();
        respBody.close();
    }

    /**
     * Determines which file should be sent based on the HTTP request
     * @param exchange   HTTP exchange
     * @param pathInfo  Requested path in the URI
     * @return          Returns the file requested in the URI, or 404 if none found
     * @throws IOException
     */
    private File getRequestedFile(HttpExchange exchange, String pathInfo) throws IOException {
        String mainPath = "lib/src/main/web/";
        try {
            File result;
            if (pathInfo.equals("/") || pathInfo.equals("/index.html")) {
                result = new File(mainPath + "index.html").getCanonicalFile();
            } else if (pathInfo.equals("/css/main.css")) {
                result = new File(mainPath + "css/main.css").getCanonicalFile();
            } else if (pathInfo.equals("/favicon.jpg")) {
                result = new File(mainPath + "favicon.jpg").getCanonicalFile();
            } else if (pathInfo.equals("/favicon.ico")) {
                result = new File(mainPath + "favicon.ico").getCanonicalFile();
            } else {
                result = new File(mainPath + "HTML/404.html").getCanonicalFile();
            }
            return result;
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        return null;
    }
}
