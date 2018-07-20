package services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import data_access.UserAccess;
import data_access.authAccess;

/**
 * Created by kevin on 3/11/18.
 */

public class LoginService {

    /**
     * Converts a Json String into a jsonObject
     * @param json  String containing data to be converted into a JsonObject
     * @return      JsonObject containing all of the passed data
     * @throws JsonSyntaxException  Thrown if the passed String is not valid Json
     */
    public JsonObject processJson(String json) throws JsonSyntaxException {
        try {
            JsonParser parser = new JsonParser();
            JsonElement requestElement = parser.parse(json);
            JsonObject requestObject = requestElement.getAsJsonObject();
            return requestObject;
        } catch(JsonSyntaxException e) {
            throw new JsonSyntaxException("{ \"message\": \"Invalid JSON Request\" }");
        }
    }

    /**
     * Generates an authToken and returns a Json String indicating that the login was successful
     * @param username  Username to be logged in
     * @param userId    UserId that the authToken should be associated with
     * @return
     */
    public String buildAuthResponse(String username, String userId) {
        authAccess gen = new authAccess();
        String authToken = gen.generateAuth(userId);
        String response = "{\n\"authToken\":\"" + authToken + "\",\n" +
                "\"userName\":\"" + username + "\",\n" +
                "\"personId\":\"" + userId + "\"\n" +
                "}";
        return response;
    }

    /**
     * Verifies that the username and password should log in a user
     * @param requestObject JsonObject containing the username and password
     * @return              Returns a response based on whether or not the user was successfully logged in.
     */
    public String verifyCredentials(JsonObject requestObject) {
        String username = requestObject.get("userName").toString();
        username = username.substring(1, username.length() - 1);
        String password = requestObject.get("password").toString();
        password = password.substring(1, password.length() - 1);
        UserAccess access = new UserAccess();
        boolean test = access.checkCredentials(username, password);
        if (test == false) {
            String error = "{ \"message\": \"Invalid login.\" }";
            return error;
        }
        String userId = access.getUser(username);
        if (userId == null) {
            String error = "{ \"message\": \"Cannot find requested User. Login failed.\" }";
            return error;
        }
        String response = buildAuthResponse(username, userId);
        return response;
    }
}
