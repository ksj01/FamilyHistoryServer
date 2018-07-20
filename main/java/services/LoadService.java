package services;

import com.google.gson.*;

import models.*;

/**
 * Created by kevin on 3/11/18.
 */

public class LoadService {

    /**
     * Processes the passed Json to determine how many Users, Persons, and Events are being created
     * @param json  JsonObject containing the requested information to be loaded into the database
     * @return      Int[] containing the number of Users, Persons, and Events
     */
    public int[] processJson(JsonObject json) {
        JsonArray users = json.get("users").getAsJsonArray();
        for (int i = 0; i < users.size(); i++) {
            JsonObject curUser = users.get(i).getAsJsonObject();
            if (curUser.has("userName") && curUser.has("password") && curUser.has("email") && curUser.has("firstName") && curUser.has("lastName") && curUser.has("gender") && curUser.has("personID")) {
                new UserModel().generateUserFromJson(curUser);
            }
            else {
                int[] error = {-1};
                return error;
            }
        }
        JsonArray persons = json.get("persons").getAsJsonArray();
        for (int i = 0; i < persons.size(); i++) {
            JsonObject curPerson = persons.get(i).getAsJsonObject();
            if (curPerson.has("firstName") && curPerson.has("lastName") && curPerson.has("gender") && curPerson.has("personID") && curPerson.has("descendant")) {
                new PersonModel().generatePersonFromJson(curPerson);
            }
            else {
                int[] error = {-1};
                return error;
            }
        }
        JsonArray events = json.get("events").getAsJsonArray();
        for (int i = 0; i < events.size(); i++) {
            JsonObject curEvent = events.get(i).getAsJsonObject();
            if (curEvent.has("eventType") && curEvent.has("personID") && curEvent.has("city") && curEvent.has("country") && curEvent.has("latitude") && curEvent.has("longitude") && curEvent.has("year") && curEvent.has("eventID") && curEvent.has("descendant")) {
                new EventModel().generateEventFromJson(curEvent);
            }
            else {
                int[] error = {-1};
                return error;
            }
        }
        int[] sizes = {users.size(), persons.size(), events.size()};
        return sizes;
    }

    /**
     * Parses the Json string and converts it into a JsonObject
     * @param json  Json String to be parsed
     * @return      JsonObject containing all the information in the Json String
     * @throws JsonSyntaxException  Thrown if the passed String is not valid Json
     */
    public JsonObject parseJson(String json) throws JsonSyntaxException {
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
     * Verifies that the passed Json contains all the required fields to build users, persons, and events
     * @param json  Json string containing all requested data
     * @return      Returns a string based on whether or not the Json passed was a valid request.
     */
    public String verifyJson(String json) {
        try {
            JsonObject requestObject;
            requestObject = parseJson(json);
            ClearService clear = new ClearService();
            clear.clear();
            int[] sizes = processJson(requestObject);
            if (sizes[0] == -1 || (sizes[0] + sizes[1] + sizes[2]) == 0) {
                String response = "{\n\"message\":\"One or more of your objects are missing required members.\"\n}";
                return response;
            }
            String response = "{\n\"message\":\"Successfully added " + sizes[0] + " users, " + sizes[1] + " persons, and " + sizes[2] + " events to the database.\"\n}";
            return response;

        } catch (JsonSyntaxException e) {
            String error = "{ \"message\": \"Invalid JSON Request\" }";
            return error;
        }
    }


}
