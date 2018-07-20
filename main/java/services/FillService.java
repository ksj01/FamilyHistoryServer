package services;

import java.util.HashMap;

import data_access.EventAccess;
import data_access.PersonAccess;
import data_access.UserAccess;
import models.UserModel;

/**
 * Created by kevin on 3/11/18.
 */

public class FillService {

    /**
     * Gets the path parameters from the HTTP request
     * @param pathInfo  String containing the HTTP URI
     * @return          String[] containgin the split path parameters
     */
    public String[] getPathParts(String pathInfo) {
        String[] pathParts = pathInfo.split("/");
        return pathParts;
    }

    /**
     * Check the path parameters to make sure they are valid
     * @param pathParts String[] containing all of the path parameters
     * @return          String based on whether or not the parameters are valid
     */
    public String checkPathParameters(String[] pathParts) {
        if (pathParts.length < 3) {
            String error = "{ \"message\": \"Invalid path parameters\" }";
            return error;
        }
        return "continue";
    }

    /**
     * Verifies that passed Generations parameter is valid
     * @param pathParts String[] containing all of the path paramters
     * @return          String based on whether or not the parameter is valid
     */
    public String checkGenerationsParameter(String[] pathParts) {
        if (pathParts.length > 3) {
            try {
                Integer.parseInt(pathParts[3]);
                return "generations";
            } catch (NumberFormatException e) {
                String error = "{ \"message\": \"Invalid generations parameter.\" }";
                return error;
            }
        }
        return "continue";
    }

    /**
     * Verifies that the requested username is a valid user in the database
     * @param username
     * @return
     */
    public String checkUsername(String username) {
        UserAccess check = new UserAccess();
        String checkResults = check.getUser(username);
        if (checkResults == null) {
            String error = "{ \"message\": \"Invalid username.\" }";
            return error;
        }
        else {
            return "continue";
        }
    }

    /**
     * Fills out the generation tree for the requested user
     * @param username  User to be filled
     * @param defaultGenerations    Number of generations to create
     * @return                      Returns a string indicating the number of persons and events created.
     */
    public String fill(String username, int defaultGenerations) {
        ClearService clear = new ClearService();
        HashMap<String, String> user = clear.clear(username);
        String gender = user.get("gender");
        UserModel thisUser = new UserModel();
        thisUser.fillExistingUser(username, username, gender.charAt(0), defaultGenerations);
        int personsAdded = new PersonAccess().getRowCount(username);
        int eventsAdded = new EventAccess().getRowCount(username);
        String response = "{\n\"message\":\"Successfully added " + personsAdded + " persons and " + eventsAdded + " events to the database.\"\n}";
        return response;
    }
}
