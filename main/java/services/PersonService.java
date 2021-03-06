package services;

import com.sun.net.httpserver.Headers;
import data_access.*;

/**
 * Created by kevin on 3/11/18.
 */

public class PersonService {

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
        if (pathParts.length < 2) {
            String error = "{ \"message\": \"Invalid path parameters\" }";
            return error;
        }
        return "continue";
    }

    /**
     * Verifies that an authToken exists in the headers
     * @param reqHeaders    HTTP headers
     * @return              String based on whether or not the parameters are valid
     */
    public String checkForAuthToken(Headers reqHeaders) {
        if (!reqHeaders.containsKey("Authorization")) {
            String error = "{ \"message\": \"Missing Auth Token\" }";
            return error;
        }
        return "continue";
    }

    /**
     * Retrieves the authToken from the headers
     * @param reqHeaders    HTTP headers
     * @return              Returns the authToken
     */
    public String getAuthToken(Headers reqHeaders) {
       String authToken =  reqHeaders.getFirst("Authorization");
       return authToken;
    }

    /**
     * Verifies that the authToken sent in the headers grants the user access to the database
     * @param authToken authToken to be verified
     * @return          String based on whether or not the parameters are valid
     */
    public String verifyAuthorization(String authToken) {
        authAccess auth = new authAccess();
        String userId = auth.checkAuth(authToken);
        if (userId == null) {
            String error = "{ \"message\": \"Invalid authorization\" }";
            return error;
        }
        return "continue";
    }

    /**
     * Retrieves the userId associated with a given authToken
     * @param authToken Authtoken for which we want the associated user
     * @return          UserId associated with the authToken
     */
    public String getUserId(String authToken) {
        authAccess auth = new authAccess();
        String userId = auth.checkAuth(authToken);
        return userId;
    }

    /**
     * Retrieves all person associated with a given user
     * @param username  User for whom we want all persons
     * @return          Json string containing all persons associated with a user
     */
    public String getAllPersons(String username) {
        PersonAccess personGrabber = new PersonAccess();
        String person = personGrabber.getAllPersons(username);
        return person;
    }

    /**
     * Retrieves a specific person
     * @param username  Username associated with the requested person
     * @param personId   EventId for the person we want
     * @return          Json string containing the requested person, or an error message
     */
    public String getOnePerson(String username, String personId) {
        PersonAccess personGrabber = new PersonAccess();
        String person = personGrabber.getPerson(personId, username);
        if (person.equals("no such PersonModel")) {
            String error = "{ \"message\": \"Invalid personID\" }";
            return error;
        }
        else if (person.equals("does not belong")) {
            String error = "{ \"message\": \"The requested PersonModel does not belong to you.\" }";
            return error;
        }
        else {
            return person;
        }
    }
}
