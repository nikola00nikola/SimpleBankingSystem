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
public class MestoResource implements Serializable {
    private int idMes, postBr;
    private String naziv;

    public MestoResource(int id, String ime, int pbr){
        idMes=id;
        naziv=ime;
        postBr=pbr;
    }
    
    public int getIdMes() {
        return idMes;
    }

    public void setIdMes(int idMes) {
        this.idMes = idMes;
    }

    public int getPostBr() {
        return postBr;
    }

    public void setPostBr(int postBr) {
        this.postBr = postBr;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
    
}
