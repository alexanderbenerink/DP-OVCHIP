package nl.hu.dp.ovchip.data.persistence;

import nl.hu.dp.ovchip.data.ProductDAO;
import nl.hu.dp.ovchip.domain.OVChipkaart;
import nl.hu.dp.ovchip.domain.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private final Connection conn;
    private OVChipkaartDAOPsql odao = null;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
        this.odao = new OVChipkaartDAOPsql(conn);
    }

    @Override
    public boolean save(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?)");
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());

            pst.execute();
            pst.close();

            PreparedStatement pst1 = conn.prepareStatement("INSERT INTO ov_chipkaart_product VALUES (?, ?, ?, ?)");
            for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                pst1.setInt(1, ovChipkaart.getKaartNummer());
                pst1.setInt(2, product.getProduct_nummer());
                pst1.setString(3, "gekocht");
                pst1.setDate(4, Date.valueOf(LocalDate.now()));
                pst1.execute();
            }

            pst1.close();

            return true;
        } catch (SQLException sqlex) {
            System.out.println("Something went wrong when saving product: ");
            sqlex.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE product SET product_nummer = ?, naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            pst.setInt(5, product.getProduct_nummer());

            pst.execute();
            pst.close();

            PreparedStatement pst1 = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            pst1.setInt(1, product.getProduct_nummer());
            pst1.execute();
            pst1.close();

            PreparedStatement pst2 = conn.prepareStatement("INSERT INTO ov_chipkaart_product VALUES (?, ?, ?, ?)");
            for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                pst2.setInt(1, ovChipkaart.getKaartNummer());
                pst2.setInt(2, product.getProduct_nummer());
                pst2.setString(3, "gekocht");
                pst2.setDate(4, Date.valueOf(LocalDate.now()));
                pst2.execute();
            }

            pst2.close();

            return true;
        } catch (SQLException sqlex) {
            System.out.println("Something went wrong when updating product: ");
            sqlex.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            pst.setInt(1, product.getProduct_nummer());

            pst.execute();
            pst.close();

            PreparedStatement pst1 = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
            pst1.setInt(1, product.getProduct_nummer());

            pst1.execute();
            pst1.close();

            return true;
        } catch (SQLException sqlex) {
            System.out.println("Something went wrong with deleting the product: ");
            sqlex.printStackTrace();

            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        ArrayList<Product> alleProducten = new ArrayList<>();

        try {
            PreparedStatement pst = conn.prepareStatement("SELECT p.product_nummer, naam, beschrijving, prijs FROM ov_chipkaart_product ocp JOIN product p ON ocp.product_nummer = p.product_nummer WHERE ocp.kaart_nummer = ?");
            pst.setInt(1, ovChipkaart.getKaartNummer());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");
                Product product = new Product(nummer, naam, beschrijving, prijs);
                alleProducten.add(product);
            }

            rs.close();

            return alleProducten;
        } catch (SQLException sqlex) {
            System.out.println("Something went wrong with finding product by ovchipkaart: ");
            sqlex.printStackTrace();

            return null;
        }
    }

    @Override
    public List<Product> findAll() {
        ArrayList<Product> alleProducten = new ArrayList<>();

        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM product");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");
                Product product = new Product(nummer, naam, beschrijving, prijs);
                alleProducten.add(product);
            }

            return alleProducten;
        } catch (SQLException sqlex) {
            System.out.println("Something went wrong with finding all products: ");
            sqlex.printStackTrace();

            return null;
        }
    }

}
