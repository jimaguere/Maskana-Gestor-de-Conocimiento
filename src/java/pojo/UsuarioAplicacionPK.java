/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author mateo
 */
@Embeddable
public class UsuarioAplicacionPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombreUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_api", nullable = false)
    private short codApi;

    public UsuarioAplicacionPK() {
    }

    public UsuarioAplicacionPK(String nombreUsuario, short codApi) {
        this.nombreUsuario = nombreUsuario;
        this.codApi = codApi;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public short getCodApi() {
        return codApi;
    }

    public void setCodApi(short codApi) {
        this.codApi = codApi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreUsuario != null ? nombreUsuario.hashCode() : 0);
        hash += (int) codApi;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioAplicacionPK)) {
            return false;
        }
        UsuarioAplicacionPK other = (UsuarioAplicacionPK) object;
        if ((this.nombreUsuario == null && other.nombreUsuario != null) || (this.nombreUsuario != null && !this.nombreUsuario.equals(other.nombreUsuario))) {
            return false;
        }
        if (this.codApi != other.codApi) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.UsuarioAplicacionPK[ nombreUsuario=" + nombreUsuario + ", codApi=" + codApi + " ]";
    }
    
}
