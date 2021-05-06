/* URL used: https://mariadb.com/kb/en/java-connector-using-maven/ */
import java.sql.*;

public class dbc {

    public static void getConnection() throws SQLException {
        // define conection and statement
        Connection conn = null;
        Statement stmt = null;

        // db connection info
        String user = "root";
        String password = null;
        String db = "nerdygadgets";

        //create connection for a server installed in localhost
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/" + db, user, password);

            // create a Statement
            stmt = conn.createStatement();

            //execute query
            try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM cities")) {
                //position result to first
                rs.first();

                // print result
                System.out.println("Aantal cities in db: " + rs.getString(1));
            }
        } catch (Exception e) {
            // catch errors
            System.err.println(e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                // do nothing
            } try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        } //end try
        // Print message everything closed
        System.out.println("SQL success, goodbye!");
    }
}
