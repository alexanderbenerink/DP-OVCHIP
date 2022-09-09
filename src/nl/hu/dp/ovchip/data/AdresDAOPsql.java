package nl.hu.dp.ovchip.data;

import nl.hu.dp.ovchip.domain.Adres;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    Connection conn;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public boolean save(Adres adres) {
        try {
            // save address using a prepareStatement
            String q = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            ResultSet rs = pst.executeQuery();

            // close connections
            rs.close();
            pst.close();

            return true;
        }
        catch (SQLException sqlex) {
            String emptyResults = "No results were returned by the query.";

            if (!sqlex.getMessage().equals(emptyResults)) {
                System.err.println("An error occurred while saving traveler: " + sqlex.getMessage());
            }
        }

        return false;
    }

    public boolean update(Adres adres) {
        try {
            // find address by id and update using a prepareStatement
            String q = "UPDATE adres SET adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            pst.setInt(7, adres.getReiziger().getId());
            ResultSet rs = pst.executeQuery();

            // close connection
            rs.close();
            pst.close();

            return true;
        }
        catch (SQLException sqlex) {
            String emptyResults = "No results were returned by the query.";

            if (!sqlex.getMessage().equals(emptyResults)) {
                System.err.println("An error occurred while trying to update address: " + sqlex.getMessage());
            }
        }
        return false;
    }

    public boolean delete(Adres adres) {
        try {
            // find traveler by id and delete them using a prepareStatement
            String q = "DELETE FROM adres WHERE adres_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, adres.getId());
            ResultSet rs = pst.executeQuery();

            // close connection
            rs.close();
            pst.close();

            return true;
        }
        catch (SQLException sqlex) {
            String emptyResults = "No results were returned by the query.";

            if (!sqlex.getMessage().equals(emptyResults)) {
                System.err.println("An error occurred while trying to delete address: " + sqlex.getMessage());
            }
        }
        return false;
    }

    public Adres findByReiziger(Reiziger reiziger) {
        try {
            // find traveler by id using a prepareStatement
            String q = "SELECT * FROM adres WHERE reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            Adres adres = null;

            // add the address
            while (rs.next())
            {
                int adresId = rs.getInt(1);
                String postcode = rs.getString(2);
                String huisnummer = rs.getString(3);
                String straat = rs.getString(4);
                String woonplaats = rs.getString(5);
//                int reizigerId = rs.getInt(6);

                adres = new Adres(adresId, postcode, huisnummer, straat, woonplaats);
            }

            // close connections
            rs.close();
            pst.close();

            return adres;
        }
        catch (SQLException sqlex) {
            System.err.println("An error occurred while searching by id: " + sqlex.getMessage());
        }
        return null;
    }

    public Adres findById(int id) {
        try {
            // find traveler by id using a prepareStatement
            String q = "SELECT * FROM adres WHERE reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            Adres adres = null;

            // add the address
            while (rs.next())
            {
                int adresId = rs.getInt(1);
                String postcode = rs.getString(2);
                String huisnummer = rs.getString(3);
                String straat = rs.getString(4);
                String woonplaats = rs.getString(5);
//                int reizigerId = rs.getInt(6);

                adres = new Adres(adresId, postcode, huisnummer, straat, woonplaats);
            }

            // close connections
            rs.close();
            pst.close();

            return adres;
        }
        catch (SQLException sqlex) {
            System.err.println("An error occurred while searching by id: " + sqlex.getMessage());
        }
        return null;
    }

    public List<Adres> findAll() {
        try {
            // use a prepareStatements to query all adressess
            String q = "SELECT * FROM adres";
            PreparedStatement pst = conn.prepareStatement(q);
            ResultSet rs = pst.executeQuery();

            List<Adres> adressen = new ArrayList<>();

            // add the adres(ses) to a list
            while (rs.next())
            {
                int id = rs.getInt(1);
                String postcode = rs.getString(2);
                String huisnummer = rs.getString(3);
                String straat = rs.getString(4);
                String woonplaats = rs.getString(5);
//                int reizigerid = rs.getInt(6);

                Adres adres = new Adres(id, postcode, huisnummer, straat, woonplaats);
                adressen.add(adres);
            }

            // close connections
            rs.close();
            pst.close();

            return adressen;
        }
        catch (SQLException sqlex) {
            System.err.println("Adresses couldn't be fetched: " + sqlex.getMessage());
        }
        return null;
    }

}
