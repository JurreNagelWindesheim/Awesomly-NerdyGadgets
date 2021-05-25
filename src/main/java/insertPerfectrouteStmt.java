import org.jgrapht.GraphPath;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class insertPerfectrouteStmt {

    public static void insertPerfectroute(Connection conn, long duration,GraphPath perfectRoute) throws NoSuchAlgorithmException {
        /* initiate statement */
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            ResultSet rs = stmt.executeQuery("INSERT INTO routes (duration, routeData) VALUES ('" + duration + "', '" + perfectRoute + "'" + ")");
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
    }
}
