/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author korisnik
 */
@Embeddable
public class UcestvujePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTra")
    private int idTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdRac")
    private int idRac;

    public UcestvujePK() {
    }

    public UcestvujePK(int idTra, int idRac) {
        this.idTra = idTra;
        this.idRac = idRac;
    }

    public int getIdTra() {
        return idTra;
    }

    public void setIdTra(int idTra) {
        this.idTra = idTra;
    }

    public int getIdRac() {
        return idRac;
    }

    public void setIdRac(int idRac) {
        this.idRac = idRac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTra;
        hash += (int) idRac;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UcestvujePK)) {
            return false;
        }
        UcestvujePK other = (UcestvujePK) object;
        if (this.idTra != other.idTra) {
            return false;
        }
        if (this.idRac != other.idRac) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.UcestvujePK[ idTra=" + idTra + ", idRac=" + idRac + " ]";
    }
    
}
