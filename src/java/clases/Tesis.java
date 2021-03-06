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
    private List<Tesis> tesisRelacionadas;
    private List<Docente>director;
    private List<Docente>jurado;
    private double puntuacion;
    private String nombreAutores;
    private String visitas;

    public String getVisitas() {
        return visitas;
    }

    public void setVisitas(String visitas) {
        this.visitas = visitas;
    }

    
    public String getNombreAutores() {
        return nombreAutores;
    }

    public void setNombreAutores(String nombreAutores) {
        this.nombreAutores = nombreAutores;
    }

    
    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }
    
    public List<Docente> getJurado() {
        return jurado;
    }

    public void setJurado(List<Docente> jurado) {
        this.jurado = jurado;
    }

    
    public List<Tesis> getTesisRelacionadas() {
        return tesisRelacionadas;
    }

    public void setTesisRelacionadas(List<Tesis> tesisRelacionadas) {
        this.tesisRelacionadas = tesisRelacionadas;
    }
 
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
        this.nombreAutores="";
        for(int i=0;i<autor.size();i++){
            this.nombreAutores=this.nombreAutores+" - "+autor.get(i).getNombre();
        }
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public List<Docente> getDirector() {
        return director;
    }

    public void setDirector(List<Docente> director) {
        this.director = director;
    }
    
    
    
    @Override
    public int compareTo(Tesis t) {
        return t.getRanking()-this.getRanking();           
    }
}
