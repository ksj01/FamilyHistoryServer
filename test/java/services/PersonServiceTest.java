package services;

import org.junit.Before;
import org.junit.Test;

import data_access.PersonAccess;
import data_access.authAccess;
import models.PersonModel;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class PersonServiceTest {
    @Before
    public void clear() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void getPathParts() throws Exception {
        String pathPartsShort = "/person/";
        String pathPartsLong = "/person/personId";
        PersonService service = new PersonService();
        String[] resultShort = service.getPathParts(pathPartsShort);
        String[] resultLong = service.getPathParts(pathPartsLong);
        String[] expectedShort = {"", "person"};
        String[] expectedLong = {"", "person", "personId"};

        assertArrayEquals(resultLong, expectedLong);
        assertArrayEquals(resultShort, expectedShort);
        assertNotEquals(resultLong, expectedLong);
        assertNotEquals(resultShort, expectedShort);
    }

    @Test
    public void checkPathParameters() throws Exception {
        String[] wrongArray = {""};
        PersonService service = new PersonService();
        String wrongResult = service.checkPathParameters(wrongArray);
        String wrongExpected = "{ \"message\": \"Invalid path parameters\" }";
        assertEquals(wrongExpected, wrongResult);
        String[] correctArray = {"", "person"};
        String correctResult = service.checkPathParameters(correctArray);
        String correctExpected = "continue";
        assertEquals(correctExpected, correctResult);
    }

    @Test
    public void verifyAuthorization() throws Exception {
        authAccess auth = new authAccess();
        String userId = "123";
        String authToken = auth.generateAuth(userId);
        PersonService service = new PersonService();
        String result = service.verifyAuthorization(authToken);
        String expectedResult = "continue";
        assertEquals(result, expectedResult);

        String dummyAuthToken = "123";
        String wrongResult = service.verifyAuthorization(dummyAuthToken);
        String wrongExpected = "{ \"message\": \"Invalid authorization\" }";
        assertEquals(wrongResult, wrongExpected);
    }

    @Test
    public void getUserId() throws Exception {
        authAccess auth = new authAccess();
        String userId = "123";
        String authToken = auth.generateAuth(userId);
        PersonService service = new PersonService();
        String result = service.getUserId(authToken);
        String expectedResult = "123";
        assertEquals(result, expectedResult);

        String dummyAuthToken = "123";
        String wrongResult = service.getUserId(dummyAuthToken);
        String wrongExpected = null;
        assertEquals(wrongResult, wrongExpected);
    }

    @Test
    public void getAllPersons() throws Exception {
        PersonModel person = new PersonModel();

        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('m');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");

        PersonModel person2 = new PersonModel();

        person2.setPersonId("456");
        person2.setPersonDescendant("this");
        person2.setName("first", "last");
        person2.setGender('f');
        person2.setFather("father");
        person2.setMother("mother");
        person2.setSpouse("spouse");

        PersonAccess personAccess = new PersonAccess();
        personAccess.setPerson(person);
        personAccess.setPerson(person2);

        PersonService service = new PersonService();
        String allPersons = service.getAllPersons("this");
        String expected = "{\n" +
                "\"data\":[\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"123\",\n" +
                "\"firstName\":\"first\",\n" +
                "\"lastName\":\"last\",\n" +
                "\"gender\":\"m\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "},\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"456\",\n" +
                "\"firstName\":\"first\",\n" +
                "\"lastName\":\"last\",\n" +
                "\"gender\":\"f\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "}]\n" +
                "}";
        assertEquals(allPersons, expected);
    }

    @Test
    public void getOnePerson() throws Exception {
        PersonModel person = new PersonModel();

        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('m');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");

        PersonModel person2 = new PersonModel();

        person2.setPersonId("456");
        person2.setPersonDescendant("this");
        person2.setName("first", "last");
        person2.setGender('f');
        person2.setFather("father");
        person2.setMother("mother");
        person2.setSpouse("spouse");

        PersonAccess personAccess = new PersonAccess();
        personAccess.setPerson(person);
        personAccess.setPerson(person2);

        PersonService service = new PersonService();
        String onePerson = service.getOnePerson("this", "123");
        String expected = "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"123\",\n" +
                "\"firstName\":\"first\",\n" +
                "\"lastName\":\"last\",\n" +
                "\"gender\":\"m\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "}";
        assertEquals(onePerson, expected);
    }

}