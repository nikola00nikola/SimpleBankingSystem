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
public class FilijalaResource implements Serializable{
    int idFil, idMes;
    String naziv, adresa;
    
    public FilijalaResource(int idF, String ime, String adr, int idM){
        idFil = idF;
        naziv = ime;
        adresa=adr;
        idMes=idM;
    }

    public int getIdFil() {
        return idFil;
    }

    public void setIdFil(int idFil) {
        this.idFil = idFil;
    }

    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
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
