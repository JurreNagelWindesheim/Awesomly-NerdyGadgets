package company;

import java.sql.*;
import java.util.ArrayList;

public class db {
    public static ArrayList<String> getAddresses(Connection conn) {
        // initiate statement
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            //try (ResultSet rs = stmt.executeQuery("SELECT DeliveryInstructions FROM invoices WHERE LastEditedWhen LIKE '"+java.time.LocalDate.now()+"%'")) {
            try (ResultSet rs = stmt.executeQuery("SELECT DeliveryInstructions FROM invoices WHERE LastEditedWhen LIKE '2021-05-07%'")) {

                /* group addresses in array */
                ArrayList<String> Addresses = new ArrayList<>();

                /* Start address Distribution centre (always the same) */
                if(rs.first()) {
                    Addresses.add("109+Dijkweg+Oudeschip");
                }

                /* loop through result */
                while (rs.next()) {
                    Addresses.add(rs.getString(1));
                    System.out.println("adress in db: "+ rs.getString(1));
                }
                System.out.println("array with addresses: " + Addresses);
                return Addresses;
            }
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
        return null;
    }

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
