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
@Table(name = "transakcija")
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTra", query = "SELECT t FROM Transakcija t WHERE t.idTra = :idTra"),
    @NamedQuery(name = "Transakcija.findByDatumVreme", query = "SELECT t FROM Transakcija t WHERE t.datumVreme = :datumVreme"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha"),
    @NamedQuery(name = "Transakcija.findByVrsta", query = "SELECT t FROM Transakcija t WHERE t.vrsta = :vrsta"),
    @NamedQuery(name = "Transakcija.findByIdFil", query = "SELECT t FROM Transakcija t WHERE t.idFil = :idFil")})
public class Transakcija implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Iznos")
    private double iznos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Svrha")
    private String svrha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "Vrsta")
    private String vrsta;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdTra")
    private Integer idTra;
    @Column(name = "IdFil")
    private Integer idFil;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private List<Ucestvuje> ucestvujeList;

    public Transakcija() {
    }

    public Transakcija(Integer idTra) {
        this.idTra = idTra;
    }

    public Transakcija(Integer idTra, Date datumVreme, double iznos, String svrha, String vrsta) {
        this.idTra = idTra;
        this.datumVreme = datumVreme;
        this.iznos = iznos;
        this.svrha = svrha;
        this.vrsta = vrsta;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }


    public Integer getIdFil() {
        return idFil;
    }

    public void setIdFil(Integer idFil) {
        this.idFil = idFil;
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
        hash += (idTra != null ? idTra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTra == null && other.idTra != null) || (this.idTra != null && !this.idTra.equals(other.idTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Transakcija[ idTra=" + idTra + " ]";
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
