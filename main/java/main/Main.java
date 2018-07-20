package main;
import server.*;
import data_access.*;

/**
 * Created by kevin on 3/4/18.
 */

public class Main {
    public static void main(String [] args){
        String portNumber = args[0];
        new Server().run(portNumber);
        database db = new database();
        db.openConnect();
        db.createTables();
    }
}
