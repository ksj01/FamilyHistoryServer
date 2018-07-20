package data_access;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import models.PersonModel;
import services.ClearService;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class PersonAccessTest {
    @Before
    public void clear() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void setPerson() throws Exception {
        PersonModel person = new PersonModel();

        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('f');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");

        PersonAccess access = new PersonAccess();
        boolean results = access.setPerson(person);

        assertTrue(results);
    }

    @Test
    public void getPerson() throws Exception {
        PersonModel person = new PersonModel();

        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('f');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");


        PersonAccess access = new PersonAccess();

        access.setPerson(person);

        String result = access.getPerson("123", "this");
        String checkJson = "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"123\",\n" +
                "\"firstName\":\"first\",\n" +
                "\"lastName\":\"last\",\n" +
                "\"gender\":\"f\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "}";

        String falseJson = "{\n" +
                "\"descendant\":\"that\",\n" +
                "\"personID\":\"123\",\n" +
                "\"firstName\":\"first\",\n" +
                "\"lastName\":\"last\",\n" +
                "\"gender\":\"f\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "}";

        assertEquals(result, checkJson);
        assertNotEquals(result, falseJson);

    }

    @Test
    public void getPerson1() throws Exception {
        PersonModel person = new PersonModel();

        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('f');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");


        PersonAccess access = new PersonAccess();
        access.setPerson(person);
        HashMap<String, String> resultCheck = new HashMap<>();
        resultCheck.put("descendant", "this");
        resultCheck.put("person_id", "123");
        resultCheck.put("firstName", "first");
        resultCheck.put("lastName", "last");
        resultCheck.put("gender", "f");
        resultCheck.put("father", "father");
        resultCheck.put("mother", "mother");
        resultCheck.put("spouse", "spouse");
        HashMap<String, String> results = access.getPerson("123");

        HashMap<String, String> falseResultCheck = new HashMap<>();
        falseResultCheck.put("descendant", "that");
        falseResultCheck.put("person_id", "123");
        falseResultCheck.put("firstName", "first");
        falseResultCheck.put("lastName", "last");
        falseResultCheck.put("gender", "f");
        falseResultCheck.put("father", "father");
        falseResultCheck.put("mother", "mother");
        falseResultCheck.put("spouse", "spouse");

        assertEquals(resultCheck, results);
        assertNotEquals(falseResultCheck, results);
    }

    @Test
    public void getAllPersons() throws Exception {
        PersonModel person = new PersonModel();
        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('f');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");

        PersonModel person2 = new PersonModel();
        person2.setPersonId("456");
        person2.setPersonDescendant("this");
        person2.setName("george", "jefferson");
        person2.setGender('f');
        person2.setFather("father");
        person2.setMother("mother");
        person2.setSpouse("spouse");

        PersonAccess access = new PersonAccess();
        access.setPerson(person);
        access.setPerson(person2);

        String result = access.getAllPersons("this");
        String expected = "{\n" +
                "\"data\":[\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"123\",\n" +
                "\"firstName\":\"first\",\n" +
                "\"lastName\":\"last\",\n" +
                "\"gender\":\"f\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "},\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"456\",\n" +
                "\"firstName\":\"george\",\n" +
                "\"lastName\":\"jefferson\",\n" +
                "\"gender\":\"f\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "}]\n" +
                "}";

        String unexpected = "{\n" +
                "\"data\":[\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"personID\":\"456\",\n" +
                "\"firstName\":\"george\",\n" +
                "\"lastName\":\"jefferson\",\n" +
                "\"gender\":\"f\",\n" +
                "\"father\":\"father\",\n" +
                "\"mother\":\"mother\",\n" +
                "\"spouse\":\"spouse\"\n" +
                "}]\n" +
                "}";

        assertEquals(result, expected);
        assertNotEquals(result, unexpected);
    }

    @Test
    public void getRowCount() throws Exception {
        PersonModel person = new PersonModel();
        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('f');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");

        PersonModel person2 = new PersonModel();
        person2.setPersonId("456");
        person2.setPersonDescendant("this");
        person2.setName("george", "jefferson");
        person2.setGender('f');
        person2.setFather("father");
        person2.setMother("mother");
        person2.setSpouse("spouse");

        PersonAccess access = new PersonAccess();
        access.setPerson(person);
        access.setPerson(person2);

        int result = access.getRowCount("this");
        assertEquals(result, 2);
        assertNotEquals(result, 8);
    }

}