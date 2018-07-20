package models;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.Random;

import data_access.*;

/**
 * Created by Kevin on 2/15/2018.
 */

/**
 * Handles all logic necessary to create persons.
 */
public class PersonModel {

    /**
     * Unique ID for this PersonModel
     */
    private String personId;

    /**
     * PersonModel ID representing the UserModel
     */
    private String descendent;

    /**
     * first name for this PersonModel
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
     * Father of this PersonModel
     */
    private PersonModel father = null;

    /**
     * Mother of this PersonModel
     */
    private PersonModel mother = null;

    /**
     * Spouse of this PersonModel
     */
    private PersonModel spouse;

    /**
     * Id of this person's father
     */
    private String fatherId;

    /**
     * Id of this person's mother
     */
    private String motherId;

    /**
     * Id of this person's spouse
     */
    private String spouseId;

    /**
     * Generates a new PersonModel to be stored in the database.
     * @param userId  the username that the new PersonModel should be tied to
     * @param gender  the gender of the PersonModel being created
     * @return          the newly generated PersonModel as a JSON object
     */
    public PersonModel generatePerson(String userId, char gender) {
        String[] name = this.generateName(gender);
        this.personId = UUID.randomUUID().toString();
        this.descendent = userId;
        this.firstName = name[0];
        this.lastName = name[1];
        this.gender = gender;
        return this;
    }

    /**
     * Generates a new spouse to be stored in the database.
     * @param user  the PersonModel that the spouse should be tied to
     * @param gender  the gender of the PersonModel being created
     * @return          the newly generated PersonModel as a JSON object
     */
    public PersonModel generateSpouse(PersonModel user, char gender) {
        String[] name = this.generateName(gender);
        this.personId = UUID.randomUUID().toString();
        this.descendent = user.descendent;
        this.firstName = name[0];
        this.lastName = name[1];
        this.gender = gender;
        this.spouse = user;
        return this;
    }

    /**
     * Recursively generates PersonModels to be stored in the database.
     * @param userId  the username that the new PersonModel should be tied to
     * @param gender  the gender of the PersonModel being created
     * @param generations  the remaining number of generations to be made.
     * @param previousGenYear  Birthday of the generation younger than this one
     * @return          the newly generated PersonModel as a JSON object
     */
    public PersonModel generatePerson(String userId, char gender, int generations, int previousGenYear) {
        if (generations > 0) {
            EventModel yearGenerator = new EventModel();
            int fatherBirthYear = yearGenerator.generateEventYear(previousGenYear - yearGenerator.getMaxGeneration(), previousGenYear - yearGenerator.getMinGeneration());
            int motherBirthYear = yearGenerator.generateEventYear(previousGenYear - yearGenerator.getMaxGeneration(), previousGenYear - yearGenerator.getMinGeneration());
            generations--;
            String[] name = this.generateName(gender);
            this.personId = UUID.randomUUID().toString();
            this.descendent = userId;
            this.firstName = name[0];
            this.lastName = name[1];
            this.gender = gender;
            if (generations == 0) {
                PersonModel father = new PersonModel();
                this.father = father.generatePerson(userId, 'm');
                PersonModel mother = new PersonModel();
                this.mother = mother.generatePerson(userId, 'f');
            } else {
                PersonModel father = new PersonModel();
                this.father = father.generatePerson(userId, 'm', generations, fatherBirthYear);
                PersonModel mother = new PersonModel();
                this.mother = mother.generatePerson(userId, 'f', generations, motherBirthYear);
            }
            new EventModel().generateMultipleEvents(this.father, fatherBirthYear);
            new EventModel().generateMultipleEvents(this.mother, motherBirthYear);
            this.father.spouse = this.mother;
            this.mother.spouse = this.father;
            PersonAccess store = new PersonAccess();
            store.setPerson(this.father);
            store.setPerson(this.mother);

            return this;
        }
        return null;
    }

