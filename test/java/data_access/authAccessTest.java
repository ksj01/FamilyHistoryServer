package data_access;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.UUID;

import services.ClearService;

import static org.junit.Assert.*;

/**
 * Created by kevin on 3/11/18.
 */
public class authAccessTest {
    @Before public void clear() {
        ClearService clear = new ClearService();
        clear.clear();
    }



    @Test
    public void generateAuth() throws Exception {
        authAccess authTest = new authAccess();
        String dummyID = "123";
        assertNotNull(authTest.generateAuth(dummyID));
    }
    @Test
    public void storeAuth() throws Exception {
        database db = new database();
        db.openConnect();
        authAccess authTest = new authAccess();
        String dummyID = "123";
        authTest.generateAuth(dummyID);
        String checkTrueAuthTokens = "SELECT user_id FROM `auth_tokens` WHERE user_id = '" + dummyID + "';";
        ResultSet results = db.getSqlResults(checkTrueAuthTokens);
        assertTrue(results.isBeforeFirst());
        String userId = results.getString("user_id");
        assertNotNull(userId);
        String badId = "456";
        String checkFalseAuthTokens = "SELECT user_id FROM `auth_tokens` WHERE user_id = '" + badId + "';";
        ResultSet falseResults = db.getSqlResults(checkFalseAuthTokens);
        assertFalse(falseResults.isBeforeFirst());
        db.close();
    }

    @Test
    public void checkAuth() throws Exception {
        String dummyId = UUID.randomUUID().toString();
        String dummyAuthToken = UUID.randomUUID().toString();
        authAccess auth = new authAccess();
        auth.storeAuth(dummyId, dummyAuthToken);
        String results = auth.checkAuth(dummyAuthToken);
        assertEquals(dummyId, results);
        String falseResults = auth.checkAuth(dummyId);
        assertNull(falseResults);
    }

}