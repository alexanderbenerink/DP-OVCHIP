package nl.hu.dp;

import nl.hu.dp.ovchip.data.*;
import nl.hu.dp.ovchip.data.persistence.AdresDAOPsql;
import nl.hu.dp.ovchip.data.persistence.OVChipkaartDAOPsql;
import nl.hu.dp.ovchip.data.persistence.ProductDAOPsql;
import nl.hu.dp.ovchip.data.persistence.ReizigerDAOPsql;
import nl.hu.dp.ovchip.domain.Adres;
import nl.hu.dp.ovchip.domain.OVChipkaart;
import nl.hu.dp.ovchip.domain.Product;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        ProductDAOPsql pdao = new ProductDAOPsql(getConnection());
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(getConnection());
        AdresDAOPsql adao = new AdresDAOPsql(getConnection());
        OVChipkaartDAOPsql odao = new OVChipkaartDAOPsql(getConnection());

        odao.setPdao(pdao);

        testReizigerDAO(rdao);
        testAdresDAO(adao, rdao);
        testOVChipkaartDAO(odao, rdao);
        testProductDAO(pdao, odao, rdao);

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
        System.out.println("\n---------- (P2) Test ReizigerDAO -------------");

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
        System.out.println("\n---------- (P3) Test AdresDAO -------------");

        // findAll(): Zoek alle addressen op
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende addressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // save(): Save adres
        String gbdatum = "1981-03-14";
        Reiziger willem = new Reiziger(76, "W", "", "Boers", java.sql.Date.valueOf(gbdatum));
//        Reiziger willem = rdao.findByGbdatum(gbdatum).get(0);
        Adres adres = new Adres(6, "1234AB", "56", "Stationsplein", "Schiedam", willem);
        willem.setAdres(adres);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        rdao.save(willem);
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
        rdao.delete(willem);
        System.out.print(", maar na AdresDAO.delete() is het: " + rdao.findByGbdatum(gbdatum) + "\n\n");

        // findByReiziger(): Zoek adres op reiziger
        Reiziger reiziger2 = rdao.findById(1);
        System.out.print("[Test] Het adres van: " + reiziger2.getNaam() + ", is na AdresDAO.findByReiziger(): " + adao.findByReiziger(reiziger2).getStraat() + " " + adao.findByReiziger(reiziger2).getHuisnummer() + "\n");
    }

    /**
     * P4. OVChipkaart DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de OVChipkaart DAO
     *
     * @throws SQLException
     */
    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- (P4) Test OVChipkaartDAO -------------\n");

        // save(): Sla een nieuwe OVChipkaart op
        Reiziger reiziger = new Reiziger(6, "J", "de", "Mol", Date.valueOf("1955-04-24"));
        Adres adres = new Adres(6, "1234AB", "56", "Johannes Vermeerlaan", "Apeldoorn", rdao.findById(reiziger.getId()));
        OVChipkaart ovchip = new OVChipkaart(12345, Date.valueOf("2022-10-14"), 1, 20, reiziger.getId());

        reiziger.setAdres(adres);
        reiziger.getOvchipkaarten().add(ovchip);

        rdao.save(reiziger);
        odao.save(ovchip);

        System.out.println("Nieuwe ovchipkaart na save(): \n" + odao.findByReiziger(reiziger).get(0) + "\n");

        // update(): Update een bestaand OVChipkaart
        System.out.println("Gegevens op de kaart voor update(): \n" + odao.findByReiziger(reiziger).get(0) + "\n");
        ovchip.setKlasse(2);
        odao.update(ovchip);

        System.out.println("Gegegevens op de kaart na update(): \n" + odao.findByReiziger(reiziger).get(0) + "\n");

        // findByReiziger(): Zoek een bestaand OVChipkaart op basis van een reiziger
        System.out.println("Zoek OV chipkaart op basis van: " + reiziger);
        System.out.println(odao.findByReiziger(reiziger).get(0) + "\n");

        // delete(): Verwijder een bestaand OVChipkaart
        System.out.println("Voor delete(): \n" + odao.findByReiziger(reiziger).get(0) + "\n");
        odao.delete(ovchip);
        rdao.delete(reiziger);
        System.out.println("Na delete(): \n" + odao.findByReiziger(reiziger) + "\n");

    }

    /**
     * P5. Product DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Product DAO
     *
     * @throws SQLException
     */
    private static void testProductDAO(ProductDAO pdao, OVChipkaartDAO odao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- (P5) Test ProductDAO -------------\n");

        // save(): Sla een nieuw product op
        Reiziger reiziger = new Reiziger(6, "J", "de", "Mol", Date.valueOf("1955-04-24"));
        OVChipkaart ovchip = new OVChipkaart(12345, Date.valueOf("2022-10-14"), 1, 20, reiziger.getId());
        Product product = new Product(7, "Test Product", "Testen persistentie veel-op-veel relaties", 0.0);

        rdao.save(reiziger);
        odao.save(ovchip);

        product.getOvChipkaarten().add(ovchip);
        reiziger.getOvchipkaarten().add(ovchip);

        pdao.save(product);

        System.out.println("Nieuw product na save(): \n" + pdao.findByOVChipkaart(ovchip) + "\n");

        // update(): Update een bestaand product
        System.out.println("Gegevens van het product voor update(): \n" + pdao.findByOVChipkaart(ovchip) + "\n");
        product.setPrijs(1.0);
        pdao.update(product);
        System.out.println("Gegevens van het product na update(): \n" + pdao.findByOVChipkaart(ovchip) + "\n");

        // findAll(): Vind alle producten
        System.out.println("Alle producten d.m.v. findAll():");
        pdao.findAll().forEach(System.out::println);

        // delete(): Verwijder een bestaand product
        System.out.println("\nHet product voor delete(): \n" + pdao.findByOVChipkaart(ovchip) + "\n");
        pdao.delete(product);
        System.out.println("Het product na delete(): \n" + pdao.findByOVChipkaart(ovchip) + "\n");

        rdao.delete(reiziger);
        odao.delete(ovchip);
    }
}