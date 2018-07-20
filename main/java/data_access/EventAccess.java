package data_access;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import models.*;

/**
 * Created by Kevin on 2/15/2018.
 */

/**
 * Handles the data access for retrieving and storing events in the database
 */
public class EventAccess {

    /**
     * Saves an EventModel to the database.
     * @param thisEvent    EventModel object containing all info needed to save an EventModel to the database
     * @return          Boolean indicating whether the operation was a success
     */
    public boolean setEvent(EventModel thisEvent) {
            String sql = "INSERT INTO events (" +
                    " event_id, " +
                    " descendant, " +
                    " person, " +
                    " latitude, " +
                    " longitude, " +
                    " country, " +
                    " city, " +
                    " event_type, " +
                    " year) " +
                    "VALUES " +
                    " ( " +
                    " '" + thisEvent.getEventId() + "', " +
                    " '" + thisEvent.getDescendant() + "', " +
                    " '" + thisEvent.getPersonId() + "', " +
                    " " + thisEvent.getLatitude() + ", " +
                    " " + thisEvent.getLongitude() + "," +
                    " '" + thisEvent.getCountry() + "'," +
                    " '" + thisEvent.getCity() + "'," +
                    " '" + thisEvent.getEventType() + "'," +
                    " " + thisEvent.getYear() +
                    ");";
            database db = new database();
            db.openConnect();
            db.executeSql(sql);
            db.close();
        return true;
    }

    /**
     * Retrieves an EventModel from the database
     * @param eventID   ID for the EventModel of which information is being accessed
     * @return          JSON object containing all information about the requested EventModel
     */
    public String getEvent(String eventID, String userId) {
        database db = new database();
        db.openConnect();

        String verifySql = "SELECT * FROM `events` WHERE event_id = '" + eventID + "';";
        String eventSql = "SELECT * FROM `events` WHERE event_id = '" + eventID + "' AND descendant = '" + userId + "';";
        ResultSet results = db.getSqlResults(eventSql);
        ResultSet verifyResults = db.getSqlResults(verifySql);

        try {
            if (!verifyResults.isBeforeFirst()) {
                db.close();
                return "no such EventModel";
            }
            if (!results.isBeforeFirst()) {
                db.close();
                return "does not belong";
            }
            else {

                StringBuilder eventJson = new StringBuilder("{\n");
                eventJson.append("\"descendant\":\"" + results.getString("descendant") + "\",\n");
                eventJson.append("\"eventID\":\"" + results.getString("event_id") + "\",\n");
                eventJson.append("\"personID\":\"" + results.getString("person") + "\",\n");
                eventJson.append("\"latitude\":\"" + results.getString("latitude") + "\",\n");
                eventJson.append("\"longitude\":\"" + results.getString("longitude") + "\",\n");
                eventJson.append("\"country\":\"" + results.getString("country") + "\",\n");
                eventJson.append("\"city\":\"" + results.getString("city") + "\",\n");
                eventJson.append("\"eventType\":\"" + results.getString("event_type") + "\",\n");
                eventJson.append("\"year\":\"" + results.getString("year") + "\"\n");
                eventJson.append("}");
                db.close();
                return eventJson.toString();
            }
        }catch (SQLException e) {
            System.out.println("An error occured.");
        }


        db.close();
        return null;
    }

    /**
     * Retrieves all events associated with a particular username
     * @param userId  username for which information we are interested in
     * @return          JSON object containing all events associated with the given username
     */
    public String getAllEvents(String userId) {
        database db = new database();
        db.openConnect();

        String eventSql = "SELECT * FROM `events` WHERE descendant = '" + userId + "';";
        ResultSet results = db.getSqlResults(eventSql);

        try {
            if (!results.isBeforeFirst()) {
                db.close();
                String error = "{ \"message\": \"This user has no events registered to them.\" }";
                return error;
            } else {

                StringBuilder eventJson = new StringBuilder("{\n");
                eventJson.append("\"data\":[");
                while (results.next()) {
                    eventJson.append("\n{\n");
                    eventJson.append("\"descendant\":\"" + results.getString("descendant") + "\",\n");
                    eventJson.append("\"eventID\":\"" + results.getString("event_id") + "\",\n");
                    eventJson.append("\"personID\":\"" + results.getString("person") + "\",\n");
                    eventJson.append("\"latitude\":\"" + results.getString("latitude") + "\",\n");
                    eventJson.append("\"longitude\":\"" + results.getString("longitude") + "\",\n");
                    eventJson.append("\"country\":\"" + results.getString("country") + "\",\n");
                    eventJson.append("\"city\":\"" + results.getString("city") + "\",\n");
                    eventJson.append("\"eventType\":\"" + results.getString("event_type") + "\",\n");
                    eventJson.append("\"year\":\"" + results.getString("year") + "\"\n");
                    eventJson.append("},");
                }
                eventJson.deleteCharAt(eventJson.length() - 1);
                eventJson.append("]\n}");
                db.close();
                return eventJson.toString();
            }
        } catch (SQLException e) {
            System.out.println("An error occured.");
        }
        db.close();
        return null;
    }

    /**
     * Retrieves the number of events assigned to a given user.
     * @param user  User for which we want the event count
     * @return      Int indicating the number of events associated with the requested user.
     *              Returns -1 if something went wrong.
     */
    public int getRowCount(String user) {
        database db = new database();
        db.openConnect();

        String eventSql = "SELECT COUNT(*) FROM `events` WHERE descendant = '" + user + "';";
        ResultSet results = db.getSqlResults(eventSql);
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
