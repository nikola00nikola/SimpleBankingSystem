/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wraper;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author korisnik
 */
public class TransakcijaResource implements Serializable{
    private int idTra, idFil, redniBroj;
    private Date datumVreme;
    private double iznos;
    private String svrha,vrsta;

    public TransakcijaResource(int idTra, int idFil, int redniBroj, Date datumVreme, double iznos, String svrha, String vrsta) {
        this.idTra = idTra;
        this.idFil = idFil;
        this.redniBroj = redniBroj;
        this.datumVreme = datumVreme;
        this.iznos = iznos;
        this.svrha = svrha;
        this.vrsta = vrsta;
    }

    
    
    public int getIdTra() {
        return idTra;
    }

    public void setIdTra(int idTra) {
        this.idTra = idTra;
    }

    public int getIdFil() {
        return idFil;
    }

    public void setIdFil(int idFil) {
        this.idFil = idFil;
    }

    public int getRedniBroj() {
        return redniBroj;
    }

    public void setRedniBroj(int redniBroj) {
        this.redniBroj = redniBroj;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }
    
    
}
