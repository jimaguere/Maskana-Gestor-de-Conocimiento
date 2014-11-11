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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "modalidad", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modalidad.findAll", query = "SELECT m FROM Modalidad m"),
    @NamedQuery(name = "Modalidad.findByModalidad", query = "SELECT m FROM Modalidad m WHERE m.modalidad = :modalidad"),
    @NamedQuery(name = "Modalidad.findByCodModalidad", query = "SELECT m FROM Modalidad m WHERE m.codModalidad = :codModalidad")})
public class Modalidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "modalidad", nullable = false, length = 100)
    private String modalidad;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cod_modalidad", nullable = false, length = 2)
    private String codModalidad;
    @JoinColumn(name = "cod_dep", referencedColumnName = "cod_dep")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento codDep;
    @OneToMany(mappedBy = "codModalidad", fetch = FetchType.EAGER)
    private List<TrabajosGrado> trabajosGradoList;

    public Modalidad() {
    }

    public Modalidad(String codModalidad) {
        this.codModalidad = codModalidad;
    }

    public Modalidad(String codModalidad, String modalidad) {
        this.codModalidad = codModalidad;
        this.modalidad = modalidad;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getCodModalidad() {
        return codModalidad;
    }

    public void setCodModalidad(String codModalidad) {
        this.codModalidad = codModalidad;
    }

    public Departamento getCodDep() {
        return codDep;
    }

    public void setCodDep(Departamento codDep) {
        this.codDep = codDep;
    }

    @XmlTransient
    public List<TrabajosGrado> getTrabajosGradoList() {
        return trabajosGradoList;
    }

    public void setTrabajosGradoList(List<TrabajosGrado> trabajosGradoList) {
        this.trabajosGradoList = trabajosGradoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codModalidad != null ? codModalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modalidad)) {
            return false;
        }
        Modalidad other = (Modalidad) object;
        if ((this.codModalidad == null && other.codModalidad != null) || (this.codModalidad != null && !this.codModalidad.equals(other.codModalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Modalidad[ codModalidad=" + codModalidad + " ]";
    }
    
}
