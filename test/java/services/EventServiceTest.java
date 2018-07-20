package services;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import data_access.*;
import models.EventModel;

/**
 * Created by kevin on 3/11/18.
 */
public class EventServiceTest {
    @Before
    public void prepare() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void getPathParts() throws Exception {
        String pathPartsShort = "/event/";
        String pathPartsLong = "/event/eventId";
        EventService service = new EventService();
        String[] resultShort = service.getPathParts(pathPartsShort);
        String[] resultLong = service.getPathParts(pathPartsLong);
        String[] expectedShort = {"", "event"};
        String[] expectedLong = {"", "event", "eventId"};

        assertArrayEquals(resultLong, expectedLong);
        assertArrayEquals(resultShort, expectedShort);
        assertNotEquals(resultLong, expectedLong);
        assertNotEquals(resultShort, expectedShort);
    }

    @Test
    public void checkPathParameters() throws Exception {
        String[] wrongArray = {""};
        EventService service = new EventService();
        String wrongResult = service.checkPathParameters(wrongArray);
        String wrongExpected = "{ \"message\": \"Invalid path parameters\" }";
        assertEquals(wrongExpected, wrongResult);
        String[] correctArray = {"", "event"};
        String correctResult = service.checkPathParameters(correctArray);
        String correctExpected = "continue";
        assertEquals(correctExpected, correctResult);
    }

    @Test
    public void verifyAuthorization() throws Exception {
        authAccess auth = new authAccess();
        String userId = "123";
        String authToken = auth.generateAuth(userId);
        EventService service = new EventService();
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
        EventService service = new EventService();
        String result = service.getUserId(authToken);
        String expectedResult = "123";
        assertEquals(result, expectedResult);

        String dummyAuthToken = "123";
        String wrongResult = service.getUserId(dummyAuthToken);
        String wrongExpected = null;
        assertEquals(wrongResult, wrongExpected);
    }

    @Test
    public void getAllEvents() throws Exception {
        EventService service = new EventService();

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
        event2.setEventDescendant("this");
        event2.setEventPerson("456");
        event2.setLatitude("555.555");
        event2.setLongitude(("777.777"));
        event2.setCountry("mainCountry");
        event2.setCity("mainCity");
        event2.setEventYear(1995);
        event2.setEventType("birth");

        EventAccess access = new EventAccess();
        access.setEvent(event);
        access.setEvent(event2);

        String allEvents = service.getAllEvents("this");
        String expected = "{\n" +
                "\"data\":[\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"eventID\":\"123\",\n" +
                "\"personID\":\"456\",\n" +
                "\"latitude\":\"555.555\",\n" +
                "\"longitude\":\"777.777\",\n" +
                "\"country\":\"mainCountry\",\n" +
                "\"city\":\"mainCity\",\n" +
                "\"eventType\":\"birth\",\n" +
                "\"year\":\"1995\"\n" +
                "},\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"eventID\":\"456\",\n" +
                "\"personID\":\"456\",\n" +
                "\"latitude\":\"555.555\",\n" +
                "\"longitude\":\"777.777\",\n" +
                "\"country\":\"mainCountry\",\n" +
                "\"city\":\"mainCity\",\n" +
                "\"eventType\":\"birth\",\n" +
                "\"year\":\"1995\"\n" +
                "}]\n" +
                "}";

        String wrong = "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"eventID\":\"123\",\n" +
                "\"personID\":\"456\",\n" +
                "\"latitude\":\"555.555\",\n" +
                "\"longitude\":\"777.777\",\n" +
                "\"country\":\"mainCountry\",\n" +
                "\"city\":\"mainCity\",\n" +
                "\"eventType\":\"birth\",\n" +
                "\"year\":\"1995\"\n" +
                "}";

        assertEquals(expected, allEvents);
        assertNotEquals(wrong, allEvents);

    }

    @Test
    public void getOneEvent() throws Exception {
        EventService service = new EventService();

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
        event2.setEventDescendant("this");
        event2.setEventPerson("456");
        event2.setLatitude("555.555");
        event2.setLongitude(("777.777"));
        event2.setCountry("mainCountry");
        event2.setCity("mainCity");
        event2.setEventYear(1995);
        event2.setEventType("birth");

        EventAccess access = new EventAccess();
        access.setEvent(event);
        access.setEvent(event2);

        String allEvents = service.getOneEvent("this", "123");
        String wrong = "{\n" +
                "\"data\":[\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"eventID\":\"123\",\n" +
                "\"personID\":\"456\",\n" +
                "\"latitude\":\"555.555\",\n" +
                "\"longitude\":\"777.777\",\n" +
                "\"country\":\"mainCountry\",\n" +
                "\"city\":\"mainCity\",\n" +
                "\"eventType\":\"birth\",\n" +
                "\"year\":\"1995\"\n" +
                "},\n" +
                "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"eventID\":\"456\",\n" +
                "\"personID\":\"456\",\n" +
                "\"latitude\":\"555.555\",\n" +
                "\"longitude\":\"777.777\",\n" +
                "\"country\":\"mainCountry\",\n" +
                "\"city\":\"mainCity\",\n" +
                "\"eventType\":\"birth\",\n" +
                "\"year\":\"1995\"\n" +
                "}]\n" +
                "}";

        String expected = "{\n" +
                "\"descendant\":\"this\",\n" +
                "\"eventID\":\"123\",\n" +
                "\"personID\":\"456\",\n" +
                "\"latitude\":\"555.555\",\n" +
                "\"longitude\":\"777.777\",\n" +
                "\"country\":\"mainCountry\",\n" +
                "\"city\":\"mainCity\",\n" +
                "\"eventType\":\"birth\",\n" +
                "\"year\":\"1995\"\n" +
                "}";

        assertEquals(expected, allEvents);
        assertNotEquals(wrong, allEvents);
    }

}