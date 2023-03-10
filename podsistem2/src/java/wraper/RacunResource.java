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
public class RacunResource implements Serializable{
    private int idRac, dozvMinus, brojTrans, idKom, idFil;
    private double stanje;
    private String status;
    private Date datumVreme;

    public RacunResource(int idRac, int dozvMinus, int brojTrans, int idKom, int idFil, double stanje, String status, Date datumVreme) {
        this.idRac = idRac;
        this.dozvMinus = dozvMinus;
        this.brojTrans = brojTrans;
        this.idKom = idKom;
        this.idFil = idFil;
        this.stanje = stanje;
        this.status = status;
        this.datumVreme = datumVreme;
    }

    public int getIdRac() {
        return idRac;
    }

    public void setIdRac(int idRac) {
        this.idRac = idRac;
    }

    public int getDozvMinus() {
        return dozvMinus;
    }

    public void setDozvMinus(int dozvMinus) {
        this.dozvMinus = dozvMinus;
    }

    public int getBrojTrans() {
        return brojTrans;
    }

    public void setBrojTrans(int brojTrans) {
        this.brojTrans = brojTrans;
    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }

    public int getIdFil() {
        return idFil;
    }

    public void setIdFil(int idFil) {
        this.idFil = idFil;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }
    
    
}
