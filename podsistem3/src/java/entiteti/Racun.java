/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author korisnik
 */
@Entity
@Table(name = "racun")
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findByIdRac", query = "SELECT r FROM Racun r WHERE r.idRac = :idRac"),
    @NamedQuery(name = "Racun.findByStanje", query = "SELECT r FROM Racun r WHERE r.stanje = :stanje"),
    @NamedQuery(name = "Racun.findByStatus", query = "SELECT r FROM Racun r WHERE r.status = :status"),
    @NamedQuery(name = "Racun.findByDozvMinus", query = "SELECT r FROM Racun r WHERE r.dozvMinus = :dozvMinus"),
    @NamedQuery(name = "Racun.findByDatumVreme", query = "SELECT r FROM Racun r WHERE r.datumVreme = :datumVreme"),
    @NamedQuery(name = "Racun.findByBrojTrans", query = "SELECT r FROM Racun r WHERE r.brojTrans = :brojTrans"),
    @NamedQuery(name = "Racun.findByIdKom", query = "SELECT r FROM Racun r WHERE r.idKom = :idKom"),
    @NamedQuery(name = "Racun.findByIdFil", query = "SELECT r FROM Racun r WHERE r.idFil = :idFil")})
public class Racun implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "Stanje")
    private double stanje;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 1)
    @Column(name = "Status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DozvMinus")
    private int dozvMinus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BrojTrans")
    private int brojTrans;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKom")
    private int idKom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdFil")
    private int idFil;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdRac")
    private Integer idRac;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "racun")
    private List<Ucestvuje> ucestvujeList;

    public Racun() {
    }

    public Racun(Integer idRac) {
        this.idRac = idRac;
    }

    public Racun(Integer idRac, double stanje, String status, int dozvMinus, Date datumVreme, int brojTrans, int idKom, int idFil) {
        this.idRac = idRac;
        this.stanje = stanje;
        this.status = status;
        this.dozvMinus = dozvMinus;
        this.datumVreme = datumVreme;
        this.brojTrans = brojTrans;
        this.idKom = idKom;
        this.idFil = idFil;
    }

    public Integer getIdRac() {
        return idRac;
    }

    public void setIdRac(Integer idRac) {
        this.idRac = idRac;
    }


    public List<Ucestvuje> getUcestvujeList() {
        return ucestvujeList;
    }

    public void setUcestvujeList(List<Ucestvuje> ucestvujeList) {
        this.ucestvujeList = ucestvujeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRac != null ? idRac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.idRac == null && other.idRac != null) || (this.idRac != null && !this.idRac.equals(other.idRac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Racun[ idRac=" + idRac + " ]";
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

    public int getDozvMinus() {
        return dozvMinus;
    }

    public void setDozvMinus(int dozvMinus) {
        this.dozvMinus = dozvMinus;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
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
    
}
