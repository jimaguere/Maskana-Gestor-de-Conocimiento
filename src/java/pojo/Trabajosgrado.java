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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
@Table(name = "trabajosgrado", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trabajosgrado.findAll", query = "SELECT t FROM Trabajosgrado t"),
    @NamedQuery(name = "Trabajosgrado.findByIdTg", query = "SELECT t FROM Trabajosgrado t WHERE t.idTg = :idTg"),
    @NamedQuery(name = "Trabajosgrado.findBySigtopografica", query = "SELECT t FROM Trabajosgrado t WHERE t.sigtopografica = :sigtopografica"),
    @NamedQuery(name = "Trabajosgrado.findByTitulo", query = "SELECT t FROM Trabajosgrado t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Trabajosgrado.findByResumen", query = "SELECT t FROM Trabajosgrado t WHERE t.resumen = :resumen"),
    @NamedQuery(name = "Trabajosgrado.findByAbstract1", query = "SELECT t FROM Trabajosgrado t WHERE t.abstract1 = :abstract1"),
    @NamedQuery(name = "Trabajosgrado.findByCalificacion", query = "SELECT t FROM Trabajosgrado t WHERE t.calificacion = :calificacion"),
    @NamedQuery(name = "Trabajosgrado.findByFechasustentacion", query = "SELECT t FROM Trabajosgrado t WHERE t.fechasustentacion = :fechasustentacion")})
public class Trabajosgrado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tg", nullable = false)
    private Integer idTg;
    @Size(max = 20)
    @Column(name = "sigtopografica", length = 20)
    private String sigtopografica;
    @Size(max = 2147483647)
    @Column(name = "titulo", length = 2147483647)
    private String titulo;
    @Size(max = 2147483647)
    @Column(name = "resumen", length = 2147483647)
    private String resumen;
    @Size(max = 2147483647)
    @Column(name = "abstract", length = 2147483647)
    private String abstract1;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "calificacion", precision = 17, scale = 17)
    private Double calificacion;
    @Column(name = "fechasustentacion")
    @Temporal(TemporalType.DATE)
    private Date fechasustentacion;
    @ManyToMany(mappedBy = "trabajosgradoList")
    private List<Docente> docenteList;
    @ManyToMany(mappedBy = "trabajosgradoList1")
    private List<Docente> docenteList1;
    @OneToMany(mappedBy = "idTg")
    private List<Keyword> keywordList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajosgrado")
    private List<Tgautor> tgautorList;
    @JoinColumn(name = "cod_modalidad", referencedColumnName = "cod_modalidad")
    @ManyToOne
    private Modalidad codModalidad;
    @JoinColumn(name = "codigo_linea", referencedColumnName = "codigo_linea")
    @ManyToOne
    private Lineainvestigacion codigoLinea;

    public Trabajosgrado() {
    }

    public Trabajosgrado(Integer idTg) {
        this.idTg = idTg;
    }

    public Integer getIdTg() {
        return idTg;
    }

    public void setIdTg(Integer idTg) {
        this.idTg = idTg;
    }

    public String getSigtopografica() {
        return sigtopografica;
    }

    public void setSigtopografica(String sigtopografica) {
        this.sigtopografica = sigtopografica;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getAbstract1() {
        return abstract1;
    }

    public void setAbstract1(String abstract1) {
        this.abstract1 = abstract1;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public Date getFechasustentacion() {
        return fechasustentacion;
    }

    public void setFechasustentacion(Date fechasustentacion) {
        this.fechasustentacion = fechasustentacion;
    }

    @XmlTransient
    public List<Docente> getDocenteList() {
        return docenteList;
    }

    public void setDocenteList(List<Docente> docenteList) {
        this.docenteList = docenteList;
    }

    @XmlTransient
    public List<Docente> getDocenteList1() {
        return docenteList1;
    }

    public void setDocenteList1(List<Docente> docenteList1) {
        this.docenteList1 = docenteList1;
    }

    @XmlTransient
    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<Keyword> keywordList) {
        this.keywordList = keywordList;
    }

    @XmlTransient
    public List<Tgautor> getTgautorList() {
        return tgautorList;
    }

    public void setTgautorList(List<Tgautor> tgautorList) {
        this.tgautorList = tgautorList;
    }

    public Modalidad getCodModalidad() {
        return codModalidad;
    }

    public void setCodModalidad(Modalidad codModalidad) {
        this.codModalidad = codModalidad;
    }

    public Lineainvestigacion getCodigoLinea() {
        return codigoLinea;
    }

    public void setCodigoLinea(Lineainvestigacion codigoLinea) {
        this.codigoLinea = codigoLinea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTg != null ? idTg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trabajosgrado)) {
            return false;
        }
        Trabajosgrado other = (Trabajosgrado) object;
        if ((this.idTg == null && other.idTg != null) || (this.idTg != null && !this.idTg.equals(other.idTg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Trabajosgrado[ idTg=" + idTg + " ]";
    }
    
}
