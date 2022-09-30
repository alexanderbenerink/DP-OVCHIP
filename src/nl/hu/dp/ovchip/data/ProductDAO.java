package nl.hu.dp.ovchip.data;

import nl.hu.dp.ovchip.domain.OVChipkaart;
import nl.hu.dp.ovchip.domain.Product;

import java.util.List;

public interface ProductDAO {
    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    public List<Product> findAll();
}
