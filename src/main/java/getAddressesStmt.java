/* URL used: https://mariadb.com/kb/en/java-connector-using-maven/ */
import java.sql.*;
import java.util.ArrayList;

public class getAddressesStmt {

    public static ArrayList<String> getRoutes(Connection conn) {
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
}