    public PersonModel generateUserPerson(JsonObject request, String userId, char gender, int generations, int previousGenYear) {
        if (generations > 0) {
            EventModel yearGenerator = new EventModel();
            int fatherBirthYear = yearGenerator.generateEventYear(previousGenYear - yearGenerator.getMaxGeneration(), previousGenYear - yearGenerator.getMinGeneration());
            int motherBirthYear = yearGenerator.generateEventYear(previousGenYear - yearGenerator.getMaxGeneration(), previousGenYear - yearGenerator.getMinGeneration());
            generations--;
            this.personId = UUID.randomUUID().toString();
            this.descendent = userId;
            String firstName = request.get("firstName").toString();
            this.firstName = firstName.substring(1, firstName.length() - 1);
            String lastName = request.get("lastName").toString();
            this.lastName = lastName.substring(1, lastName.length() - 1);
            this.gender = gender;
            if (generations == 0) {
                PersonModel father = new PersonModel();
                this.father = father.generatePerson(userId, 'm');
                PersonModel mother = new PersonModel();
                this.mother = mother.generatePerson(userId, 'f');
            } else {
                PersonModel father = new PersonModel();
                this.father = father.generatePerson(userId, 'm', generations, fatherBirthYear);
                PersonModel mother = new PersonModel();
                this.mother = mother.generatePerson(userId, 'f', generations, motherBirthYear);
            }
            new EventModel().generateMultipleEvents(this.father, fatherBirthYear);
            new EventModel().generateMultipleEvents(this.mother, motherBirthYear);
            this.father.spouse = this.mother;
            this.mother.spouse = this.father;
            PersonAccess store = new PersonAccess();
            store.setPerson(this.father);
            store.setPerson(this.mother);

            return this;
        }
        return null;
    }

    /**
     * Generates an PersonModel object based on user-submitted Json.
     * @param person Json representing the new person.
     */
    public void generatePersonFromJson(JsonObject person) {

        String descendant = person.get("descendant").toString();
        descendant = descendant.substring(1, descendant.length() - 1);
        String firstName = person.get("firstName").toString();
        firstName = firstName.substring(1, firstName.length() - 1);
        String lastName = person.get("lastName").toString();
        lastName = lastName.substring(1, lastName.length() - 1);
        char gender = person.get("gender").toString().toCharArray()[1];
        String personId = person.get("personID").toString();
        personId = personId.substring(1, personId.length() - 1);
        String father = null;
        String mother = null;
        String spouse = null;
        if (person.has("father")) {
            father = person.get("father").toString();
            father = father.substring(1, father.length() - 1);
        }
        if (person.has("mother")) {
            mother = person.get("mother").toString();
            mother = mother.substring(1, mother.length() - 1);
        }
        if (person.has("spouse")) {
            spouse = person.get("spouse").toString();
            spouse = spouse.substring(1, spouse.length() - 1);
        }

        this.personId = personId;
        this.descendent = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personId = personId;
        this.fatherId = father;
        this.motherId = mother;
        this.spouseId = spouse;

        PersonAccess store = new PersonAccess();
        store.setPerson(this);
    }

