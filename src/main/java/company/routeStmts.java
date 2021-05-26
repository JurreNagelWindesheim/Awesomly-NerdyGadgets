package company;

import org.jgrapht.GraphPath;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class routeStmts {

    public static ArrayList<String> getRoutes(Connection conn) {
        /* initiate statement */
        Statement stmt;

        try {
            /* create a Statement */
            stmt = conn.createStatement();

            /* execute query */
            try (ResultSet rs = stmt.executeQuery("SELECT routeId, routeData FROM routes WHERE routeDate LIKE '"+java.time.LocalDate.now()+"%' AND peopleId IS NULL")) {
            //    try (ResultSet rs = stmt.executeQuery("SELECT routeId, routeData FROM routes WHERE routeDate LIKE '2021-05-25%' AND peopleId IS NULL")) {

                /* group routes in array */
                ArrayList<String> routes = new ArrayList<>();

                while (rs.next()) {
                    routes.add(rs.getInt(1) + ", " + rs.getString(2));
                }
                return routes;

            }
        } catch (Exception e) {
            /* catch errors */
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void insertRoutesIntoDb(Connection conn, long duration, GraphPath perfectRoute) {
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
