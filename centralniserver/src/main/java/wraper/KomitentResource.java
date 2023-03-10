/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wraper;

import java.io.Serializable;

/**
 *
 * @author korisnik
 */
public class KomitentResource implements Serializable {
    private int idMes, idKom;
    private String naziv, adresa;

    public KomitentResource(int idK, String ime, String adr, int idM){
        idMes=idM;
        idKom=idK;
        naziv=ime;
        adresa=adr;
    }

    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    
    
    
    
}
