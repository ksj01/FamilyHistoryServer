package data_access;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Kevin on 2/15/2018.
 */


/**
 *
 * Generates authentication tokens, as well as verifies UserModel permissions
 */
public class authAccess {

    /**
     *
     * Generates new authentication tokens
     * @return  String containing an authentication token
     */
    public String generateAuth(String user_id) {
        String authToken = UUID.randomUUID().toString();
        storeAuth(user_id, authToken);
        return authToken;
    }

    /**
     * Stores a new authorization token in the database.
     * @param user_id   User ID to be associated with the authToken
     * @param authToken New authToken being stored in the database.
     */
    public void storeAuth(String user_id, String authToken) {
        String sql = "INSERT INTO auth_tokens " +
                "(value, " +
                "user_id) " +
                "VALUES " +
                "('" + authToken+
                "', '" + user_id +
                "');";
        database db = new database();
        db.openConnect();
        db.executeSql(sql);
        db.close();
    }

    /**
     *
     * Checks if an authentication token grants access to information associated with a particular username
     * @param authToken Authentication token to be verified
     * @return          Boolean indicating whether or not a UserModel was authenticated
     */
    public String checkAuth(String authToken) {
        database db = new database();
        db.openConnect();

        String checkAuthTokens = "SELECT user_id FROM `auth_tokens` WHERE value = '" + authToken + "';";
        ResultSet results = db.getSqlResults(checkAuthTokens);
        try {
            if (!results.isBeforeFirst()) {
                db.close();
                return null;
            }
            else {
                String userId = results.getString("user_id");
                db.close();
                return userId;
            }
        }catch (SQLException e) {
            System.out.println("An error occured.");
        }


        db.close();
        return null;
    }
}
