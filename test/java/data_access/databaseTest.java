package data_access;

import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class databaseTest {
    @Before public void deleteTables() {
        database db = new database();
        db.openConnect();
        String authSql = "DROP TABLE \"main\".\"auth_tokens\"";
        String usersSql = "DROP TABLE \"main\".\"users\"";
        String eventsSql = "DROP TABLE \"main\".\"events\"";
        String personsSql = "DROP TABLE \"main\".\"persons\"";
        db.executeSql(authSql);
        db.executeSql(usersSql);
        db.executeSql(eventsSql);
        db.executeSql(personsSql);
        db.close();
    }

    @Test
    public void createTables() throws Exception {
        database db = new database();
        db.openConnect();
        db.createTables();
        db.close();
        db.openConnect();
        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        ResultSet results = db.getSqlResults(sql);
        String[] matches = {"auth_tokens", "events", "persons", "users"};
        int i = 0;
        while (results.next()) {
            assertEquals(results.getString(1), matches[i]);
            i++;
        }
    }

}