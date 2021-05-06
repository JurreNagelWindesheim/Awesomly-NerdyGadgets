import java.sql.*;

public class dbc {

    public static void getConnection() throws SQLException {
        String user = "root";
        String password = null;
        String db = "nerdygadgets";

        //create connection for a server installed in localhost
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/"+db, user, password)) {

            // create a Statement
            try (Statement stmt = conn.createStatement()) {

                //execute query
                try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM cities")) {

                    //position result to first
                    rs.first();

                    // loop trough result
                    System.out.println("Aantal cities in db: "+rs.getString(1)); //result is "Hello World!"
                }
            }
        }
    }
}
