package nl.hu.dp.ovchip.domain;

public class Adres {
    private int id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private Reiziger reiziger;

    public Adres(int id, String pc, String hn, String st, String wp) {
        this.id = id;
        this.postcode = pc;
        this.huisnummer = hn;
        this.straat = st;
        this.woonplaats = wp;
    }

    public Adres(int id, String pc, String hn, String st, String wp, Reiziger rg) {
        this.id = id;
        this.postcode = pc;
        this.huisnummer = hn;
        this.straat = st;
        this.woonplaats = wp;
        this.reiziger = rg;
    }

    public int getId() {
        return this.id;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String pc) {
        this.postcode = pc;
    }

    public String getHuisnummer() {
        return this.huisnummer;
    }

    public void setHuisnummer(String hn) {
        this.huisnummer = hn;
    }

    public String getStraat() {
        return this.straat;
    }

    public String getWoonplaats() {
        return this.woonplaats;
    }

    public Reiziger getReiziger() {
        return this.reiziger;
    }

    public void setReiziger(Reiziger rg) {
        this.reiziger = rg;
    }

    @Override
    public String toString() {
        return "#" + this.id + " " + getPostcode() + "-" + getHuisnummer();
    }
}
