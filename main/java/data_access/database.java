package data_access;
import java.sql.*;

/**
 * Created by kevin on 3/4/18.
 */

/**
 * Sets up the database so it is adequate for our purposes
 */
public class database {

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (java.lang.ClassNotFoundException e) {
            System.out.println("Could not load database driver");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Connection conn;

    /**
     * Opens a database to use
     */
    public void openConnect() {

        final String CONNECTION_URL = "jdbc:sqlite:/home/kevin/Documents/server.sqlite";

        try {
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch(java.sql.SQLException e) {
            System.out.println("openConnect has failed.");
        }
    }
    Statement stmt = null;

    /**
     * Creates the tables we need for our server to work properly.
     */
    public void createTables() {
        String auth_tokens = "CREATE TABLE IF NOT EXISTS `auth_tokens` (\n" +
                "  `value` varchar(50) NOT NULL,\n" +
                "  `user_id` varchar(50) NOT NULL\n" +
                ");";
        String events = "CREATE TABLE IF NOT EXISTS `events` (\n" +
                "  `event_id` varchar(50) NOT NULL,\n" +
                "  `descendant` varchar(50) NOT NULL,\n" +
                "  `person` varchar(50) NOT NULL,\n" +
                "  `latitude` varchar(50) NOT NULL,\n" +
                "  `longitude` varchar(50) NOT NULL,\n" +
                "  `country` varchar(50) NOT NULL,\n" +
                "  `city` varchar(50) NOT NULL,\n" +
                "  `event_type` varchar(50) NOT NULL,\n" +
                "  `year` int(4) NOT NULL\n" +
                ");";
        String persons = "CREATE TABLE IF NOT EXISTS `persons` (\n" +
                "  `person_id` varchar(50) NOT NULL,\n" +
                "  `descendant` varchar(50) NOT NULL,\n" +
                "  `first_name` varchar(50) NOT NULL,\n" +
                "  `last_name` varchar(50) NOT NULL,\n" +
                "  `gender` char(1) NOT NULL,\n" +
                "  `father` varchar(50) DEFAULT NULL,\n" +
                "  `mother` varchar(50) DEFAULT NULL,\n" +
                "  `spouse` varchar(50) DEFAULT NULL\n" +
                ");";
        String users = "CREATE TABLE IF NOT EXISTS `users` (\n" +
                "  `user_id` varchar(50) NOT NULL,\n" +
                "  `person_id` varchar(50) NOT NULL,\n" +
                "  `password` varchar(50) NOT NULL,\n" +
                "  `email` varchar(50) NOT NULL,\n" +
                "  `first_name` varchar(50) NOT NULL,\n" +
                "  `last_name` varchar(50) NOT NULL,\n" +
                "  `gender` char(1) NOT NULL,\n" +
                "  `username` varchar(50) NOT NULL\n" +
                ");";
        try {
            stmt = conn.createStatement();
            stmt.execute(auth_tokens);
            stmt.execute(events);
            stmt.execute(persons);
            stmt.execute(users);
            conn.commit();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("createTables has failed.");
        }
    }

    /**
     * Executes whatever sql we send to it.
     * @param sql   Sql to be executed
     * @return      Boolean indication whether or not the statement was executed
     */
    public Boolean executeSql(String sql) {
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
            conn.commit();
            stmt.close();
            return true;
        }
        catch(SQLException e) {
            return false;
        }
    }

    /**
     * Executes sql and returns requested values.
     * @param sql   Sql to be executed
     * @return      Returns a ResultSet containing all requested data.
     */
    public ResultSet getSqlResults(String sql) {
        try {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql);
            conn.commit();
            return results;
        }
        catch(SQLException e) {
            return null;
        }
    }

    /**
     * Closes the current database connection
     * @return  boolean indicating that the database was closed or not.
     */
    public Boolean close() {
        try {
            conn.close();
            return true;
        }
        catch(SQLException e) {
            return false;
        }
    }
}
