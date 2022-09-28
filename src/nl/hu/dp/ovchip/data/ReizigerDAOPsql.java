package nl.hu.dp.ovchip.data;

import nl.hu.dp.ovchip.data.ReizigerDAO;
import nl.hu.dp.ovchip.domain.OVChipkaart;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private final Connection conn;
    private final OVChipkaartDAOPsql odao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
        this.odao = new OVChipkaartDAOPsql(conn);
    }

    @Override
    public boolean save(Reiziger reiziger) {
        try {
            // save traveler using a prepareStatement
            String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            ResultSet rs = pst.executeQuery();

            // close connections
            rs.close();
            pst.close();

            // save travelers ovchip card(s)
            for (OVChipkaart ovchip : reiziger.getOvchipkaarten()) {
                odao.save(ovchip);
            }

            return true;
        }
        catch (SQLException sqlex) {
            String emptyResults = "No results were returned by the query.";

            if (!sqlex.getMessage().equals(emptyResults)) {
                System.err.println("An error occurred while saving traveler: ");
                sqlex.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            // find traveler by id and update using a prepareStatement
            String q = "UPDATE reiziger SET reiziger_id = ?, voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.setInt(6, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            // close connection
            rs.close();
            pst.close();

            // update travelers ovchip card(s)
            for (OVChipkaart ovchip : reiziger.getOvchipkaarten()) {
                odao.update(ovchip);
            }

            return true;
        }
        catch (SQLException sqlex) {
            String emptyResults = "No results were returned by the query.";

            if (!sqlex.getMessage().equals(emptyResults)) {
                System.err.println("An error occurred while trying to update traveler: ");
                sqlex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            // delete travelers ovchip card(s) before deleting traveler
            // otherwise you'll get an sql error telling you that traveler
            // still has references in the ov_chipkaart table
            for (OVChipkaart ovchip : reiziger.getOvchipkaarten()) {
                odao.delete(ovchip);
            }

            // find traveler by id and delete them using a prepareStatement
            String q = "DELETE FROM reiziger WHERE reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            // close connection
            rs.close();
            pst.close();

            return true;
        }
        catch (SQLException sqlex) {
            String emptyResults = "No results were returned by the query.";

            if (!sqlex.getMessage().equals(emptyResults)) {
                System.err.println("An error occurred while trying to delete traveler: ");
                sqlex.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) {
        try {
            // find traveler by id using a prepareStatement
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            Reiziger reiziger = null;

            // add the traveler
            while (rs.next())
            {
                int travelerId = rs.getInt(1);
                String firstLetters = rs.getString(2);
                String affix = rs.getString(3);
                String lastName = rs.getString(4);
                Date dob = rs.getDate(5);

                reiziger = new Reiziger(travelerId, firstLetters, affix, lastName, dob);
            }

            // close connections
            rs.close();
            pst.close();

            return reiziger;
        }
        catch (SQLException sqlex) {
            System.err.println("An error occurred while searching by id: ");
            sqlex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            // find traveler by id using a prepareStatement
            String q = "SELECT * FROM reiziger WHERE geboortedatum = ?;";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();


            // add the traveler(s) to a list
            while (rs.next())
            {
                int travelerId = rs.getInt(1);
                String firstLetters = rs.getString(2);
                String affix = rs.getString(3);
                String lastName = rs.getString(4);
                Date dob = rs.getDate(5);
                Reiziger reiziger = new Reiziger(travelerId, firstLetters, affix, lastName, dob);
                reizigers.add(reiziger);
            }

            // close connections
            rs.close();
            pst.close();

            return reizigers;
        }
        catch (SQLException sqlex) {
            System.err.println("An error occurred while searching by birthdate: ");
            sqlex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            // use a prepareStatements to query all travelers
            String q = "SELECT * FROM reiziger";
            PreparedStatement pst = conn.prepareStatement(q);
            ResultSet rs = pst.executeQuery();


            // add the traveler(s) to a list
            while (rs.next())
            {
                int id = rs.getInt(1);
                String firstLetters = rs.getString(2);
                String affix = rs.getString(3);
                String lastName = rs.getString(4);
                Date dob = rs.getDate(5);

                Reiziger reiziger = new Reiziger(id, firstLetters, affix, lastName, dob);
                reizigers.add(reiziger);
            }

            // close connections
            rs.close();
            pst.close();

            return reizigers;
        }
        catch (SQLException sqlex) {
            System.err.println("Travelers couldn't be fetched: ");
            sqlex.printStackTrace();
        }
        return null;
    }
}
