import org.jgrapht.GraphPath;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class insertPerfectrouteStmt {

    public static void insertPerfectroute(Connection conn, int driverId, long duration,GraphPath perfectRoute) throws NoSuchAlgorithmException {
        /* initiate statement */
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            ResultSet rs = stmt.executeQuery("INSERT INTO routes (peopleId, duration, routeData) VALUES ('" + driverId + "', '" + duration + "', '" + perfectRoute + "'" + ")");
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
    }
}