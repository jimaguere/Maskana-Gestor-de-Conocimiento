/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "vocabulario", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vocabulario.findAll", query = "SELECT v FROM Vocabulario v"),
    @NamedQuery(name = "Vocabulario.findByIdPalabra", query = "SELECT v FROM Vocabulario v WHERE v.idPalabra = :idPalabra"),
    @NamedQuery(name = "Vocabulario.findByPalabra", query = "SELECT v FROM Vocabulario v WHERE v.palabra = :palabra")})
public class Vocabulario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_palabra", nullable = false)
    private Integer idPalabra;
    @Size(max = 100)
    @Column(name = "palabra", length = 100)
    private String palabra;

    public Vocabulario() {
    }

    public Vocabulario(Integer idPalabra) {
        this.idPalabra = idPalabra;
    }

    public Integer getIdPalabra() {
        return idPalabra;
    }

    public void setIdPalabra(Integer idPalabra) {
        this.idPalabra = idPalabra;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPalabra != null ? idPalabra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vocabulario)) {
            return false;
        }
        Vocabulario other = (Vocabulario) object;
        if ((this.idPalabra == null && other.idPalabra != null) || (this.idPalabra != null && !this.idPalabra.equals(other.idPalabra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Vocabulario[ idPalabra=" + idPalabra + " ]";
    }
    
}
