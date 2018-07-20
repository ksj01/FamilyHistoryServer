package models;

import com.google.gson.JsonObject;

import java.util.UUID;

import data_access.*;

/**
 * Created by Kevin on 2/15/2018.
 */

/**
 * Handles all logic necessary to create persons.
 */
public class UserModel {

    /**
     * Unique ID for this UserModel
     */
    private String personId;

    /**
     * Unique ID for this UserModel
     */
    private String userId;

    /**
     * first name for this UserModel
     */
    private String firstName;

    /**
     * last name for this UserModel
     */
    private String lastName;

    /**
     * m or f, representing gender
     */
    private char gender;

    /**
     * Father of this UserModel
     */
    private String email;

    /**
     * Mother of this UserModel
     */
    private String password;

    /**
     * Spouse of this UserModel
     */
    private String username;

    private PersonModel spouse;

    /**
     * Earliest that the root user can be born
     */
    private int minYear = 1980;

    /**
     * Latest that the root user can be born
     */
    private int maxYear = 2000;


    /**
     * Generates a new UserModel to be stored in the database.
     * @param username  the username that the new UserModel should be tied to
     * @param gender  the gender of the user being created
     * @param generations   How many generations you want to make for this user
     * @return          the newly generated UserModel
     */
    public UserModel generateUser(JsonObject request, String username, char gender, int generations) {
        this.username = username;
        this.userId = username;
        this.gender = gender;
        this.personId = username;
        int birthYear = new EventModel().generateEventYear(minYear, maxYear);
        int spouseBirth = new EventModel().generateEventYear(minYear, maxYear);

        PersonModel thisPerson = new PersonModel();
        thisPerson.generateUserPerson(request, userId, gender, generations, birthYear);
        thisPerson.setPersonId(this.personId);

        if (gender == 'm') {
            this.spouse = new PersonModel().generateSpouse(thisPerson, 'f');
        }
        else {
            this.spouse = new PersonModel().generateSpouse(thisPerson, 'm');
        }

        createEvents(thisPerson, birthYear, spouse, spouseBirth);
        PersonAccess store = new PersonAccess();
        store.setPerson(thisPerson);
        store.setPerson(spouse);

        return this;
    }

    /**
     * Creates generation data for an existing user after that user's data has been wiped.
     * @param username  Username of the user
     * @param userId    UserId for the user
     * @param gender    Gender of the user
     * @param generations   How many generations of data you want.
     * @return              The newly created UserModel
     */
    public UserModel fillExistingUser(String username, String userId, char gender, int generations) {
        this.username = username;
        this.userId = UUID.randomUUID().toString();
        this.gender = gender;
        this.personId = this.userId;
        int birthYear = new EventModel().generateEventYear(minYear, maxYear);
        int spouseBirth = new EventModel().generateEventYear(minYear, maxYear);

        PersonModel thisPerson = new PersonModel();
        thisPerson.generatePerson(userId, gender, generations, birthYear);

        thisPerson.setPersonId(userId);

        if (gender == 'm') {
            this.spouse = new PersonModel().generateSpouse(thisPerson, 'f');
        }
        else {
            this.spouse = new PersonModel().generateSpouse(thisPerson, 'm');
        }

        createEvents(thisPerson, birthYear, spouse, spouseBirth);

        PersonAccess store = new PersonAccess();
        store.setPerson(thisPerson);
        store.setPerson(spouse);

        return this;
    }


    /**
     * Generates a UserModel object based on user-submitted Json.
     * @param user Json representing the new person.
     */
    public void generateUserFromJson(JsonObject user) {

        String userName = user.get("userName").toString();
        userName = userName.substring(1, userName.length() - 1);
        String password = user.get("password").toString();
        password = password.substring(1, password.length() - 1);
        String email = user.get("email").toString();
        email = email.substring(1, email.length() - 1);
        String firstName = user.get("firstName").toString();
        firstName = firstName.substring(1, firstName.length() - 1);
        String lastName = user.get("lastName").toString();
        lastName = lastName.substring(1, lastName.length() - 1);
        char gender = user.get("gender").toString().toCharArray()[1];
        String personID = user.get("personID").toString();
        personID = personID.substring(1, personID.length() - 1);

        this.userId = personID;
        this.username = userName;
        this.personId = personID;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;

        UserAccess store = new UserAccess();
        store.storeUser(this);
    }