    /**
     * Creates a name for the current person based on the Json files located in the project
     * @param gender    Gender for the person
     * @return          String[] for the first and last name. The [0] index is the first name and the [1] index is the last name.
     */
    public String[] generateName(char gender) {
        String[] name = new String[2];
        StringBuilder fName = new StringBuilder("");
        StringBuilder lName = new StringBuilder("");
        BufferedReader firstBr = null;
        BufferedReader lastBr = null;
        File firstName = null;
        File lastName = null;
        if (gender == 'm') {
            firstName = new File("lib/src/main/json/mnames.json");
        }
        else {
            firstName = new File("lib/src/main/json/fnames.json");
        }
        lastName = new File("lib/src/main/json/snames.json");
        try {
            firstBr = new BufferedReader(new FileReader(firstName));
            lastBr = new BufferedReader(new FileReader(lastName));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        String line;
        try {
            while ((line = firstBr.readLine()) != null) {
                fName.append(line);
            }
            while ((line = lastBr.readLine()) != null) {
                lName.append(line);
            }
        } catch (IOException e) {
            System.out.println("IO Exception.");
        }
        JsonParser parser  = new JsonParser();
        JsonElement jsonFirstName = parser.parse(fName.toString());
        JsonElement jsonLastName = parser.parse(lName.toString());
        JsonArray firstNames = jsonFirstName.getAsJsonObject().get("data").getAsJsonArray();
        JsonArray lastNames = jsonLastName.getAsJsonObject().get("data").getAsJsonArray();
        Random rand = new Random();
        String finalFirstName = firstNames.get(rand.nextInt(firstNames.size())).toString();
        finalFirstName = finalFirstName.substring(1, finalFirstName.length() - 1);

        String finalLastName = lastNames.get(rand.nextInt(lastNames.size())).toString();
        finalLastName = finalLastName.substring(1, finalLastName.length() - 1);
        name[0] = finalFirstName;
        name[1] = finalLastName;
        return name;
    }

    /**
     * Sets a specific ID for the current PersonModel object
     * @param personId  Unique ID to be associated with the current PersonModel object
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Associates this PersonModel with a root PersonModel, or the UserModel
     * @param descendant    ID of the UserModel
     */
    public void setPersonDescendant(String descendant) {
        this.descendent = descendant;
    }

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
     * Sets a gender for the current PersonModel object
     * @param gender    single char, m for male, f for female
     */
    public void setGender(char gender) {
        this.gender = gender;
    }

    /**
     * Makes another PersonModel the father of the current PersonModel.
     * @param personId  Unique ID of the PersonModel to be the current PersonModel's father
     */
    public void setFather(String personId) {
        this.fatherId = personId;
    }

    /**
     * Makes another PersonModel the mother of the current PersonModel
     * @param personId Unique ID of the PersonModel to be the current PersonModel's mother
     */
    public void setMother(String personId) {
        this.motherId = personId;
    }

    /**
     * Makes another PersonModel the spouse of the current PersonModel.
     * @param spouseId  ID of the PersonModel to be the current PersonModel's spouse
     */
    public void setSpouse(String spouseId) {
        this.spouseId = spouseId;
    }


    /**
     * Getter for a particular UserModel associated with the passed ID
     * @param personId  ID for the UserModel you want to get
     * @return          returns a PersonModel object
     */
    public PersonModel getPerson(String personId) {
        PersonAccess personDb = new PersonAccess();
        HashMap<String, String> person = personDb.getPerson(personId);
        if (person == null) {
            return null;
        }

        this.personId = personId;
        this.descendent = person.get("descendant");
        this.firstName = person.get("firstName");
        this.lastName = person.get("lastName");
        this.gender = person.get("gender").charAt(0);
        this.fatherId = person.get("father");
        this.motherId = person.get("mother");
        this.spouseId = person.get("spouse");

        return this;
    }

    /**
     * Getter for this PersonModel object's PersonModel ID variable
     * @return  the PersonModel's id that is associated with this PersonModel
     */
    public String getPersonId() {return this.personId;}

    /**
     * Getter for this PersonModel object's descendant ID variable
     * @return  the id that is associated with this PersonModel's descendent
     */
    public String getDescendent() {return this.descendent;}

    /**
     * Getter for this PersonModel object's firstName variable
     * @return  the first name that is associated with this PersonModel
     */
    public String getFirstName() {return this.firstName;}

    /**
     * Getter for this PersonModel object's lastName variable
     * @return  the last name that is associated with this PersonModel
     */
    public String getLastName() {return this.lastName;}

    /**
     * Getter for this PersonModel object's gender variable
     * @return  the gender that is associated with this PersonModel
     */
    public char getGender() {return this.gender;}

    /**
     * Getter for this PersonModel object's father variable
     * @return  the father of this PersonModel
     */
    public PersonModel getFather() {return this.father;}

    /**
     * Getter for this PersonModel object's mother variable
     * @return  the mother of this PersonModel
     */
    public PersonModel getMother() {return this.mother;}

    /**
     * Getter for this PersonModel object's spouse variable
     * @return  the spouse of this PersonModel
     */
    public PersonModel getSpouse() {return this.spouse;}

    /**
     * Getter for this PersonModel object's fatherId variable
     * @return  the fatherId of this PersonModel
     */
    public String getFatherId() {return this.fatherId;}

    /**
     * Getter for this PersonModel object's motherId variable
     * @return  the motherId of this PersonModel
     */
    public String getMotherId() {return this.motherId;}

    /**
     * Getter for this PersonModel object's spouseId variable
     * @return  the spouseId of this PersonModel
     */
    public String getSpouseId() {return this.spouseId;}

}
