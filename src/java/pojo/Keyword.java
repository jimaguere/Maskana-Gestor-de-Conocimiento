/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "keyword", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Keyword.findAll", query = "SELECT k FROM Keyword k"),
    @NamedQuery(name = "Keyword.findById", query = "SELECT k FROM Keyword k WHERE k.id = :id"),
    @NamedQuery(name = "Keyword.findByPalabra", query = "SELECT k FROM Keyword k WHERE k.palabra = :palabra")})
public class Keyword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 1000)
    @Column(name = "palabra", length = 1000)
    private String palabra;
    @JoinTable(name = "sinonimo_keyword", joinColumns = {
        @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_word", referencedColumnName = "id_word", nullable = false)})
    @ManyToMany
    private List<Palabra> palabraList;
    @JoinColumn(name = "id_tg", referencedColumnName = "id_tg")
    @ManyToOne
    private TrabajosGrado idTg;

    public Keyword() {
    }

    public Keyword(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    @XmlTransient
    public List<Palabra> getPalabraList() {
        return palabraList;
    }

    public void setPalabraList(List<Palabra> palabraList) {
        this.palabraList = palabraList;
    }

    public TrabajosGrado getIdTg() {
        return idTg;
    }

    public void setIdTg(TrabajosGrado idTg) {
        this.idTg = idTg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Keyword)) {
            return false;
        }
        Keyword other = (Keyword) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Keyword[ id=" + id + " ]";
    }
    
}
