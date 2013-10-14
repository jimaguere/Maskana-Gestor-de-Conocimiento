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
public class TgautorPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_tg", nullable = false)
    private int idTg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codestudiante", nullable = false, length = 20)
    private String codestudiante;

    public TgautorPK() {
    }

    public TgautorPK(int idTg, String codestudiante) {
        this.idTg = idTg;
        this.codestudiante = codestudiante;
    }

    public int getIdTg() {
        return idTg;
    }

    public void setIdTg(int idTg) {
        this.idTg = idTg;
    }

    public String getCodestudiante() {
        return codestudiante;
    }

    public void setCodestudiante(String codestudiante) {
        this.codestudiante = codestudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTg;
        hash += (codestudiante != null ? codestudiante.hashCode() : 0);
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
        if ((this.codestudiante == null && other.codestudiante != null) || (this.codestudiante != null && !this.codestudiante.equals(other.codestudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.TgautorPK[ idTg=" + idTg + ", codestudiante=" + codestudiante + " ]";
    }
    
}
