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
    @NamedQuery(name = "TrabajosGrado.findAll", query = "SELECT t FROM TrabajosGrado t"),
    @NamedQuery(name = "TrabajosGrado.findByIdTg", query = "SELECT t FROM TrabajosGrado t WHERE t.idTg = :idTg"),
    @NamedQuery(name = "TrabajosGrado.findBySigtopografica", query = "SELECT t FROM TrabajosGrado t WHERE t.sigtopografica = :sigtopografica"),
    @NamedQuery(name = "TrabajosGrado.findByTitulo", query = "SELECT t FROM TrabajosGrado t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "TrabajosGrado.findByResumen", query = "SELECT t FROM TrabajosGrado t WHERE t.resumen = :resumen"),
    @NamedQuery(name = "TrabajosGrado.findByAbstract1", query = "SELECT t FROM TrabajosGrado t WHERE t.abstract1 = :abstract1"),
    @NamedQuery(name = "TrabajosGrado.findByCalificacion", query = "SELECT t FROM TrabajosGrado t WHERE t.calificacion = :calificacion"),
    @NamedQuery(name = "TrabajosGrado.findByFechasustentacion", query = "SELECT t FROM TrabajosGrado t WHERE t.fechasustentacion = :fechasustentacion"),
    @NamedQuery(name = "TrabajosGrado.findByVisitas", query = "SELECT t FROM TrabajosGrado t WHERE t.visitas = :visitas"),
    @NamedQuery(name= "Trabajosgrado.findByRango",query="SELECT t FROM TrabajosGrado t ORDER BY T.fechasustentacion DESC")})
public class TrabajosGrado implements Serializable {
    @ManyToMany(mappedBy = "trabajosGradoList", fetch = FetchType.LAZY)
    private List<Docente> docenteList;
    @ManyToMany(mappedBy = "trabajosGradoList1", fetch = FetchType.LAZY)
    private List<Docente> docenteList1;
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
    @Column(name = "visitas")
    private Integer visitas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajosGrado", fetch = FetchType.LAZY)
    private List<Tgautor> tgautorList;
    @JoinColumn(name = "cod_modalidad", referencedColumnName = "cod_modalidad")
    @ManyToOne(fetch = FetchType.LAZY)
    private Modalidad codModalidad;
    @JoinColumn(name = "codigo_linea", referencedColumnName = "codigo_linea")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lineainvestigacion codigoLinea;

    public TrabajosGrado() {
    }

    public TrabajosGrado(Integer idTg) {
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

    public Integer getVisitas() {
        return visitas;
    }

    public void setVisitas(Integer visitas) {
        this.visitas = visitas;
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
        if (!(object instanceof TrabajosGrado)) {
            return false;
        }
        TrabajosGrado other = (TrabajosGrado) object;
        if ((this.idTg == null && other.idTg != null) || (this.idTg != null && !this.idTg.equals(other.idTg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.TrabajosGrado[ idTg=" + idTg + " ]";
    }

    public List<Docente> getDocenteList() {
        return docenteList;
    }

    public void setDocenteList(List<Docente> docenteList) {
        this.docenteList = docenteList;
    }

    public List<Docente> getDocenteList1() {
        return docenteList1;
    }

    public void setDocenteList1(List<Docente> docenteList1) {
        this.docenteList1 = docenteList1;
    }
    
}
