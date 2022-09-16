package nl.hu.dp.ovchip.data;

import nl.hu.dp.ovchip.domain.OVChipkaart;
import nl.hu.dp.ovchip.domain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovChipkaart);
    public boolean update(OVChipkaart ovChipkaart);
    public boolean delete(OVChipkaart ovChipkaart);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
}
