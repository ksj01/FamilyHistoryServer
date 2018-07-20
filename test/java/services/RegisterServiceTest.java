package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class RegisterServiceTest {
    @Before
    public void prepare() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void buildUser() throws Exception {
        String json = "{\"userName\":\"username\",\"password\":\"password\",\"email\":\"email\",\"firstName\":\"firstname\",\"lastName\":\"lastname\",\"gender\":\"m\"}";
        RegisterService register = new RegisterService();
        JsonObject user = register.processJson(json);
        String response = register.buildUser(user);
        RegisterService service = new RegisterService();
        JsonObject responseObject = service.processJson(response);
        String authToken = responseObject.get("authToken").toString();
        authToken = authToken.substring(1, authToken.length() - 1);

        String expected = "{\n\"authToken\":\"" + authToken + "\",\n" +
                "\"userName\":\"username\",\n" +
                "\"personId\":\"username\"\n" +
                "}";

        assertEquals(expected, response);

        String repeatResponse = register.buildUser(user);

        String repeat = "{ \"message\": \"Username not available\" }";

        assertEquals(repeatResponse, repeat);

    }

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

}