package company;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SecurePassword {

    public static String makeSHA256Psswd (String password) {
        String securePassword = get_SHA_256_SecurePassword(password);

        return securePassword;
    }

    private static String get_SHA_256_SecurePassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static void insertPsswd(Connection conn, String role, String password) {

        /*
        * This function makes a new user
        * This function is currently not in use, but can be used in the future
        * Currently as of 27-05-2021 there is only one administrator account
        */

        password = SecurePassword.makeSHA256Psswd(password);

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

