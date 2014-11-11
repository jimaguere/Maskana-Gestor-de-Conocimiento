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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "docente", catalog = "sawa", schema = "public")
@NamedQueries({
    @NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d"),
    @NamedQuery(name = "Docente.findByCodocente", query = "SELECT d FROM Docente d WHERE d.codocente = :codocente"),
    @NamedQuery(name = "Docente.findByNombres", query = "SELECT d FROM Docente d WHERE d.nombres = :nombres"),
    @NamedQuery(name = "Docente.findByCedula", query = "SELECT d FROM Docente d WHERE d.cedula = :cedula"),
    @NamedQuery(name = "Docente.findByNombre", query = "SELECT d FROM Docente d WHERE d.nombre = :nombre"),
    @NamedQuery(name = "Docente.findByApellido", query = "SELECT d FROM Docente d WHERE d.apellido = :apellido"),
    @NamedQuery(name = "Docente.findByTipo", query = "SELECT d FROM Docente d WHERE d.tipo = :tipo"),
    @NamedQuery(name = "Docente.findById", query = "SELECT d FROM Docente d WHERE d.id = :id")})
public class Docente implements Serializable {
    @JoinTable(name = "tgjurado", joinColumns = {
        @JoinColumn(name = "id_docente", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_tg", referencedColumnName = "id_tg", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TrabajosGrado> trabajosGradoList;
    @JoinTable(name = "tgasesor", joinColumns = {
        @JoinColumn(name = "id_docente", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_tg", referencedColumnName = "id_tg", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TrabajosGrado> trabajosGradoList1;
    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "codocente", length = 20)
    private String codocente;
    @Size(max = 100)
    @Column(name = "nombres", length = 100)
    private String nombres;
    @Size(max = 20)
    @Column(name = "cedula", length = 20)
    private String cedula;
    @Size(max = 50)
    @Column(name = "nombre", length = 50)
    private String nombre;
    @Size(max = 50)
    @Column(name = "apellido", length = 50)
    private String apellido;
    @Column(name = "tipo")
    private Character tipo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    public Docente() {
    }

    public Docente(Integer id) {
        this.id = id;
    }

    public String getCodocente() {
        return codocente;
    }

    public void setCodocente(String codocente) {
        this.codocente = codocente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Docente[ id=" + id + " ]";
    }

    public List<TrabajosGrado> getTrabajosGradoList() {
        return trabajosGradoList;
    }

    public void setTrabajosGradoList(List<TrabajosGrado> trabajosGradoList) {
        this.trabajosGradoList = trabajosGradoList;
    }

    public List<TrabajosGrado> getTrabajosGradoList1() {
        return trabajosGradoList1;
    }

    public void setTrabajosGradoList1(List<TrabajosGrado> trabajosGradoList1) {
        this.trabajosGradoList1 = trabajosGradoList1;
    }
    
}
