package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.junit.Test;

import static org.junit.Assert.*;
import data_access.*;
import models.UserModel;

/**
 * Created by kevin on 3/11/18.
 */
public class LoginServiceTest {
    @Test(expected = JsonSyntaxException.class)
    public void processJson() throws Exception {
        String validJson = "{\n\t\"userName\":\"username\"\n}";
        String invalidJson = "userName\":\"username\"\n}";

        LoginService service = new LoginService();
        JsonObject validResult = service.processJson(validJson);
        JsonObject expected = new JsonObject();
        expected.addProperty("userName", "username");
        JsonObject invalidResult = service.processJson(invalidJson);

        assertEquals(validResult, expected);
        assertEquals(invalidResult, null);
    }

    @Test
    public void verifyCredentials() throws Exception {
        UserModel user = new UserModel();
        user.setUserId("this");
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("first");
        user.setLastName("last");
        user.setGender('m');
        user.setUsername("username");

        UserAccess access = new UserAccess();
        access.storeUser(user);

        LoginService service = new LoginService();
        String credential = "{\n" +
                "\t\"userName\":\"username\",\n" +
                "\t\"password\":\"password\"\n" +
                "}";

        JsonObject credentialObject = service.processJson(credential);
        String response = service.verifyCredentials(credentialObject);
        JsonObject responseObject = service.processJson(response);
        String authToken = responseObject.get("authToken").toString();
        authToken = authToken.substring(1, authToken.length() - 1);
        String expectedResponse = "{\n" +
                "\"authToken\":\"" + authToken + "\",\n" +
                "\"userName\":\"username\",\n" +
                "\"personId\":\"this\"\n" +
                "}";

        String falseCredential = "{\n" +
                "\t\"userName\":\"username\",\n" +
                "\t\"password\":\"something\"\n" +
                "}";

        JsonObject falseObject = service.processJson(falseCredential);
        String invalidResponse = service.verifyCredentials(falseObject);
        String expectedInvalid = "{ \"message\": \"Invalid login.\" }";

        assertEquals(expectedResponse, response);
        assertEquals(invalidResponse, expectedInvalid);
    }

}