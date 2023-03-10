/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author korisnik
 */
@Entity
@Table(name = "ucestvuje")
@NamedQueries({
    @NamedQuery(name = "Ucestvuje.findAll", query = "SELECT u FROM Ucestvuje u"),
    @NamedQuery(name = "Ucestvuje.findByIdTra", query = "SELECT u FROM Ucestvuje u WHERE u.ucestvujePK.idTra = :idTra"),
    @NamedQuery(name = "Ucestvuje.findByIdRac", query = "SELECT u FROM Ucestvuje u WHERE u.ucestvujePK.idRac = :idRac"),
    @NamedQuery(name = "Ucestvuje.findByRedBroj", query = "SELECT u FROM Ucestvuje u WHERE u.redBroj = :redBroj")})
public class Ucestvuje implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "RedBroj")
    private int redBroj;
    @Column(name = "Prvi")
    private Boolean prvi;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UcestvujePK ucestvujePK;
    @JoinColumn(name = "IdRac", referencedColumnName = "IdRac", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Racun racun;
    @JoinColumn(name = "IdTra", referencedColumnName = "IdTra", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Transakcija transakcija;

    public Ucestvuje() {
        prvi=true;
    }

    public Ucestvuje(UcestvujePK ucestvujePK) {
        this.ucestvujePK = ucestvujePK;
        prvi=Boolean.TRUE;
    }

    public Ucestvuje(UcestvujePK ucestvujePK, int redBroj) {
        this.ucestvujePK = ucestvujePK;
        this.redBroj = redBroj;
        prvi=Boolean.TRUE;
    }

    public Ucestvuje(int idTra, int idRac) {
        this.ucestvujePK = new UcestvujePK(idTra, idRac);
        prvi=Boolean.TRUE;
    }

    public UcestvujePK getUcestvujePK() {
        return ucestvujePK;
    }

    public void setUcestvujePK(UcestvujePK ucestvujePK) {
        this.ucestvujePK = ucestvujePK;
    }


    public Racun getRacun() {
        return racun;
    }

    public void setRacun(Racun racun) {
        this.racun = racun;
    }

    public Transakcija getTransakcija() {
        return transakcija;
    }

    public void setTransakcija(Transakcija transakcija) {
        this.transakcija = transakcija;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ucestvujePK != null ? ucestvujePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ucestvuje)) {
            return false;
        }
        Ucestvuje other = (Ucestvuje) object;
        if ((this.ucestvujePK == null && other.ucestvujePK != null) || (this.ucestvujePK != null && !this.ucestvujePK.equals(other.ucestvujePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Ucestvuje[ ucestvujePK=" + ucestvujePK + " ]";
    }

    public int getRedBroj() {
        return redBroj;
    }

    public void setRedBroj(int redBroj) {
        this.redBroj = redBroj;
    }

    public Boolean getPrvi() {
        return prvi;
    }

    public void setPrvi(Boolean prvi) {
        this.prvi = prvi;
    }
    
}
