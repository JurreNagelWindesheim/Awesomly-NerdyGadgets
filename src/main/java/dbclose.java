/* URL used: https://mariadb.com/kb/en/java-connector-using-maven/ */
import java.sql.*;

public class dbclose {

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
