/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "aplicacion", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aplicacion.findAll", query = "SELECT a FROM Aplicacion a"),
    @NamedQuery(name = "Aplicacion.findByCodApi", query = "SELECT a FROM Aplicacion a WHERE a.codApi = :codApi"),
    @NamedQuery(name = "Aplicacion.findByNombreApi", query = "SELECT a FROM Aplicacion a WHERE a.nombreApi = :nombreApi")})
public class Aplicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_api", nullable = false)
    private Short codApi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nombre_api", nullable = false, length = 20)
    private String nombreApi;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aplicacion")
    private List<UsuarioAplicacion> usuarioAplicacionList;

    public Aplicacion() {
    }

    public Aplicacion(Short codApi) {
        this.codApi = codApi;
    }

    public Aplicacion(Short codApi, String nombreApi) {
        this.codApi = codApi;
        this.nombreApi = nombreApi;
    }

    public Short getCodApi() {
        return codApi;
    }

    public void setCodApi(Short codApi) {
        this.codApi = codApi;
    }

    public String getNombreApi() {
        return nombreApi;
    }

    public void setNombreApi(String nombreApi) {
        this.nombreApi = nombreApi;
    }

    @XmlTransient
    public List<UsuarioAplicacion> getUsuarioAplicacionList() {
        return usuarioAplicacionList;
    }

    public void setUsuarioAplicacionList(List<UsuarioAplicacion> usuarioAplicacionList) {
        this.usuarioAplicacionList = usuarioAplicacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codApi != null ? codApi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aplicacion)) {
            return false;
        }
        Aplicacion other = (Aplicacion) object;
        if ((this.codApi == null && other.codApi != null) || (this.codApi != null && !this.codApi.equals(other.codApi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Aplicacion[ codApi=" + codApi + " ]";
    }
    
}
