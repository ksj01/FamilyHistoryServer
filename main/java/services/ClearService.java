package services;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import data_access.*;
/**
 * Created by kevin on 3/11/18.
 */

public class ClearService {

    /**
     * Clears all data from the database
     * @return  Json message stating if the database was successfully cleared
     */
    public String clear() {
        database db = new database();
        db.openConnect();

        String emptyAuthTokens = "DELETE FROM `auth_tokens`;";
        String emptyEvents = "DELETE FROM `events`;";
        String emptyPersons = "DELETE FROM `persons`;";
        String emptyUsers = "DELETE FROM `users`;";

        db.executeSql(emptyAuthTokens);
        db.executeSql(emptyEvents);
        db.executeSql(emptyPersons);
        db.executeSql(emptyUsers);
        db.close();
        String clearedMessage = "{ \"message\": \"Clear Succeeded\" }";
        return clearedMessage;
    }


    /**
     * Clears the events and persons from the database associated with the specified username
     * @param username  Username for whom we want to remove events and persons
     * @return  HashMap containing the user whose data was just removed
     */
    public HashMap clear(String username) {
        database db = new database();
        db.openConnect();
        String userIdSql = "SELECT user_id, person_id, password, email, first_name, last_name, gender, username FROM users WHERE username = '" + username + "';";
        ResultSet results = db.getSqlResults(userIdSql);
        try {
            String userId = results.getString("user_id");
            HashMap<String, String> hash = new HashMap<>();
            hash.put("user_id", results.getString("user_id"));
            hash.put("person_id", results.getString("person_id"));
            hash.put("password", results.getString("password"));
            hash.put("email", results.getString("email"));
            hash.put("first_name", results.getString("first_name"));
            hash.put("last_name", results.getString("last_name"));
            hash.put("gender", results.getString("gender"));
            hash.put("username", results.getString("username"));
            String emptyEvents = "DELETE FROM `events` WHERE descendant = '" + username + "';";
            String emptyPersons = "DELETE FROM `persons` WHERE descendant = '" + username + "';";

            db.executeSql(emptyEvents);
            db.executeSql(emptyPersons);
            db.close();
            return hash;

        }catch (SQLException e) {
            System.out.println("An error occured.");
        }

        db.close();
        return null;

    }

}
