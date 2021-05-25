import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class updateRouteStmt {
    public static void updatePeopleId(Connection conn, int peopleId, int routeId) {
        /* initiate statement */
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            ResultSet rs = stmt.executeQuery("UPDATE routes SET peopleId = " + peopleId + " WHERE routeId = " + routeId);
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
    }
}
