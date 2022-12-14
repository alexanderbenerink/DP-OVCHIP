package nl.hu.dp.ovchip.domain;

import java.sql.Date;
import java.util.List;

public class OVChipkaart {
    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private int reizigerId;
    private List<Product> product;

    public OVChipkaart(int kn, Date gt, int kl, double sl, int ri) {
        this.kaartNummer = kn;
        this.geldigTot = gt;
        this.klasse = kl;
        this.saldo = sl;
        this.reizigerId = ri;
    }

    public OVChipkaart(int kn, Date gt, int kl, double sl, int ri, List<Product> pr) {
        this.kaartNummer = kn;
        this.geldigTot = gt;
        this.klasse = kl;
        this.saldo = sl;
        this.reizigerId = ri;
        this.product = pr;
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Kaartnummer: " + this.kaartNummer + "\n" +
                "Geldig tot: " + this.geldigTot + "\n" +
                "Klasse: " + this.klasse + "\n" +
                "Saldo: " + this.saldo + "\n" +
                "Reiziger id: " + this.reizigerId;
    }
}
