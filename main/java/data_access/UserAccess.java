package data_access;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.*;

/**
 * Created by Kevin on 2/15/2018.
 */


/**
 * Generates users and authentication tokens, as well as verifies user permissions
 */
public class UserAccess {

    /**
     * Stores a UserModel in the database
     * @param user UserModel object containing all information required to store a UserModel
     * @return Boolean indicating whether or not storing was successful
     */
    public boolean storeUser(UserModel user) {

        String sql = "INSERT INTO users " +
                "(user_id, " +
                "person_id, " +
                "password, " +
                "email, " +
                "first_name, " +
                "last_name, " +
                "gender, " +
                "username) " +
                "VALUES " +
                "('" + user.getUserId() +
                "', '" + user.getPersonId() +
                "', '" + user.getPassword() +
                "', '" + user.getEmail() +
                "', '" + user.getFirstName() +
                "', '" + user.getLastName() +
                "', '" + user.getGender() +
                "', '" + user.getUsername() +
                "');";
        database db = new database();
        db.openConnect();
        db.executeSql(sql);
        db.close();
        return true;
    }


    /**
     * Retrieves a user from the database
     *
     * @param username ID for the user of which information is being accessed
     * @return JSON object containing all information about the requested user
     */
    public String getUser(String username) {
        database db = new database();
        db.openConnect();
        String userIdSql = "SELECT user_id FROM users WHERE username = '" + username + "';";
        ResultSet results = db.getSqlResults(userIdSql);
        try {
            if (!results.isBeforeFirst()) {
                db.close();
                return null;
            }
            String userId = results.getString("user_id");

            db.close();
            return userId;

        }catch (SQLException e) {
            System.out.println("An error occured.");
        }

        db.close();
        return null;
    }

    /**
     * Retrieves a user from the database
     * @param user_id ID for the user of which information is being accessed
     * @return JSON object containing all information about the requested user
     */
    public String getUserName(String user_id) {
        database db = new database();
        db.openConnect();
        String userIdSql = "SELECT username FROM users WHERE user_id = '" + user_id + "';";
        ResultSet results = db.getSqlResults(userIdSql);
        try {
            if (!results.isBeforeFirst()) {
                db.close();
                return null;
            }
            String userId = results.getString("username");

            db.close();
            return userId;

        }catch (SQLException e) {
            System.out.println("An error occured.");
        }

        db.close();
        return null;
    }

    /**
     * Verifies the credentials of a user attempting to log in to the server.
     * @param username  Username credential
     * @param password  Password credential
     * @return          Returns a boolean indicating whether or not the credentials match and can login a user.
     */
    public boolean checkCredentials(String username, String password) {
        database db = new database();
        db.openConnect();
        String userIdSql = "SELECT username FROM users WHERE username = '" + username + "' AND password = '" + password + "';";
        ResultSet results = db.getSqlResults(userIdSql);
        try {
            if (results.isBeforeFirst() == false) {
                db.close();
                return false;
            }
            else {
                db.close();
                return true;
            }
        }catch (SQLException e) {
            System.out.println("An error occured.");
        }

        db.close();
        return false;
    }

    /**
     * Checks to see if a username has already been registered or not.
     * @param username  Username attempting to be registered
     * @return          Boolean indicating whether or not the username is in use.
     */
    public boolean checkIfUsed(String username) {
        database db = new database();
        db.openConnect();
        String userIdSql = "SELECT user_id FROM users WHERE username = '" + username + "';";
        ResultSet results = db.getSqlResults(userIdSql);
        try {
            if (!results.isBeforeFirst()) {
                db.close();
                return false;
            }
            else {
                db.close();
                return true;
            }
        }catch (SQLException e) {
            System.out.println("An error occured.");
        }

        db.close();
        return false;
    }
}