/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "usuario_aplicacion", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioAplicacion.findAll", query = "SELECT u FROM UsuarioAplicacion u"),
    @NamedQuery(name = "UsuarioAplicacion.findByNombreUsuario", query = "SELECT u FROM UsuarioAplicacion u WHERE u.usuarioAplicacionPK.nombreUsuario = :nombreUsuario"),
    @NamedQuery(name = "UsuarioAplicacion.findByCodApi", query = "SELECT u FROM UsuarioAplicacion u WHERE u.usuarioAplicacionPK.codApi = :codApi"),
    @NamedQuery(name = "UsuarioAplicacion.findByPk", query = "SELECT u FROM UsuarioAplicacion u WHERE u.usuarioAplicacionPK.codApi = :codApi and u.usuarioAplicacionPK.nombreUsuario=:nombreUsuario"),
    @NamedQuery(name = "UsuarioAplicacion.findByEscritura", query = "SELECT u FROM UsuarioAplicacion u WHERE u.escritura = :escritura")})
public class UsuarioAplicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioAplicacionPK usuarioAplicacionPK;
    @Column(name = "escritura")
    private Boolean escritura;
    @JoinColumn(name = "nombre_usuario", referencedColumnName = "nombre_usuario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @JoinColumn(name = "cod_api", referencedColumnName = "cod_api", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Aplicacion aplicacion;

    public UsuarioAplicacion() {
    }

    public UsuarioAplicacion(UsuarioAplicacionPK usuarioAplicacionPK) {
        this.usuarioAplicacionPK = usuarioAplicacionPK;
    }

    public UsuarioAplicacion(String nombreUsuario, short codApi) {
        this.usuarioAplicacionPK = new UsuarioAplicacionPK(nombreUsuario, codApi);
    }

    public UsuarioAplicacionPK getUsuarioAplicacionPK() {
        return usuarioAplicacionPK;
    }

    public void setUsuarioAplicacionPK(UsuarioAplicacionPK usuarioAplicacionPK) {
        this.usuarioAplicacionPK = usuarioAplicacionPK;
    }

    public Boolean getEscritura() {
        return escritura;
    }

    public void setEscritura(Boolean escritura) {
        this.escritura = escritura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioAplicacionPK != null ? usuarioAplicacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioAplicacion)) {
            return false;
        }
        UsuarioAplicacion other = (UsuarioAplicacion) object;
        if ((this.usuarioAplicacionPK == null && other.usuarioAplicacionPK != null) || (this.usuarioAplicacionPK != null && !this.usuarioAplicacionPK.equals(other.usuarioAplicacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.UsuarioAplicacion[ usuarioAplicacionPK=" + usuarioAplicacionPK + " ]";
    }
    
}
