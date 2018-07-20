package models;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.Random;
import java.util.UUID;

import data_access.*;

/**
 * Created by Kevin on 2/15/2018.
 */

/**
 * Handles the logic necessary for generating events.
 */
public class EventModel {

    /**
     * unique ID for this EventModel
     */
    private String eventId;

    /**
     * PersonService ID representing the UserModel
     */
    private String descendant;

    /**
     * PersonService ID representing the PersonModel that this EventModel happened to
     */
    private String personId;

    /**
     * latitude of this EventModel location
     */
    private float latitude;

    /**
     * longitude of this EventModel location
     */
    private float longitude;

    /**
     * country that this EventModel occured in
     */
    private String country;

    /**
     * city that this EventModel occured to
     */
    private String city;

    /**
     * Type of EventModel that this is
     */
    private String eventType;

    /**
     * Year in which this EventModel occured
     */
    private int year;

    /**
     * The latest year a person can be born.
     */
    private int maxBirthYear = 2018;

    /**
     * The minimum number of years between generations
     */
    private int minGeneration = 15;

    /**
     * The maximum number of years between generations
     */
    private int maxGeneration = 45;

    /**
     * The minimum age of baptisms
     */
    private int minBaptism = 8;

    /**
     * The minimum age for a person to be married
     */
    private int minMarriage = 18;

    /**
     * The minimum age a person can die (for our fictional purposes).
     */
    private int minDeath = 46;

    /**
     * The oldest one of our people can be.
     */
    private int maxDeath = 100;

    /**
     * The current year.
     */
    private int currentYear = 2018;


    /**
     * Generates a new EventModel to be stored in the database.
     * @param thisPerson  PersonModel that the new EventModel should be tied to
     * @return          the newly created EventModel
     */
    public EventModel generateEvent(PersonModel thisPerson) {
        String[] location = this.generateEventLocation();
            this.descendant = thisPerson.getDescendent();
        this.eventId = UUID.randomUUID().toString();
        this.personId = thisPerson.getPersonId();
        this.latitude = Float.parseFloat(location[0]);
        this.longitude = Float.parseFloat(location[1]);
        this.country = location[2];
        this.city = location[3];
        return this;
    }

    /**
     * This clones an event, typically used so that spouses can have effectively identical info for their marriage events.
     * @param personId  PersonId you want the new event to be associated with
     * @param orig      Event being clones
     * @return          Returns the newly cloned event
     */
    public EventModel cloneEvent(String personId, EventModel orig) {
        PersonModel thisPerson = new PersonModel().getPerson(personId);
        this.eventId = UUID.randomUUID().toString();
        if (thisPerson == null) {
            this.descendant = personId;
        }
        else {
            this.descendant = thisPerson.getDescendent();
        }
        this.personId = personId;
        this.latitude = orig.latitude;
        this.longitude = orig.longitude;
        this.country = orig.country;
        this.city = orig.city;
        this.year = orig.year;
        this.eventType = orig.eventType;
        return this;
    }

    /**
     * Generates an EventModel object based on user-submitted Json.
     * @param event Json representing the new event.
     */
    public void generateEventFromJson(JsonObject event) {

        String eventType = event.get("eventType").toString();
        eventType = eventType.substring(1, eventType.length() - 1);
        String personID = event.get("personID").toString();
        personID = personID.substring(1, personID.length() - 1);
        String city = event.get("city").toString();
        city = city.substring(1, city.length() - 1);
        float latitude = Float.parseFloat(event.get("latitude").toString());
        float longitude = Float.parseFloat(event.get("longitude").toString());
        int year = Integer.parseInt(event.get("year").toString());
        String eventID = event.get("eventID").toString();
        eventID = eventID.substring(1, eventID.length() - 1);
        String descendant = event.get("descendant").toString();
        descendant = descendant.substring(1, descendant.length() - 1);
        String country = event.get("country").toString();
        country = country.substring(1, country.length() - 1);

        this.eventType = eventType;
        this.personId = personID;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
        this.eventId = eventID;
        this.descendant = descendant;
        this.country = country;
        EventAccess store = new EventAccess();
        store.setEvent(this);
    }

    /**
     * Generates multiple events for a person and makes sure that the dates make sense. Creates birth, marriage, death, and baptism for th passed person.
     * @param thisPerson    PersonModel for whom the events are being made
     * @param birthYear     Year that the person should be born.
     */
    public void generateMultipleEvents(PersonModel thisPerson, int birthYear) {
        EventModel birth = new EventModel();
        birth.generateEvent(thisPerson);
        birth.eventType = "birth";
        birth.year = birthYear;
        EventModel death = new EventModel();
        death.generateEvent(thisPerson);
        death.eventType = "death";
        death.year = death.generateEventYear((birth.year + minDeath), birth.year + death.maxDeath);
        EventModel baptism = new EventModel();
        baptism.generateEvent(thisPerson);
        baptism.eventType = "baptism";
        baptism.year = baptism.generateEventYear(birth.year + baptism.minBaptism, death.year);
        EventModel marriage = new EventModel();
        marriage.generateEvent(thisPerson);
        marriage.eventType = "marriage";
        marriage.year = marriage.generateEventYear((birth.year + marriage.minMarriage), death.year);
        EventAccess store = new EventAccess();
        store.setEvent(birth);
        store.setEvent(baptism);
        store.setEvent(marriage);
        store.setEvent(death);
    }


