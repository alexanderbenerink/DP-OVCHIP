package nl.hu.dp.ovchip.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovchipkaarten = new ArrayList<>();

    public Reiziger(int id, String vl, String tv, String an, Date gb) {
        this.id = id;
        this.voorletters = vl;
        this.tussenvoegsel = tv;
        this.achternaam = an;
        this.geboortedatum = gb;
        this.adres = null;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return this.voorletters;
    }

    public String getTussenvoegsel() {
        return this.tussenvoegsel;
    }

    public String getAchternaam() {
        return this.achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return this.geboortedatum;
    }

    public String getNaam() {
        return this.voorletters + " " + this.tussenvoegsel + " " + this.achternaam;
    }

    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOvchipkaarten() {
        return ovchipkaarten;
    }

    public void setOvchipkaarten(List<OVChipkaart> ovchipkaarten) {
        this.ovchipkaarten = ovchipkaarten;
    }

    @Override
    public String toString() {
        return "#" + this.id + " " + getNaam() + ", geb." + this.geboortedatum + " ";
    }
}
