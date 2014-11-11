/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "tgautor", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tgautor.findAll", query = "SELECT t FROM Tgautor t"),
    @NamedQuery(name = "Tgautor.findByIdTg", query = "SELECT t FROM Tgautor t WHERE t.tgautorPK.idTg = :idTg"),
    @NamedQuery(name = "Tgautor.findByCalificacion", query = "SELECT t FROM Tgautor t WHERE t.calificacion = :calificacion"),
    @NamedQuery(name = "Tgautor.findByIdEstudiante", query = "SELECT t FROM Tgautor t WHERE t.tgautorPK.idEstudiante = :idEstudiante")})
public class Tgautor implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TgautorPK tgautorPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calificacion", precision = 17, scale = 17)
    private Double calificacion;
    @JoinColumn(name = "id_tg", referencedColumnName = "id_tg", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TrabajosGrado trabajosGrado;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante estudiante;

    public Tgautor() {
    }

    public Tgautor(TgautorPK tgautorPK) {
        this.tgautorPK = tgautorPK;
    }

    public Tgautor(int idTg, int idEstudiante) {
        this.tgautorPK = new TgautorPK(idTg, idEstudiante);
    }

    public TgautorPK getTgautorPK() {
        return tgautorPK;
    }

    public void setTgautorPK(TgautorPK tgautorPK) {
        this.tgautorPK = tgautorPK;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public TrabajosGrado getTrabajosGrado() {
        return trabajosGrado;
    }

    public void setTrabajosGrado(TrabajosGrado trabajosGrado) {
        this.trabajosGrado = trabajosGrado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tgautorPK != null ? tgautorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tgautor)) {
            return false;
        }
        Tgautor other = (Tgautor) object;
        if ((this.tgautorPK == null && other.tgautorPK != null) || (this.tgautorPK != null && !this.tgautorPK.equals(other.tgautorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Tgautor[ tgautorPK=" + tgautorPK + " ]";
    }
    
}
