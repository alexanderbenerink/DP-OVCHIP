package nl.hu.dp.ovchip.data;

import nl.hu.dp.ovchip.domain.Reiziger;

import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findById(int id);
    public List<Reiziger> findByGbdatum(String datum);
    public List<Reiziger> findAll();
}
