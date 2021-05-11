/* URL used: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/ */

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class hashPsswdStmt {

    public static void insertPsswd(Connection conn, String role, String password) throws NoSuchAlgorithmException {

        password = hashPsswd.makeSHA256Psswd(password);

        /* initiate statement */
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            ResultSet rs = stmt.executeQuery("INSERT INTO backoffice_login (role, hashedPsswd) VALUES ('" + role + "', '" + password + "')");
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
    }

}
