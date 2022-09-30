package nl.hu.dp.ovchip.data.persistence;

import nl.hu.dp.ovchip.data.OVChipkaartDAO;
import nl.hu.dp.ovchip.domain.OVChipkaart;
import nl.hu.dp.ovchip.domain.Product;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection conn;
    private ProductDAOPsql pdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
//        this.pdao = new ProductDAOPsql(conn);
    }

    public void setPdao(ProductDAOPsql pdao) {
        this.pdao = pdao;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ov_chipkaart VALUES (?, ?, ?, ?, ?)");
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setDouble(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReizigerId());

            pst.execute();
            pst.close();
            return true;
        }
        catch (SQLException sqlex) {
            System.out.println("Saving failed: ");
            sqlex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE ov_chipkaart SET kaart_nummer = ?, geldig_tot = ?, klasse = ?, saldo = ? WHERE reiziger_id = ?");
            pst.setInt(1, ovChipkaart.getKaartNummer());
            pst.setDate(2, ovChipkaart.getGeldigTot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setDouble(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReizigerId());

            pst.execute();
            pst.close();

            for (Product product : pdao.findByOVChipkaart(ovChipkaart)) {
                if (ovChipkaart.getProduct().contains(product)) {
                    pdao.update(product);
                }
            }

            return true;
        }
        catch (SQLException sqlex) {
            System.out.println("Couldn't update ovchipcard info: ");
            sqlex.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");
            pst.setInt(1, ovChipkaart.getKaartNummer());

            pst.execute();
            pst.close();

            PreparedStatement pst1 = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            pst1.setInt(1, ovChipkaart.getKaartNummer());

            pst1.execute();
            pst1.close();

            return true;
        }
        catch (SQLException sqlex) {
            System.out.println("Couldn't remove ovchipcard: ");
            sqlex.printStackTrace();

            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?");
            pst.setInt(1, reiziger.getId());
            ResultSet rs = pst.executeQuery();

            List<OVChipkaart> ovChipkaarten = new ArrayList<>();

            while (rs.next()) {
                int kaartNummer = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                int reizigerId = rs.getInt("reiziger_id");

                ovChipkaarten.add(new OVChipkaart(kaartNummer, geldigTot, klasse, saldo, reizigerId));
            }

            rs.close();

            return ovChipkaarten;
        }
        catch (SQLException sqlex) {
            System.out.println("Couldn't find the ovchipcard: ");
            sqlex.printStackTrace();

            return null;
        }
    }
}
