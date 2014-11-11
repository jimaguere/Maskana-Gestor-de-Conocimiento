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

/**
 *
 * @author mateo
 */
@Embeddable
public class TgautorPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_tg", nullable = false)
    private int idTg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante", nullable = false)
    private int idEstudiante;

    public TgautorPK() {
    }

    public TgautorPK(int idTg, int idEstudiante) {
        this.idTg = idTg;
        this.idEstudiante = idEstudiante;
    }

    public int getIdTg() {
        return idTg;
    }

    public void setIdTg(int idTg) {
        this.idTg = idTg;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTg;
        hash += (int) idEstudiante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TgautorPK)) {
            return false;
        }
        TgautorPK other = (TgautorPK) object;
        if (this.idTg != other.idTg) {
            return false;
        }
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.TgautorPK[ idTg=" + idTg + ", idEstudiante=" + idEstudiante + " ]";
    }
    
}
