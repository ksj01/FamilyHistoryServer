package data_access;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Handles the data access for retrieving and storing people in the database
 */
public class PersonAccess {

    /**
     * Saves a PersonModel to the database.
     * @param person    PersonModel object containing all info needed to save a PersonModel to the database
     * @return          Boolean indicating whether the operation was a success
     */
    public boolean setPerson(PersonModel person) {
        String mother = null;
        String father = null;
        String spouse = null;
        if (person.getMother() != null) {
            mother = person.getMother().getPersonId();
        }
        else if (person.getMotherId() != null) {
            mother = person.getMotherId();
        }
        if (person.getFather() != null) {
            father = person.getFather().getPersonId();
        }
        else if (person.getFatherId() != null) {
            father = person.getFatherId();
        }
        if (person.getSpouse() != null) {
            spouse = person.getSpouse().getPersonId();
        }
        else if (person.getSpouseId() != null) {
            spouse = person.getSpouseId();
        }
        String sql = "INSERT INTO persons " +
                "(person_id, " +
                "descendant, " +
                "first_name, " +
                "last_name, " +
                "gender, " +
                "father, " +
                "mother, " +
                "spouse) " +
                "VALUES " +
                "('" + person.getPersonId() +
                "', '" + person.getDescendent() +
                "', '" + person.getFirstName() +
                "', '" + person.getLastName() +
                "', '" + person.getGender() +
                "', '" + father +
                "', '" + mother +
                "', '" + spouse +
                "');";
        database db = new database();
        db.openConnect();
        db.executeSql(sql);
        db.close();
        return true;
    }

    /**
     * Retrieves a PersonModel from the database
     * @param personID   ID for the PersonModel of which information is being accessed
     * @return          JSON object containing all information about the requested PersonModel
     */
    public String getPerson(String personID, String userId) {
        database db = new database();
        db.openConnect();
        String verifySql = "SELECT * FROM `persons` WHERE person_id = '" + personID + "';";
        String personSql = "SELECT * FROM `persons` WHERE person_id = '" + personID + "' AND descendant = '" + userId + "';";
        ResultSet verifyResults = db.getSqlResults(verifySql);
        ResultSet results = db.getSqlResults(personSql);

        try {
            if (!verifyResults.isBeforeFirst()) {
                db.close();
                return "no such PersonModel";
            }
            if (!results.isBeforeFirst()) {
                db.close();
                return "does not belong";
            }
            else {
                StringBuilder personJson = new StringBuilder("{\n");
                personJson.append("\"descendant\":\"" + results.getString("descendant") + "\",\n");
                personJson.append("\"personID\":\"" + results.getString("person_id") + "\",\n");
                personJson.append("\"firstName\":\"" + results.getString("first_name") + "\",\n");
                personJson.append("\"lastName\":\"" + results.getString("last_name") + "\",\n");
                personJson.append("\"gender\":\"" + results.getString("gender") + "\",\n");
                personJson.append("\"father\":\"" + results.getString("father") + "\",\n");
                personJson.append("\"mother\":\"" + results.getString("mother") + "\",\n");
                personJson.append("\"spouse\":\"" + results.getString("spouse") + "\"\n");
                personJson.append("}");
                db.close();
                return personJson.toString();
            }
        }catch (SQLException e) {
            System.out.println("An error occured.");
        }


        db.close();
        return null;
    }


    /**
     * Retrieves a PersonModel from the database
     * @param personID   ID for the EventModel of which information is being accessed
     * @return          JSON object containing all information about the requested EventModel
     */
    public HashMap getPerson(String personID) {
        database db = new database();
        db.openConnect();
        String verifySql = "SELECT * FROM `persons` WHERE person_id = '" + personID + "';";
        ResultSet verifyResults = db.getSqlResults(verifySql);

        try {
            if (!verifyResults.isBeforeFirst()) {
                db.close();
                return null;
            }
            else {
                HashMap<String, String> hash = new HashMap<>();

                hash.put("descendant", verifyResults.getString("descendant"));
                hash.put("person_id", verifyResults.getString("person_id"));
                hash.put("firstName", verifyResults.getString("first_name"));
                hash.put("lastName", verifyResults.getString("last_name"));
                hash.put("gender", verifyResults.getString("gender"));
                hash.put("father", verifyResults.getString("father"));
                hash.put("mother", verifyResults.getString("mother"));
                hash.put("spouse", verifyResults.getString("spouse"));
                db.close();
                return hash;
            }
        }catch (SQLException e) {
            System.out.println("An error occured.");
        }

        db.close();
        return null;
    }
    /**
     * Retrieves all persons associated with a particular username
     * @param userId  username for which information we are interested in
     * @return          JSON object containing all persons associated with the given username
     */
    public String getAllPersons(String userId) {
        database db = new database();
        db.openConnect();

        String personSql = "SELECT * FROM `persons` WHERE descendant = '" + userId + "';";
        ResultSet results = db.getSqlResults(personSql);

        try {
            if (!results.isBeforeFirst()) {
                db.close();
                return null;
            } else {

                StringBuilder personJson = new StringBuilder("{\n");
                personJson.append("\"data\":[");
                while (results.next()) {
                    personJson.append("\n{\n");
                    personJson.append("\"descendant\":\"" + results.getString("descendant") + "\",\n");
                    personJson.append("\"personID\":\"" + results.getString("person_id") + "\",\n");
                    personJson.append("\"firstName\":\"" + results.getString("first_name") + "\",\n");
                    personJson.append("\"lastName\":\"" + results.getString("last_name") + "\",\n");
                    personJson.append("\"gender\":\"" + results.getString("gender") + "\",\n");
                    personJson.append("\"father\":\"" + results.getString("father") + "\",\n");
                    personJson.append("\"mother\":\"" + results.getString("mother") + "\",\n");
                    personJson.append("\"spouse\":\"" + results.getString("spouse") + "\"\n");
                    personJson.append("},");
                }
                personJson.deleteCharAt(personJson.length() - 1);
                personJson.append("]\n}");
                db.close();
                return personJson.toString();
            }
        } catch (SQLException e) {
            System.out.println("An error occured.");
        }


        db.close();
        return null;
    }

    /**
     * Retrieves the number of persons assigned to a given user.
     * @param user  User for which we want the person count
     * @return      Int indicating the number of persons associated with the requested user.
     *              Returns -1 if something went wrong.
     */
    public int getRowCount(String user) {
        database db = new database();
        db.openConnect();

        String personSql = "SELECT COUNT(*) FROM `persons` WHERE descendant = '" + user + "';";
        ResultSet results = db.getSqlResults(personSql);
        try {
            if (!results.isBeforeFirst()) {
                db.close();
                return -1;
            } else {
                int count = results.getInt(1);
                db.close();
                return count;
            }
        } catch (SQLException e) {
            System.out.println("An error occured.");
        }
        db.close();
        return -1;
    }
}
