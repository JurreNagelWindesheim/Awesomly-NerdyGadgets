import java.sql.*;

public class dbc {

    public static void getConnection() throws SQLException {
        String user = "root";
        String password = null;
        String db = "nerdygadgets";

        //create connection for a server installed in localhost
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/", user, password)) {

            // create a Statement
            try (Statement stmt = conn.createStatement()) {

                //execute query
                try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM routes")) {

                    //position result to first
                    rs.first();

                    // loop trough result
                    while(rs.next()){
                        int count = rs.getInt(1);
                        System.out.println("count of routes : " + count);
                    }
                }
            }
        }
    }
}
