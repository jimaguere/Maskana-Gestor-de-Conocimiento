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
@Table(name = "departamento", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findByCodDep", query = "SELECT d FROM Departamento d WHERE d.codDep = :codDep"),
    @NamedQuery(name = "Departamento.findByCodFac", query = "SELECT d FROM Departamento d WHERE d.codFac.codFac = :codFac"),
    @NamedQuery(name = "Departamento.findByNombre", query = "SELECT d FROM Departamento d WHERE d.nombre = :nombre")})
public class Departamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "cod_dep", nullable = false, length = 5)
    private String codDep;
    @Size(max = 50)
    @Column(name = "nombre", length = 50)
    private String nombre;
    @OneToMany(mappedBy = "codDep")
    private List<Lineainvestigacion> lineainvestigacionList;
    @OneToMany(mappedBy = "codDep")
    private List<Modalidad> modalidadList;
    @OneToMany(mappedBy = "codDep")
    private List<Programa> programaList;
    @JoinColumn(name = "cod_fac", referencedColumnName = "cod_fac")
    @ManyToOne
    private Facultad codFac;

    public Departamento() {
    }

    public Departamento(String codDep) {
        this.codDep = codDep;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Lineainvestigacion> getLineainvestigacionList() {
        return lineainvestigacionList;
    }

    public void setLineainvestigacionList(List<Lineainvestigacion> lineainvestigacionList) {
        this.lineainvestigacionList = lineainvestigacionList;
    }

    @XmlTransient
    public List<Modalidad> getModalidadList() {
        return modalidadList;
    }

    public void setModalidadList(List<Modalidad> modalidadList) {
        this.modalidadList = modalidadList;
    }

    @XmlTransient
    public List<Programa> getProgramaList() {
        return programaList;
    }

    public void setProgramaList(List<Programa> programaList) {
        this.programaList = programaList;
    }

    public Facultad getCodFac() {
        return codFac;
    }

    public void setCodFac(Facultad codFac) {
        this.codFac = codFac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codDep != null ? codDep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        if ((this.codDep == null && other.codDep != null) || (this.codDep != null && !this.codDep.equals(other.codDep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Departamento[ codDep=" + codDep + " ]";
    }
    
}
