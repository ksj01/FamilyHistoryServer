package data_access;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import models.EventModel;
import services.ClearService;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class EventAccessTest {
    @Before
    public void clear() {
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void setEvent() throws Exception {
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

        EventAccess access = new EventAccess();
        boolean results = access.setEvent(event);
        assertTrue(results);
    }

    @Test
    public void getEvent() throws Exception {

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

        EventModel falseEvent = new EventModel();
        falseEvent.setEventId("456");
        falseEvent.setEventDescendant("this");
        falseEvent.setEventPerson("456");
        falseEvent.setLatitude("555.555");
        falseEvent.setLongitude(("777.777"));
        falseEvent.setCountry("mainCountry");
        falseEvent.setCity("mainCity");
        falseEvent.setEventYear(1995);
        falseEvent.setEventType("birth");

        EventAccess access = new EventAccess();
        access.setEvent(event);
        access.setEvent(falseEvent);

        String eventResults = access.getEvent("123", "this");
        String falseEventResults = access.getEvent("456", "this");
        String shouldBeResults = "{\n" +
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
        assertEquals(eventResults, shouldBeResults);
        assertNotEquals(falseEventResults, shouldBeResults);
    }

    @Test
    public void getAllEvents() throws Exception {

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

        EventModel falseEvent = new EventModel();
        falseEvent.setEventId("789");
        falseEvent.setEventDescendant("that");
        falseEvent.setEventPerson("456");
        falseEvent.setLatitude("555.555");
        falseEvent.setLongitude(("777.777"));
        falseEvent.setCountry("mainCountry");
        falseEvent.setCity("mainCity");
        falseEvent.setEventYear(1995);
        falseEvent.setEventType("birth");

        EventAccess access = new EventAccess();
        access.setEvent(event);
        access.setEvent(event2);
        access.setEvent(falseEvent);

        String eventResults = access.getAllEvents("this");
        String eventResultsFalse = access.getAllEvents("that");
        String shouldBeResults = "{\n" +
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
        assertEquals(eventResults, shouldBeResults);
        assertNotEquals(eventResults, eventResultsFalse);

    }

    @Test
    public void getRowCount() throws Exception {
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
        int count = access.getRowCount("this");
        assertEquals(count, 2);
        assertNotEquals(count, 8);
    }

}