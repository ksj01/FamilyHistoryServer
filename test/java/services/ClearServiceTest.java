package services;

import org.junit.Before;
import org.junit.Test;

import data_access.EventAccess;
import data_access.PersonAccess;
import data_access.UserAccess;
import models.EventModel;
import models.PersonModel;
import models.UserModel;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class ClearServiceTest {
    @Before
    public void prepare() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void clear() throws Exception {
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
        person2.setPersonDescendant("that");
        person2.setName("first", "last");
        person2.setGender('f');
        person2.setFather("father");
        person2.setMother("mother");
        person2.setSpouse("spouse");

        PersonAccess personAccess = new PersonAccess();
        personAccess.setPerson(person);
        personAccess.setPerson(person2);

        EventModel event = new EventModel();
        event.setEventId("123");
        event.setEventDescendant("this");
        event.setEventPerson("456");
        event.setLatitude("555.555");
        event.setLongitude(("777.777"));
        event.setCountry("mainCountry");
        event.setCity("mainCity");
        event.setEventYear(1995);
        event.setEventType("birth");

        EventModel event2 = new EventModel();
        event2.setEventId("456");
        event2.setEventDescendant("that");
        event2.setEventPerson("456");
        event2.setLatitude("555.555");
        event2.setLongitude(("777.777"));
        event2.setCountry("mainCountry");
        event2.setCity("mainCity");
        event2.setEventYear(1995);
        event2.setEventType("birth");

        EventAccess eventAccess = new EventAccess();
        eventAccess.setEvent(event);
        eventAccess.setEvent(event2);

        int rows = eventAccess.getRowCount("this");
        assertEquals(rows, 1);
        assertNotEquals(rows, 0);

        rows = eventAccess.getRowCount("that");
        assertEquals(rows, 1);
        assertNotEquals(rows, 0);

        rows = personAccess.getRowCount("this");
        assertNotEquals(rows, 0);
        assertEquals(rows, 1);

        rows = personAccess.getRowCount("that");
        assertNotEquals(rows, 0);
        assertEquals(rows, 1);

        ClearService clear = new ClearService();
        clear.clear();

        rows = eventAccess.getRowCount("this");
        assertEquals(rows, 0);
        assertNotEquals(rows, 1);

        rows = personAccess.getRowCount("this");
        assertEquals(rows, 0);
        assertNotEquals(rows, 1);
    }

    @Test
    public void clear1() throws Exception {

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

        PersonModel person = new PersonModel();

        person.setPersonId("123");
        person.setPersonDescendant("this");
        person.setName("first", "last");
        person.setGender('f');
        person.setFather("father");
        person.setMother("mother");
        person.setSpouse("spouse");

        PersonModel person2 = new PersonModel();

        person2.setPersonId("123");
        person2.setPersonDescendant("that");
        person2.setName("first", "last");
        person2.setGender('f');
        person2.setFather("father");
        person2.setMother("mother");
        person2.setSpouse("spouse");

        PersonAccess personAccess = new PersonAccess();
        personAccess.setPerson(person);
        personAccess.setPerson(person2);

        EventModel event = new EventModel();
        event.setEventId("123");
        event.setEventDescendant("this");
        event.setEventPerson("456");
        event.setLatitude("555.555");
        event.setLongitude(("777.777"));
        event.setCountry("mainCountry");
        event.setCity("mainCity");
        event.setEventYear(1995);
        event.setEventType("birth");

        EventModel event2 = new EventModel();
        event2.setEventId("123");
        event2.setEventDescendant("that");
        event2.setEventPerson("456");
        event2.setLatitude("555.555");
        event2.setLongitude(("777.777"));
        event2.setCountry("mainCountry");
        event2.setCity("mainCity");
        event2.setEventYear(1995);
        event2.setEventType("birth");

        EventAccess eventAccess = new EventAccess();
        eventAccess.setEvent(event);
        eventAccess.setEvent(event2);

        int rows = eventAccess.getRowCount("this");
        assertEquals(rows, 1);
        assertNotEquals(rows, 0);

        rows = eventAccess.getRowCount("that");
        assertEquals(rows, 1);
        assertNotEquals(rows, 0);

        rows = personAccess.getRowCount("this");
        assertNotEquals(rows, 0);
        assertEquals(rows, 1);

        rows = personAccess.getRowCount("that");
        assertNotEquals(rows, 0);
        assertEquals(rows, 1);

        ClearService clear = new ClearService();
        clear.clear("this");

        rows = eventAccess.getRowCount("this");
        assertEquals(rows, 0);
        assertNotEquals(rows, 1);

        rows = eventAccess.getRowCount("that");
        assertEquals(rows, 1);
        assertNotEquals(rows, 0);

        rows = personAccess.getRowCount("this");
        assertEquals(rows, 0);
        assertNotEquals(rows, 1);

        rows = personAccess.getRowCount("that");
        assertNotEquals(rows, 0);
        assertEquals(rows, 1);
    }

}