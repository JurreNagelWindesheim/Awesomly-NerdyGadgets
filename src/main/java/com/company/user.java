package com.company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class user {
    public static boolean loginCheck(Connection conn, String role, String password) {

        password = SecurePassword.makeSHA256Psswd(password);

        /* initiate statement */
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            ResultSet rs = stmt.executeQuery("SELECT role, hashedPsswd FROM backoffice_login WHERE role = '" + role + "' AND hashedPsswd = '" + password + "'");
            return rs.first();
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
            return false;
        }
    }
}
