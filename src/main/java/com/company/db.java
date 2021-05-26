package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db {
    public static Connection getConnection() {
        /* define connection */
        Connection conn;

        /* db connection info */
        String user = "root";
        String password = "";
        String db = "nerdygadgets";

        /* create connection for a server installed in localhost */
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/"+ db, user, password);
            System.out.println("Connection to " + db + " was a success!");
            return conn;
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection conn) {

        try {
            /* block used to close resources */
            conn.close();
            conn = null;
            /* Print message everything closed */
            System.out.println("DB connection close success, goodbye!");
            System.out.println("-------------------------------------------------");
        } catch (SQLException se) {
            /* do nothing */
        } try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
            /* Print message everything closed */
            System.out.println("DB connection close success, goodbye!");
            System.out.println("-------------------------------------------------");
        }
    }
}
