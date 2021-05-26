package company;

import java.sql.Connection;
import java.util.ArrayList;

public class test {
    public static int aantalPakketjes;
    public static int aantalPakketjesPerBus = 3;
//    public static int aantalBeschikbareBusjes;
    public static ArrayList<String> AddressenUitDatabase;


    public static void generateRoute() {

        //vul een Arraylist met de adressen van vandaag
        try (Connection conn = db.getConnection()) {
            AddressenUitDatabase = db.getAddresses(conn);
            assert conn != null;
            db.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }

        aantalPakketjes = AddressenUitDatabase.size();

        while (aantalPakketjes > aantalPakketjesPerBus) {
            aantalPakketjes -= aantalPakketjesPerBus;

        }



    }
}
