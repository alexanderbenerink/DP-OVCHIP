package nl.hu.dp;

import nl.hu.dp.ovchip.data.ReizigerDAO;
import nl.hu.dp.ovchip.data.ReizigerDAOPsql;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.sql.*;
import java.util.List;

public class OvchipApplication {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(getConnection());
        testReizigerDAO(rdao);
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
}