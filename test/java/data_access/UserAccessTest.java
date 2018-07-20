package data_access;

import org.junit.Before;
import org.junit.Test;

import models.UserModel;
import services.ClearService;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class UserAccessTest {
    @Before
    public void clear() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void storeUser() throws Exception {
        UserModel user = new UserModel();
        user.setUserId("this");
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("first");
        user.setLastName("last");
        user.setGender('m');
        user.setUsername("username");

        UserAccess access = new UserAccess();
        boolean results = access.storeUser(user);
        assertTrue(results);
    }

    @Test
    public void getUser() throws Exception {
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

        String result = access.getUser("username");
        assertEquals("this", result);
        assertNotEquals("that", result);
    }

    @Test
    public void getUserName() throws Exception {
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

        String result = access.getUserName("this");
        assertEquals("username", result);
        assertNotEquals("notUsername", result);
    }

    @Test
    public void checkCredentials() throws Exception {

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
        boolean result = access.checkCredentials("username", "password");
        boolean falseUsername = access.checkCredentials("wrong", "password");
        boolean falsePassword = access.checkCredentials("username", "wrong");
        boolean falseBoth = access.checkCredentials("wrong", "wrong");

        assertTrue(result);
        assertFalse(falseUsername);
        assertFalse(falsePassword);
        assertFalse(falseBoth);
    }

    @Test
    public void checkIfUsed() throws Exception {

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

        boolean notTaken = access.checkIfUsed("newUser");
        boolean taken = access.checkIfUsed("username");

        assertFalse(notTaken);
        assertTrue(taken);


    }

}