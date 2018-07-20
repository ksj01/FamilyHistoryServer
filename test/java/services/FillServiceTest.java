package services;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import data_access.UserAccess;
import models.UserModel;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class FillServiceTest {
    @Before
    public void prepare() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void getPathParts() throws Exception {
        String pathPartsShort = "/fill/user";
        String pathPartsLong = "/fill/user/7";
        FillService service = new FillService();
        String[] resultShort = service.getPathParts(pathPartsShort);
        String[] resultLong = service.getPathParts(pathPartsLong);
        String[] expectedShort = {"", "fill", "user"};
        String[] expectedLong = {"", "fill", "user", "7"};

        assertArrayEquals(resultLong, expectedLong);
        assertArrayEquals(resultShort, expectedShort);
        assertNotEquals(resultLong, expectedLong);
        assertNotEquals(resultShort, expectedShort);
    }

    @Test
    public void checkPathParameters() throws Exception {
        String[] wrongArray = {"", "user"};
        FillService service = new FillService();
        String wrongResult = service.checkPathParameters(wrongArray);
        String wrongExpected = "{ \"message\": \"Invalid path parameters\" }";
        assertEquals(wrongExpected, wrongResult);
        String[] correctArray = {"", "user", "user"};
        String correctResult = service.checkPathParameters(correctArray);
        String correctExpected = "continue";
        assertEquals(correctExpected, correctResult);
    }

    @Test
    public void checkGenerationsParameter() throws Exception {
        String[] wrongArray = {"", "fill", "user", "gens"};
        FillService service = new FillService();
        String wrongResult = service.checkGenerationsParameter(wrongArray);
        String wrongExpected = "{ \"message\": \"Invalid generations parameter.\" }";
        assertEquals(wrongExpected, wrongResult);
        String[] correctArray = {"", "fill", "user", "3"};
        String correctResult = service.checkGenerationsParameter(correctArray);
        String correctExpected = "generations";
        assertEquals(correctExpected, correctResult);
        String[] shortArray = {"", "fill", "user"};
        String shortResults = service.checkGenerationsParameter(shortArray);
        String expectedShort = "continue";
        assertEquals(shortResults, expectedShort);
    }

    @Test
    public void checkUsername() throws Exception {
        UserModel user = new UserModel();
        user.setUserId("this");
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("first");
        user.setLastName("last");
        user.setGender('m');
        user.setUsername("this");

        UserAccess access = new UserAccess();
        access.storeUser(user);

        FillService service = new FillService();
        String correctResult = service.checkUsername("this");
        String wrongResult = service.checkUsername("that");
        String correctExpectation = "continue";
        String wrongExpectation = "{ \"message\": \"Invalid username.\" }";

        assertEquals(correctExpectation, correctResult);
        assertEquals(wrongExpectation, wrongResult);
    }

    @Before
    public void registerUser() {
        String json = "{\"userName\":\"username\",\"password\":\"password\",\"email\":\"email\",\"firstName\":\"firstname\",\"lastName\":\"lastname\",\"gender\":\"m\"}";

        RegisterService register = new RegisterService();
        JsonObject user = register.processJson(json);
        register.buildUser(user);
    }

    @Test
    public void fill() throws Exception {
        FillService service = new FillService();
        String response = service.fill("username", 0);
        String expectedNoFamily = "{\n" +
                "\"message\":\"Successfully added 0 persons and 0 events to the database.\"\n" +
                "}";
        assertEquals(expectedNoFamily, response);

        String bigResponse = service.fill("username", 4);
        String expectedBigFamily = "{\n" +
                "\"message\":\"Successfully added 32 persons and 125 events to the database.\"\n" +
                "}";

        assertEquals(expectedBigFamily, bigResponse);
    }

}