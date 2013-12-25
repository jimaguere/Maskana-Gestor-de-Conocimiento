/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.List;

/**
 *
 * @author mateo
 */
public class Tesis implements Comparable<Tesis> {
    private String sigTopografica;
    private String idTg;
    private String titulo;
    private List<Autor>autor;
    private String resumen;
    private int ranking;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    
    public String getSigTopografica() {
        return sigTopografica;
    }

    public void setSigTopografica(String sigTopografica) {
        this.sigTopografica = sigTopografica;
    }

    public String getIdTg() {
        return idTg;
    }

    public void setIdTg(String idTg) {
        this.idTg = idTg;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
    
    @Override
    public int compareTo(Tesis t) {
        return t.getRanking()-this.getRanking();           
    }
}
