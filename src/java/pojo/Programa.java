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
@Table(name = "programa", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programa.findAll", query = "SELECT p FROM Programa p"),
    @NamedQuery(name = "Programa.findByCodProg", query = "SELECT p FROM Programa p WHERE p.codProg = :codProg"),
    @NamedQuery(name = "Programa.findByCodDep", query = "SELECT p FROM Programa p WHERE p.codDep.codDep = :codDep"),
    @NamedQuery(name = "Programa.findByNombre", query = "SELECT p FROM Programa p WHERE p.nombre = :nombre")})
public class Programa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "cod_prog", nullable = false, length = 10)
    private String codProg;
    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    @JoinColumn(name = "cod_dep", referencedColumnName = "cod_dep")
    @ManyToOne
    private Departamento codDep;
    @OneToMany(mappedBy = "programa")
    private List<Estudiante> estudianteList;

    public Programa() {
    }

    public Programa(String codProg) {
        this.codProg = codProg;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
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
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codProg != null ? codProg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programa)) {
            return false;
        }
        Programa other = (Programa) object;
        if ((this.codProg == null && other.codProg != null) || (this.codProg != null && !this.codProg.equals(other.codProg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Programa[ codProg=" + codProg + " ]";
    }
    
}
