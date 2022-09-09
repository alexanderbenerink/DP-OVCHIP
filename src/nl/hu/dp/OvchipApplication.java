package nl.hu.dp;

import nl.hu.dp.ovchip.data.AdresDAO;
import nl.hu.dp.ovchip.data.AdresDAOPsql;
import nl.hu.dp.ovchip.data.ReizigerDAO;
import nl.hu.dp.ovchip.data.ReizigerDAOPsql;
import nl.hu.dp.ovchip.domain.Adres;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.sql.*;
import java.util.List;

public class OvchipApplication {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(getConnection());
        AdresDAOPsql adao = new AdresDAOPsql(getConnection());

//        testReizigerDAO(rdao);
        testAdresDAO(adao, rdao);

        closeConnection();
    }

    private static Connection getConnection() {
        try {
            // set up postgresql jdbc connection
            String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=root";
            connection = DriverManager.getConnection(url);
            return connection;
        }
        catch (SQLException sqlex) {
            System.err.println("Something went wrong with psql: " + sqlex.getMessage());
            return null;
        }
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        // Zoek reiziger op ID
        int id = 1;
        System.out.print("[Test] ReizigerDAO.findById() geeft de volgende reiziger: " + "\n");
        System.out.println(rdao.findById(id) + "\n");

        // Update reiziger
        Reiziger willem = new Reiziger(76, "W", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(willem);
        willem.setAchternaam("Wortel");
        rdao.update(willem);
        System.out.println("[Test] ReizigerDAO.update() heeft de volgende reizigers achternaam veranderd:\n" + willem + "\n");

        // Zoek reiziger op geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGbdatum geeft de volgende reiziger(s):\n" + rdao.findByGbdatum(gbdatum) + "\n");

        // Verwijder reiziger
        System.out.println("[Test] ReizigerDAO.delete() verwijdert de volgende reizigers:\n" + willem + "\n" + sietske);
        rdao.delete(willem);
        rdao.delete(sietske);
        if (rdao.findById(76) == null && rdao.findById(77) == null) {
            System.out.println("Gebruikers zijn verwijderd");
        }
        System.out.println();
    }

    /**
     * P3. Adres DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Adres DAO
     *
     * @throws SQLException
     */
    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // findAll(): Zoek alle addressen op
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende addressen:");
        for (Adres a : adressen) {
            // // Reiziger hier is null omdat ik geen reiziger mee kan geven in AdresDAOPsql aan de Adres constructor..
            // // Dus is het voor nu gecomment.

//            Adres adres = adao.findById(a.getId());
//            Reiziger reiziger = adres.getReiziger();
//            System.out.println("Reiziger {" + reiziger + ", Adres {" + a + "}}");
            System.out.println(a);
        }
        System.out.println();

        // save(): Save adres
        String gbdatum = "1981-03-14";
//        Reiziger willem = new Reiziger(76, "W", "", "Boers", java.sql.Date.valueOf(gbdatum));
        Reiziger willem = rdao.findByGbdatum(gbdatum).get(0);
        Adres adres = new Adres(6, "1234AB", "56", "Stationsplein", "Schiedam", willem);
        willem.setAdres(adres);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
//        rdao.save(willem);
        adao.save(adres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // update(): Update adres
        System.out.print("[Test] Eerst is het huisnummer van Willem: " + willem.getAdres().getHuisnummer());
        adres.setHuisnummer("50");
        adao.update(adres);
        System.out.print(", na AdresDAO.update() is het huisnummer: " + willem.getAdres().getHuisnummer() + "\n\n");

        // delete(): Delete adres
        Reiziger reiziger = rdao.findByGbdatum(gbdatum).get(0);
        System.out.print("[Test] Eerst is Willems adres: " + willem.getAdres().getPostcode() + " " + willem.getAdres().getWoonplaats());
        adao.delete(adres);
        reiziger = rdao.findByGbdatum(gbdatum).get(0);
        System.out.print(", maar na AdresDAO.delete() is het: " + reiziger.getAdres() + "\n\n");

        // findByReiziger(): Zoek adres op reiziger
        Reiziger reiziger2 = rdao.findById(1);
        System.out.print("[Test] Het adres van: " + reiziger2.getNaam() + ", is na AdresDAO.findByReiziger(): " + adao.findByReiziger(reiziger2).getStraat() + " " + adao.findByReiziger(reiziger2).getHuisnummer());
    }
}