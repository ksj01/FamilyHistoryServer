package services;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by kevin on 3/11/18.
 */

public class SharedServices {

    /**
     * Used for writing a response to be sent back to the client via HTTP
     * @param str
     * @param os
     * @throws IOException
     */
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    /**
     * Opens an HTTP connection and sends the response back to the user
     * @param exchange
     * @param response
     * @throws IOException
     */
    public void sendResponse(HttpExchange exchange, String response) throws IOException {
        try {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream respBody = exchange.getResponseBody();
            writeString(response, respBody);
            respBody.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

    /**
     * Reads in data from the client
     * @param is
     * @return
     * @throws IOException
     */
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
