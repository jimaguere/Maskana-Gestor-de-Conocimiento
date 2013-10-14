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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "palabra", catalog = "sawa", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Palabra.findAll", query = "SELECT p FROM Palabra p"),
    @NamedQuery(name = "Palabra.findByIdWord", query = "SELECT p FROM Palabra p WHERE p.idWord = :idWord"),
    @NamedQuery(name = "Palabra.findByWord", query = "SELECT p FROM Palabra p WHERE p.word = :word")})
public class Palabra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_word", nullable = false)
    private Integer idWord;
    @Size(max = 1000)
    @Column(name = "word", length = 1000)
    private String word;
    @JoinTable(name = "sinonimo", joinColumns = {
        @JoinColumn(name = "id_word", referencedColumnName = "id_word", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_meaning", referencedColumnName = "id_meaning", nullable = false)})
    @ManyToMany
    private List<Significado> significadoList;
    @ManyToMany(mappedBy = "palabraList")
    private List<Keyword> keywordList;

    public Palabra() {
    }

    public Palabra(Integer idWord) {
        this.idWord = idWord;
    }

    public Integer getIdWord() {
        return idWord;
    }

    public void setIdWord(Integer idWord) {
        this.idWord = idWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @XmlTransient
    public List<Significado> getSignificadoList() {
        return significadoList;
    }

    public void setSignificadoList(List<Significado> significadoList) {
        this.significadoList = significadoList;
    }

    @XmlTransient
    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<Keyword> keywordList) {
        this.keywordList = keywordList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idWord != null ? idWord.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Palabra)) {
            return false;
        }
        Palabra other = (Palabra) object;
        if ((this.idWord == null && other.idWord != null) || (this.idWord != null && !this.idWord.equals(other.idWord))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pojo.Palabra[ idWord=" + idWord + " ]";
    }
    
}
