package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class LoadServiceTest {
    @Test
    public void processJson() throws Exception {
        String correctJson = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"persons\": [\n" +
                "    {\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"father\": \"Patrick_Spencer\",\n" +
                "      \"mother\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"Patrick\",\n" +
                "      \"lastName\": \"Spencer\",\n" +
                "      \"gender\": \"m\",\n" +
                "      \"personID\":\"Patrick_Spencer\",\n" +
                "      \"spouse\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"CS240\",\n" +
                "      \"lastName\": \"JavaRocks\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Im_really_good_at_names\",\n" +
                "      \"spouse\": \"Patrick_Spencer\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"started family map\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"city\": \"Salt Lake City\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.7500,\n" +
                "      \"longitude\": -110.1167,\n" +
                "      \"year\": 2016,\n" +
                "      \"eventID\": \"Sheila_Family_Map\",\n" +
                "      \"descendant\":\"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"fixed this thing\",\n" +
                "      \"personID\": \"Patrick_Spencer\",\n" +
                "      \"city\": \"Provo\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.2338,\n" +
                "      \"longitude\": -111.6585,\n" +
                "      \"year\": 2017,\n" +
                "      \"eventID\": \"I_hate_formatting\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        String invalidJson = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"persons\": [\n" +
                "    {\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"father\": \"Patrick_Spencer\",\n" +
                "      \"mother\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"Patrick\",\n" +
                "      \"lastName\": \"Spencer\",\n" +
                "      \"gender\": \"m\",\n" +
                "      \"personID\":\"Patrick_Spencer\",\n" +
                "      \"spouse\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"CS240\",\n" +
                "      \"lastName\": \"JavaRocks\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Im_really_good_at_names\",\n" +
                "      \"spouse\": \"Patrick_Spencer\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"started family map\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"city\": \"Salt Lake City\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.7500,\n" +
                "      \"longitude\": -110.1167,\n" +
                "      \"year\": 2016,\n" +
                "      \"eventID\": \"Sheila_Family_Map\",\n" +
                "      \"descendant\":\"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"fixed this thing\",\n" +
                "      \"personID\": \"Patrick_Spencer\",\n" +
                "      \"city\": \"Provo\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.2338,\n" +
                "      \"longitude\": -111.6585,\n" +
                "      \"year\": 2017,\n" +
                "      \"eventID\": \"I_hate_formatting\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        int[] validResponse = {1, 3, 2};
        int[] invalidResponse = {-1};

        LoadService service = new LoadService();
        JsonObject parsedCorrect = service.parseJson(correctJson);
        JsonObject parsedIncorrect = service.parseJson(invalidJson);
        int[] validSizes = service.processJson(parsedCorrect);
        int[] invalidSizes = service.processJson(parsedIncorrect);

        assertArrayEquals(validSizes, validResponse);
        assertArrayEquals(invalidResponse, invalidSizes);
    }

    @Test(expected = JsonSyntaxException.class)
    public void parseJson() throws Exception {
        String validJson = "    {\n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }";

        String invalidJson = "\n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }";

        LoadService service = new LoadService();
        JsonObject validResults = service.parseJson(validJson);
        JsonObject invalidResults = service.parseJson(invalidJson);
        JsonObject expected = new JsonObject();
        expected.addProperty("userName", "sheila");
        expected.addProperty("password", "parker");
        expected.addProperty("email", "sheila@parker.com");
        expected.addProperty("firstName", "Sheila");
        expected.addProperty("lastName", "Parker");
        expected.addProperty("gender", "f");
        expected.addProperty("personID", "Sheila_Parker");
        assertEquals(validResults, expected);
        assertEquals(invalidResults, null);
    }

    @Test
    public void verifyJson() throws Exception {
        String correctJson = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"persons\": [\n" +
                "    {\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"father\": \"Patrick_Spencer\",\n" +
                "      \"mother\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"Patrick\",\n" +
                "      \"lastName\": \"Spencer\",\n" +
                "      \"gender\": \"m\",\n" +
                "      \"personID\":\"Patrick_Spencer\",\n" +
                "      \"spouse\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"CS240\",\n" +
                "      \"lastName\": \"JavaRocks\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Im_really_good_at_names\",\n" +
                "      \"spouse\": \"Patrick_Spencer\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"started family map\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"city\": \"Salt Lake City\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.7500,\n" +
                "      \"longitude\": -110.1167,\n" +
                "      \"year\": 2016,\n" +
                "      \"eventID\": \"Sheila_Family_Map\",\n" +
                "      \"descendant\":\"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"fixed this thing\",\n" +
                "      \"personID\": \"Patrick_Spencer\",\n" +
                "      \"city\": \"Provo\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.2338,\n" +
                "      \"longitude\": -111.6585,\n" +
                "      \"year\": 2017,\n" +
                "      \"eventID\": \"I_hate_formatting\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        String invalidJson = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"persons\": [\n" +
                "    {\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"father\": \"Patrick_Spencer\",\n" +
                "      \"mother\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"Patrick\",\n" +
                "      \"lastName\": \"Spencer\",\n" +
                "      \"gender\": \"m\",\n" +
                "      \"personID\":\"Patrick_Spencer\",\n" +
                "      \"spouse\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"CS240\",\n" +
                "      \"lastName\": \"JavaRocks\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Im_really_good_at_names\",\n" +
                "      \"spouse\": \"Patrick_Spencer\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"started family map\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"city\": \"Salt Lake City\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.7500,\n" +
                "      \"longitude\": -110.1167,\n" +
                "      \"year\": 2016,\n" +
                "      \"eventID\": \"Sheila_Family_Map\",\n" +
                "      \"descendant\":\"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"fixed this thing\",\n" +
                "      \"personID\": \"Patrick_Spencer\",\n" +
                "      \"city\": \"Provo\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.2338,\n" +
                "      \"longitude\": -111.6585,\n" +
                "      \"year\": 2017,\n" +
                "      \"eventID\": \"I_hate_formatting\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";

        LoadService service = new LoadService();
        String validResult = service.verifyJson(correctJson);
        String expected = "{\n\"message\":\"Successfully added 1 users, 3 persons, and 2 events to the database.\"\n}";

        String invalidResult = service.verifyJson(invalidJson);
        String unexpected = "{\n\"message\":\"One or more of your objects are missing required members.\"\n}";

        assertEquals(validResult, expected);
        assertEquals(unexpected, invalidResult);

    }

}