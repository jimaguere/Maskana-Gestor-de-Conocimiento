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
@Table(name = "lineainvestigacion", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lineainvestigacion.findAll", query = "SELECT l FROM Lineainvestigacion l"),
    @NamedQuery(name = "Lineainvestigacion.findByCodigoLinea", query = "SELECT l FROM Lineainvestigacion l WHERE l.codigoLinea = :codigoLinea"),
    @NamedQuery(name = "Lineainvestigacion.findByDescripcion", query = "SELECT l FROM Lineainvestigacion l WHERE l.descripcion = :descripcion"),
    @NamedQuery(name = "Lineainvestigacion.findByNombre", query = "SELECT l FROM Lineainvestigacion l WHERE l.nombre = :nombre")})
public class Lineainvestigacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo_linea", nullable = false, length = 2)
    private String codigoLinea;
    @Size(max = 1000)
    @Column(name = "descripcion", length = 1000)
    private String descripcion;
    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    @JoinColumn(name = "cod_dep", referencedColumnName = "cod_dep")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento codDep;
    @OneToMany(mappedBy = "codigoLinea", fetch = FetchType.EAGER)
    private List<TrabajosGrado> trabajosGradoList;

    public Lineainvestigacion() {
    }

    public Lineainvestigacion(String codigoLinea) {
        this.codigoLinea = codigoLinea;
    }

    public String getCodigoLinea() {
        return codigoLinea;
    }

    public void setCodigoLinea(String codigoLinea) {
        this.codigoLinea = codigoLinea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        hash += (codigoLinea != null ? codigoLinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lineainvestigacion)) {
            return false;
        }
        Lineainvestigacion other = (Lineainvestigacion) object;
        if ((this.codigoLinea == null && other.codigoLinea != null) || (this.codigoLinea != null && !this.codigoLinea.equals(other.codigoLinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Lineainvestigacion[ codigoLinea=" + codigoLinea + " ]";
    }
    
}
