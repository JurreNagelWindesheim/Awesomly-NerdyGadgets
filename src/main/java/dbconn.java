/* URL used: https://mariadb.com/kb/en/java-connector-using-maven/ */
import java.sql.*;

public class dbconn {

    public static Connection getConnection() {
        /* define connection */
        Connection conn;

        /* db connection info */
        String user = "root";
        String password = "";
        String db = "nerdygadgets";

        /* create connection for a server installed in localhost */
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/" + db, user, password);
            System.out.println("Connection to " + db + " was a success!");
            return conn;
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
            return null;
        }
    }
}
