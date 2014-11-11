/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "estudiante", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByCodestudiante", query = "SELECT e FROM Estudiante e WHERE e.codestudiante = :codestudiante"),
    @NamedQuery(name = "Estudiante.findByApellidos", query = "SELECT e FROM Estudiante e WHERE e.apellidos = :apellidos"),
    @NamedQuery(name = "Estudiante.findByIdentificacion", query = "SELECT e FROM Estudiante e WHERE e.identificacion = :identificacion"),
    @NamedQuery(name = "Estudiante.findByFechaGrado", query = "SELECT e FROM Estudiante e WHERE e.fechaGrado = :fechaGrado"),
    @NamedQuery(name = "Estudiante.findByNombres", query = "SELECT e FROM Estudiante e WHERE e.nombres = :nombres"),
    @NamedQuery(name = "Estudiante.findById", query = "SELECT e FROM Estudiante e WHERE e.id = :id")})
public class Estudiante implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante", fetch = FetchType.EAGER)
    private List<Tgautor> tgautorList;
    private static final long serialVersionUID = 1L;
    @Size(max = 1000)
    @Column(name = "codestudiante", length = 1000)
    private String codestudiante;
    @Size(max = 50)
    @Column(name = "apellidos", length = 50)
    private String apellidos;
    @Size(max = 20)
    @Column(name = "identificacion", length = 20)
    private String identificacion;
    @Column(name = "fecha_grado")
    @Temporal(TemporalType.DATE)
    private Date fechaGrado;
    @Size(max = 100)
    @Column(name = "nombres", length = 100)
    private String nombres;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "programa", referencedColumnName = "cod_prog")
    @ManyToOne(fetch = FetchType.EAGER)
    private Programa programa;

    public Estudiante() {
    }

    public Estudiante(Integer id) {
        this.id = id;
    }

    public String getCodestudiante() {
        return codestudiante;
    }

    public void setCodestudiante(String codestudiante) {
        this.codestudiante = codestudiante;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Date getFechaGrado() {
        return fechaGrado;
    }

    public void setFechaGrado(Date fechaGrado) {
        this.fechaGrado = fechaGrado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
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
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Estudiante[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Tgautor> getTgautorList() {
        return tgautorList;
    }

    public void setTgautorList(List<Tgautor> tgautorList) {
        this.tgautorList = tgautorList;
    }
    
}
