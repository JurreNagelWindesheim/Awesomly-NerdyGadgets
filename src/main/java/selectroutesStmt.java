import java.sql.*;
import java.util.ArrayList;

public class selectroutesStmt {

    public static ArrayList<String> getRoutes(Connection conn) {
        // initiate statement
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            try (ResultSet rs = stmt.executeQuery("SELECT routeId, routeData FROM routes WHERE routeDate LIKE '2021-05-12%'")) {

                /* group routes in array */
                ArrayList<String> routes = new ArrayList<>();

                while (rs.next()) {
                    routes.add(rs.getInt(1) + " " + rs.getString(2));
                    System.out.println("route in db: "+ rs.getString(2));
                }
                return routes;
            }
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
        return null;
    }
}
