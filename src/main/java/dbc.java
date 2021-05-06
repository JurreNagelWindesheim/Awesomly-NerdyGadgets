import java.sql.*;

public class dbc {

    public static void getConnection() throws SQLException {
        //create connection for a server installed in localhost, with a user "root" with no password
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/", "root", null)) {
            // create a Statement
            try (Statement stmt = conn.createStatement()) {
                //execute query
                try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM routes")) {
                    //position result to first
                    rs.first();
                    while(rs.next()){
                        int count = rs.getInt(1);
                        System.out.println("count of routes : " + count);
                    }
                    System.out.println(rs.getString(1)); //result is 0
                }
            }
        }
    }
}