    /**
     * Creates multiple events for both the root person and their spouse.
     * @param thisPerson    PersonModel for whom we want events
     * @param birthYear     Year that the user was born
     * @param spouse        PersonModel for the user's spouse
     * @param spouseBirth   Year that the spouse was born
     */
    public void createEvents(PersonModel thisPerson, int birthYear, PersonModel spouse, int spouseBirth) {
        createBirthEvent(thisPerson, birthYear);
        createBaptismEvent(thisPerson, birthYear);
        EventModel marriage = createMarriageEvent(thisPerson, birthYear);

        createBirthEvent(spouse, spouseBirth);
        createBaptismEvent(spouse, spouseBirth);

        EventModel spouseMarriage = new EventModel();
        spouseMarriage = spouseMarriage.cloneEvent(spouse.getPersonId(), marriage);

        EventAccess store = new EventAccess();
        store.setEvent(spouseMarriage);
    }

    /**
     * Creates a birth event for the passed person
     * @param thisPerson    Person for whom the birth is for
     * @param birthYear     Year that the person was born
     */
    public void createBirthEvent(PersonModel thisPerson, int birthYear) {
        EventModel birth = new EventModel();
        birth.generateEvent(thisPerson);
        birth.setEventType("birth");
        birth.setEventYear(birthYear);
        EventAccess storeEvents = new EventAccess();
        storeEvents.setEvent(birth);
    }

    /**
     * Creates a baptism event for the passed person
     * @param thisPerson    Person for whom the baptism is for
     * @param birthYear     Year that the person was born
     */
    public void createBaptismEvent(PersonModel thisPerson, int birthYear) {
        EventModel baptism = new EventModel();
        baptism.generateEvent(thisPerson);
        baptism.setEventType("baptism");
        baptism.setEventYear(baptism.generateEventYear(birthYear + baptism.getMinBaptism(), baptism.getCurrentYear()));
        EventAccess storeEvents = new EventAccess();
        storeEvents.setEvent(baptism);
    }

    /**
     * Creates a marriage event for the passed person
     * @param thisPerson    Person for whom the marriage is for
     * @param birthYear     Year that the person was born
     * @return              This marriage event, to be cloned for the spouse's marriage event
     */
    public EventModel createMarriageEvent(PersonModel thisPerson, int birthYear) {
        EventModel marriage = new EventModel();
        marriage.generateEvent(thisPerson);
        marriage.setEventType("marriage");
        marriage.setEventYear(marriage.generateEventYear((birthYear + marriage.getMinMarriage()), marriage.getCurrentYear()));
        EventAccess storeEvents = new EventAccess();
        storeEvents.setEvent(marriage);
        return marriage;
    }

    /**
     * Sets a specific ID for the current UserModel object
     * @param personId  Unique ID to be associated with the current UserModel object
     */
    public void setPersonId(String personId) {this.personId = personId;}


    /**
     * Sets the first and last name for this UserModel
     * @param firstName String of the first name to be used
     * @param lastName  String of the last name to be used
     */
    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Sets a gender for the current UserModel object
     * @param gender    single char, m for male, f for female
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Getter for this UserModel object's personID variable
     * @return  the person's id that is associated with this UserModel
     */
    public String getPersonId() {return this.personId;}

    /**
     * Getter for this UserModel object's firstName variable
     * @return  the first name that is associated with this UserModel
     */
    public String getFirstName() {return this.firstName;}

    /**
     * Getter for this UserModel object's lastName variable
     * @return  the last name that is associated with this UserModel
     */
    public String getLastName() {return this.lastName;}

    /**
     * Getter for this UserModel object's gender variable
     * @return  the gender that is associated with this UserModel
     */
    public char getGender() {return this.gender;}

    /**
     * Getter for this UserModel object's password variable
     * @return  the password that is associated with this UserModel
     */
    public String getPassword() {return this.password;}

    /**
     * Setter for this UserModel's password variable
     * @param password  Password to be used for this User
     */
    public void setPassword(String password) {this.password = password;}

    /**
     * Setter for this UserModel's email variable
     * @param email  email to be used for this User
     */
    public void setEmail(String email) {this.email = email;}

    /**
     * Getter for this UserModel object's email variable
     * @return  the email that is associated with this UserModel
     */
    public String getEmail() {return this.email;}

    /**
     * Getter for this UserModel object's username variable
     * @return  the username that is associated with this UserModel
     */
    public String getUsername() {return this.username;}

    /**
     * Setter for this UserModel's firstName variable
     * @param firstName  firstName to be used for this User
     */
    public void setFirstName(String firstName) {this.firstName = firstName;}

    /**
     * Setter for this UserModel's lastName variable
     * @param lastName  lastName to be used for this User
     */
    public void setLastName(String lastName) {this.lastName = lastName;}

    /**
     * Setter for this UserModel's userId variable
     * @param userId  userId to be used for this User
     */
    public void setUserId(String userId) {this.userId = userId;}

    /**
     * Getter for this UserModel object's userId variable
     * @return  the userId that is associated with this UserModel
     */
    public String getUserId() {return this.userId;}

    /**
     * Setter for this UserModel's username variable
     * @param username  username to be used for this User
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