    /**
     * Decides the latitude, longitude, country, and city that the new EventModel occurred in
     * @return  String[] containing the location information for the new EventModel
     */
    public String[] generateEventLocation() {
        String[] newEventLocation = new String[4];
        StringBuilder location = new StringBuilder("");
        BufferedReader br = null;
        File locFile = null;
        locFile = new File("lib/src/main/json/locations.json");
        try {
            br = new BufferedReader(new FileReader(locFile));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        String line;
        try {
            while ((line = br.readLine()) != null) {
                location.append(line);
            }
        } catch (IOException e) {
            System.out.println("IO Exception.");
        }
        JsonParser parser  = new JsonParser();
        JsonElement jsonLocation = parser.parse(location.toString());
        JsonArray locationArray = jsonLocation.getAsJsonObject().get("data").getAsJsonArray();
        Random rand = new Random();
        int selection = rand.nextInt(locationArray.size());
        JsonElement selectedLocation = locationArray.get(selection);

        String finalCountry = selectedLocation.getAsJsonObject().get("country").toString();
        finalCountry = finalCountry.substring(1, finalCountry.length() - 1);

        String finalCity = selectedLocation.getAsJsonObject().get("city").toString();
        finalCity = finalCity.substring(1, finalCity.length() - 1);
        newEventLocation[0] = selectedLocation.getAsJsonObject().get("latitude").toString();
        newEventLocation[1] = selectedLocation.getAsJsonObject().get("longitude").toString();
        newEventLocation[2] = finalCountry;
        newEventLocation[3] = finalCity;
        return newEventLocation;
    }

    /**
     * Decides the year that the new EventModel took place
     * @param min   Max year for the event
     * @param max   Min year for the event
     * @return
     */
    public int generateEventYear(int min, int max) {
        Random rand = new Random();
        int newEventYear = rand.nextInt(max-min) + min;
        return newEventYear;
    }

    /**
     * Sets a specific EventService ID for the current EventModel object
     * @param eventID   Unique EventModel ID
     */
    public void setEventId(String eventID) {this.eventId = eventID;}

    /**
     * Associates a specific descendant to be associated with the current EventModel object
     * @param descendant    PersonService ID associated with the UserModel
     */
    public void setEventDescendant(String descendant) {this.descendant = descendant;}

    /**
     * Sets a PersonModel to which the EventModel occured
     * @param person    PersonModel ID to be associated with this EventModel
     */
    public void setEventPerson(String person) {this.personId = person;}

    /**
     * Sets the EventModel type associated with the current EventModel
     * @param type  of EventModel that occured
     */
    public void setEventType(String type) {
        this.eventType = type;
    }

    /**
     * Sets the year in which this EventModel occured
     * @param year in which the EventModel took place
     */
    public void setEventYear(int year) {
        this.year = year;
    }

    /**
     * Getter for this EventModel object's ID variable
     * @return  the id that is associated with this EventModel
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Getter for this EventModel object's descendent variable
     * @return  the descendant that is associated with this EventModel
     */
    public String getDescendant() {return this.descendant;}

    /**
     * Getter for this EventModel object's PersonModel ID variable
     * @return  the PersonModel's id that is associated with this EventModel
     */
    public String getPersonId() {return this.personId;}

    /**
     * Getter for this EventModel object's latitude variable
     * @return  the latitude that is associated with this EventModel
     */
    public float getLatitude() {
        return latitude;}

    /**
     * Getter for this EventModel object's longitude variable
     * @return  the longitude that is associated with this EventModel
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Getter for this EventModel object's county variable
     * @return  the country that this EventModel occured in
     */
    public String getCountry() {return this.country;}

    /**
     * Getter for this EventModel object's city variable
     * @return  the city that this EventModel occured in
     */
    public String getCity() {return this.city;}

    /**
     * Getter for this EventModel object's eventType variable
     * @return  the type of this EventModel
     */
    public String getEventType() {return this.eventType;}

    /**
     * Setter for the latitude value
     * @param latitude  Latitude to be associated with this event
     */
    public void setLatitude(String latitude) {this.latitude = Float.parseFloat(latitude);}

    /**
     * Setter for the longitude value
     * @param longitude  longitude to be associated with this event
     */
    public void setLongitude(String longitude) {this.longitude = Float.parseFloat(longitude);}

    /**
     * Setter for the country value
     * @param country  country to be associated with this event
     */
    public void setCountry(String country) {this.country = country;}

    /**
     * Setter for the city value
     * @param city  city to be associated with this event
     */
    public void setCity(String city) {this.city = city;}

    /**
     * Getter for this EventModel object's year variable
     * @return  the year of this EventModel
     */
    public int getYear() {return this.year;}

    /**
     * Getter for this EventModel object's minGeneration variable
     * @return  the minGeneration of this EventModel
     */
    public int getMinGeneration() { return this.minGeneration;}

    /**
     * Getter for this EventModel object's maxGeneration variable
     * @return  the maxGeneration of this EventModel
     */
    public int getMaxGeneration() {return this.maxGeneration;}

    /**
     * Getter for this EventModel object's minBaptism variable
     * @return  the minBaptism of this EventModel
     */
    public int getMinBaptism() {return this.minBaptism;}

    /**
     * Getter for this EventModel object's minMarriage variable
     * @return  the minMarriage of this EventModel
     */
    public int getMinMarriage() {return this.minMarriage;}

    /**
     * Getter for this EventModel object's minDeath variable
     * @return  the minDeath of this EventModel
     */
    public int getMinDeath() {return this.minDeath;}

    /**
     * Getter for this EventModel object's maxDeath variable
     * @return  the maxDeath of this EventModel
     */
    public int getMaxDeath() {return this.maxDeath;}

    /**
     * Getter for this EventModel object's currentYear variable
     * @return  the currentYear of this EventModel
     */
    public int getCurrentYear() {return this.currentYear;}
}
