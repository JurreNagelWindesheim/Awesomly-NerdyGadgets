/* used link:https://www.java67.com/2013/02/how-to-connect-mysql-database-from-java.html */

import java.sql.Connection;     // represents the connection to the database
import java.sql.DriverManager;  //  obtains the connection to the database
import java.sql.SQLException;   // handles SQL errors between the Java application and the database
import java.sql.ResultSet;      // ResultSet and Statement model the data result sets and SQL statements
import java.sql.Statement;      // Idem

public class dbc {

    public static void getConnection() {
        String dbURL = "jdbc:mysql://localhost/nerdygadgets";
        String username = "root";
        String password = "";

        Connection dbCon = null;
        Statement stmt = null;
        ResultSet rs = null;

        String query ="select count(*) from routes";

        try {
            //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbURL, username, password);

            //getting PreparedStatment to execute query
            stmt = dbCon.prepareStatement(query);

            //Resultset returned by query
            rs = stmt.executeQuery(query);

            // Do something with the query
            while(rs.next()){
                int count = rs.getInt(1);
                System.out.println("count of routes : " + count);
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
        } finally {
            //close connection ,stmt and resultset here
            // ...
        }
    }

    public static void main(String[] args) {
        getConnection();
    }

}
