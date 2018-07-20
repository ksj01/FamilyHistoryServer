package server;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import contexts.*;


public class Server{
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    public void run(String portNumber) {
        System.out.println("Initializing HTTP Server");

        try {
            // Create a new HttpServer object.
            // Rather than calling "new" directly, we instead create
            // the object by calling the HttpServer.create static factory method.
            // Just like "new", this method returns a reference to the new object.
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        System.out.println("Creating contexts");
        server.createContext("/", new IndexHandler());
        server.createContext("/index.html", new IndexHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());

        server.createContext("/user/register", new RegisterHandler());
        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }

}
