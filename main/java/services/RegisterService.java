package services;

import com.google.gson.*;
import data_access.*;
import models.UserModel;

/**
 * Created by kevin on 3/11/18.
 */


public class RegisterService {

    /**
     * Builds a new user based on the supplied information
     * @param requestObject JsonObject containing all information required to make a new user
     * @return              Json response indicating whether or not the user was successfully created
     */
    public String buildUser(JsonObject requestObject) {
        String username = requestObject.get("userName").toString();
        username = username.substring(1, username.length() - 1);
        UserAccess userDb = new UserAccess();
        boolean results = userDb.checkIfUsed(username);
        if (results == true) {
            String error = "{ \"message\": \"Username not available\" }";
            return error;
        }
        String password = requestObject.get("password").toString();
        password = password.substring(1, password.length() - 1);
        String email = requestObject.get("email").toString();
        email = email.substring(1, email.length() - 1);
        String firstName = requestObject.get("firstName").toString();
        firstName = firstName.substring(1, firstName.length() - 1);
        String lastName = requestObject.get("lastName").toString();
        lastName = lastName.substring(1, lastName.length() - 1);
        char gender = requestObject.get("gender").toString().toCharArray()[1];
        int defaultGenerations = 4;
        UserModel thisUser = new UserModel();
        thisUser.generateUser(requestObject, username, gender, defaultGenerations);
        thisUser.setPassword(password);
        thisUser.setEmail(email);
        thisUser.setFirstName(firstName);
        thisUser.setLastName(lastName);
        UserAccess access = new UserAccess();
        access.storeUser(thisUser);
        authAccess gen = new authAccess();
        String authToken = gen.generateAuth(thisUser.getUserId());
        String response = "{\n\"authToken\":\"" + authToken + "\",\n" +
                "\"userName\":\"" + username + "\",\n" +
                "\"personId\":\"" + thisUser.getPersonId() + "\"\n" +
                "}";
        return response;

    }


    /**
     * Processes the supplied Json String to convert it in to a Json Object
     * @param json  Json String to be converted
     * @return      JsonObject containing all of the data supplied
     * @throws JsonSyntaxException  Thrown if the Json String didn't contain valid Json
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
}

