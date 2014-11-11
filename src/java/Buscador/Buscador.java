/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Buscador;

import ManageBean.ModeloBean;
import clases.Autor;
import clases.Docente;
import clases.ExtracPdfText;
import Conexion.Ontologia;
import Conexion.OntologiaAcademica;
import clases.Tesis;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QuerySolution;
import facadePojo.EstudianteFacade;
import facadePojo.TrabajosGradoFacade;
import facadePojo.VocabularioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import pojo.TrabajosGrado;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "buscador")
@SessionScoped
public class Buscador {

    FSDirectory dir;
    @EJB
    VocabularioFacade vocabularioFacadel;
    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    TrabajosGradoFacade trabajosGradoFacade;
    private String tipoBusqueda;
    private String cadenaBusqueda;
    private String palabra;
    private String resultados;
    private List<Tesis> lista;
    private List<Tesis> selectedTesis;
    private Tesis tesisSelecion;
    private OntologiaAcademica acad;
    private OntologiaAcademica acadAutor;
    private IndexSearcher searcher;
    private String contenido;
    private TrabajosGrado entidadTrabajo;
    private boolean java;
    private String textoPdf;

    public String getTextoPdf() {
        return textoPdf;
    }

    public void setTextoPdf(String textoPdf) {
        this.textoPdf = textoPdf;
    }

    public boolean isJava() {
        return java;
    }

    public void setJava(boolean java) {
        this.java = java;
    }

    public TrabajosGrado getEntidadTrabajo() {
        return entidadTrabajo;
    }

    public void setEntidadTrabajo(TrabajosGrado entidadTrabajo) {
        this.entidadTrabajo = entidadTrabajo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public IndexSearcher getSearcher() {
        return searcher;
    }

    public void setSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public String getCadenaBusqueda() {
        return cadenaBusqueda;
    }

    public void setCadenaBusqueda(String cadenaBusqueda) {
        this.cadenaBusqueda = cadenaBusqueda;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public List<Tesis> getLista() {
        return lista;
    }

    public void setLista(List<Tesis> lista) {
        this.lista = lista;
    }

    public List<Tesis> getSelectedTesis() {
        return selectedTesis;
    }

    public void setSelectedTesis(List<Tesis> selectedTesis) {
        this.selectedTesis = selectedTesis;
    }

    public Tesis getTesisSelecion() {
        return tesisSelecion;
    }

    public void setTesisSelecion(Tesis tesisSelecion) {
        this.tesisSelecion = tesisSelecion;
    }

    public String asignarTesis(Tesis tesis) {
        this.tesisSelecion = tesis;
        String consulta = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  \n"
                + "select  \n"
                + "?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado (count(?id_tg)as ?c)\n"
                + " where{\n"
                + "?Trabajo_grado po1:Es_realizado ?autor.\n"
                + "?Trabajo_grado po1:titulo?Titulo.\n"
                + "?Trabajo_grado po1:id_trabajo?id_tg.\n"
                + "?Trabajo_grado po1:resumen?resumen.\n"
                + "?Trabajo_grado po1:signatura_topografica?Signatura_Topografica.\n"
                + "<http://www.owl-ontologies.com/TesisGrado.owl#tg" + this.tesisSelecion.getIdTg() + ">po1:tiene ?keyword.\n"
                + "?keyword po1:describen?Trabajo_grado."
                + "<http://www.owl-ontologies.com/TesisGrado.owl#tg" + this.tesisSelecion.getIdTg() + ">po1:id_trabajo ?id_tg_or."
                + "FILTER (\n"
                + "?id_tg_or!=?id_tg"
                + ")\n"
                + "}\n"
                + "group by ?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado";
        List<Tesis> lista = relacionartesis(consulta);
        int sub = (int) ((int) lista.size() * 0.3);
        this.tesisSelecion.setTesisRelacionadas(lista.subList(0, sub));
        this.tesisSelecion.setDirector(obtenerDirectorTesis(this.tesisSelecion));
        //this.tesisSelecion.setJurado(obtenerDirectorJurado(this.tesisSelecion));
        try {
            this.entidadTrabajo = this.trabajosGradoFacade.findById(this.tesisSelecion.getIdTg().trim()).get(0);
            this.entidadTrabajo.setVisitas(this.entidadTrabajo.getVisitas() == null ? 1 : this.entidadTrabajo.getVisitas() + 1);
            this.trabajosGradoFacade.edit(entidadTrabajo);
        } catch (Exception e) {
            entidadTrabajo.setVisitas(1);
        }
        this.java = true;
        this.textoPdf="";
        return "TrabajoGrado.xhtml";
    }

    /**
     * Creates a new instance of Buscador
     */
    public Buscador() {
    }

    @PostConstruct
    public void iniciar() throws IOException {
        this.tipoBusqueda = "1";
        this.cadenaBusqueda = "";
        acad = new OntologiaAcademica();
        acadAutor = new OntologiaAcademica();
        /*       ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
         if(ont==null){
         ont=new ModeloBean();
         }
         ont.reset();
        
         acad.setModel(ont.getModelOnt());
         acadAutor.setModel(ont.getModelOnt());
         searcher = ont.getSearcher();*/
        contenido = "1";

        ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String ruta = (String) servContx.getRealPath("/");
        Ontologia ont = new Ontologia();
        acad.setModel(ont.iniciar(ruta));
        acadAutor.setModel(acad.getModel());
        dir = FSDirectory.open(new File(ruta + "resources/Indice"));
        searcher = new IndexSearcher(IndexReader.open(dir));
        searcher.setSimilarity(new LMDirichletSimilarity((float) 1));
    }

    public List<String> completeGeneral(String query) {
        query = query.replaceAll("'", "");
        List<String> resul = new ArrayList<String>();
        List<Object[]> listaWords;
        String[] listaTitulo = query.split(" ");
        String res = "";
        for (int i = 0; i < listaTitulo.length - 1; i++) {
            if (i == 0) {
                res = listaTitulo[i];
            } else {
                res = res + " " + listaTitulo[i];
            }
        }
        listaWords = this.vocabularioFacadel.findAllJaroWordsComplet(listaTitulo[listaTitulo.length - 1]);
        for (int i = 0; i < 5; i++) {
            System.out.println(listaWords.get(i).toString());
            if (res.equals("")) {
                resul.add(listaWords.get(i)[0].toString());
            } else {
                resul.add(res + " " + listaWords.get(i)[0].toString());
            }

        }
        return resul;
    }

    public List<Object[]> depurarAutor() {
        this.palabra = this.cadenaBusqueda.replaceAll("'", "");
        String buscar = "";
        return estudianteFacade.findAllJaro(this.palabra);
    }

    public String[] depurarTitulo() {
        this.palabra = this.cadenaBusqueda.replaceAll("'", "");
        String[] busquedaTitulo = this.palabra.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaTitulo.length; i++) {
            if (this.vocabularioFacadel.findAllStopWords(busquedaTitulo[i]) == null) {
                List<Object[]> listaWords = this.vocabularioFacadel.findAllJaroWords(busquedaTitulo[i]);
                if (!listaWords.isEmpty()) {
                    for (int j = 0; j < listaWords.size(); j++) {
                        buscar = buscar + " " + listaWords.get(j)[0].toString();
                    }
                }
            }
        }
        if (!buscar.equals("")) {
            return buscar.split(" ");
        } else {
            return null;
        }
    }

    public String depurarContenidoTodoCorrec() {
        String[] busquedaContenido = this.cadenaBusqueda.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaContenido.length; i++) {
            buscar = buscar + "+" + busquedaContenido[i] + "~ ";
        }
        return buscar.substring(0, buscar.length() - 1);
    }

    public String depurarContenidoAlgunoCorrec() {
        String[] busquedaContenido = this.cadenaBusqueda.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaContenido.length; i++) {
            buscar = buscar + "" + busquedaContenido[i] + "~ ";
        }
        return buscar.substring(0, buscar.length() - 1);
    }

    public String depurarContenidoTodo() {
        String busquedaContenido = "(\"" + this.cadenaBusqueda + "\")";
        return busquedaContenido;
    }

    public String depurarContenidoAlguno() {
        String[] busquedaContenido = this.cadenaBusqueda.split(" ");
        String buscar = "";
        for (int i = 0; i < busquedaContenido.length; i++) {
            buscar = buscar + "" + busquedaContenido[i] + " ";
        }
        return buscar.substring(0, buscar.length() - 1);
    }

    public void prepararLista1(String consulta) {
        com.hp.hpl.jena.query.ResultSet results = acad.consultar(consulta);
        if (!results.hasNext()) {
            this.resultados = "No se encontraron resultados para: " + this.cadenaBusqueda;
        } else {
            this.resultados = "Resultados encontrados para: " + this.cadenaBusqueda;
        }
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            String consultar = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                    + "select distinct "
                    //  + "?nombre_persona?apellido_persona?calificacion"
                    + "?nombre_persona?apellido_persona"
                    + " where{"
                    + "<" + soln.get("Trabajo_grado").toString() + "> po1:Es_realizado?autor."
                    + "?autor po1:nombre_persona?nombre_persona."
                    + "?autor po1:apellido_persona?apellido_persona."
                    // + "?autor po1:calificacion?calificacion."
                    + "}order by ?nombre_persona";
            System.out.println(consultar);

            com.hp.hpl.jena.query.ResultSet rs = acadAutor.consultar(consultar);
            List<Autor> autor = new ArrayList<Autor>();
            while (rs.hasNext()) {
                QuerySolution soln1 = rs.nextSolution();
                Autor auto = new Autor();
                auto.setNombre(soln1.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", "") + " " + soln1.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
                //auto.setCalificacion(soln1.get("calificacion").toString().replace("^^http://www.w3.org/2001/XMLSchema#float", ""));
                autor.add(auto);
            }
            Tesis tesis = new Tesis();
            tesis.setIdTg(soln.get("id_tg").toString().replace("^^http://www.w3.org/2001/XMLSchema#int", ""));
            tesis.setTitulo(soln.get("Titulo").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            //tesis.setSigTopografica(soln.get("Signatura_Topografica").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            tesis.setAutor(autor);
            //tesis.setResumen(soln.get("resumen").toString().replace("@es", ""));
            this.lista.add(tesis);
            this.palabra = "";
        }
        if (!lista.isEmpty()) {
            acad.terminar();
            acadAutor.terminar();
        }
    }

    public void prepararLista(String consulta) {
        com.hp.hpl.jena.query.ResultSet results = acad.consultarAvanzada(consulta);
        if (!results.hasNext()) {
            this.resultados = "No se encontraron resultados para: " + this.cadenaBusqueda;
        } else {
            this.resultados = "Resultados encontrados para: " + this.cadenaBusqueda;
        }
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            String consultar = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                    + "select distinct "
                    //   + "?nombre_persona?apellido_persona?calificacion"
                    + "?nombre_persona?apellido_persona"
                    + " where{"
                    + "<" + soln.get("Trabajo_grado").toString() + "> po1:Es_realizado?autor."
                    + "?autor po1:nombre_persona?nombre_persona."
                    + "?autor po1:apellido_persona?apellido_persona."
                    //+ "?autor po1:calificacion?calificacion."
                    + "}order by ?nombre_persona";
            System.out.println("count:" + soln.get("c"));
            com.hp.hpl.jena.query.ResultSet rs = acadAutor.consultar(consultar);
            List<Autor> autor = new ArrayList<Autor>();
            while (rs.hasNext()) {
                QuerySolution soln1 = rs.nextSolution();
                Autor auto = new Autor();
                auto.setNombre(soln1.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", "") + " " + soln1.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
                //auto.setCalificacion(soln1.get("calificacion").toString().replace("^^http://www.w3.org/2001/XMLSchema#float", ""));
                autor.add(auto);
            }
            Tesis tesis = new Tesis();
            tesis.setIdTg(soln.get("id_tg").toString().replace("^^http://www.w3.org/2001/XMLSchema#int", ""));
            tesis.setTitulo(soln.get("Titulo").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            //tesis.setSigTopografica(soln.get("Signatura_Topografica").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            tesis.setAutor(autor);
            tesis.setRanking(Integer.parseInt(soln.get("c").toString().replace("^^http://www.w3.org/2001/XMLSchema#integer", "")));
            //tesis.setResumen(soln.get("resumen").toString().replace("@es", ""));
            this.lista.add(tesis);
            this.palabra = "";

        }
        if (!lista.isEmpty()) {
            acad.terminar();
            acadAutor.terminar();
        }
        Collections.sort(lista);
    }

    public void busquedaTitulo() throws IOException {
        if (this.cadenaBusqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No Encontraron resultados para su busqueda", ""));
            return;
        }
        String[] busqueda = depurarTitulo();
        if (busqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No Encontraron resultados para su busqueda", ""));
            return;
        }
        String filtro = "";
        this.palabra = "";
        for (int i = 1; i < busqueda.length; i++) {
            this.palabra = this.palabra + " " + busqueda[i];
            if (i != busqueda.length - 1) {
                filtro = filtro + "REGEX(str(?sin)," + "\"" + busqueda[i] + "\",\"i\")||";
            } else {
                filtro = filtro + "REGEX(str(?sin)," + "\"" + busqueda[i] + "\",\"i\")";
            }
        }
        String consulta = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  \n"
                + "select  \n"
                + "?id_tg?Titulo?Trabajo_grado (count(?id_tg)as ?c)\n"
                + " where{\n"
                + "?Trabajo_grado po1:tiene ?keyword .\n"
                + "?Trabajo_grado po1:titulo?Titulo.\n"
                + "?Trabajo_grado po1:id_trabajo?id_tg.\n"
                + "?keyword po1:sinonimo ?sin.\n"
                + "FILTER (\n"
                + filtro
                + ")\n"
                + "}\n"
                + "group by ?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado";
        this.lista = new ArrayList<Tesis>();
        prepararLista(consulta);
        String url = "./Resultado.xhtml";
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().redirect(url);

    }

    public void busquedaAutor() throws IOException {
        if (this.cadenaBusqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("por favor Seleccione un autor para su busqueda", ""));
            return;
        }
        String filtro = "";
        List<Object[]> autores = depurarAutor();
        if (autores.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No se encontraron resultados", ""));
            return;
        }
        for (int i = 0; i < autores.size(); i++) {
            String nombres[] = autores.get(i)[0].toString().split(" ");
            for (int j = 0; j < nombres.length; j++) {
                if (j != nombres.length - 1) {
                    filtro = filtro + "(REGEX(str(?nom)," + "\"" + nombres[j] + "\",\"i\")||"
                            + "REGEX(str(?ape)," + "\"" + nombres[j] + "\",\"i\"))&&";
                } else {
                    filtro = filtro + "(REGEX(str(?nom)," + "\"" + nombres[j] + "\",\"i\")||"
                            + "REGEX(str(?ape)," + "\"" + nombres[j] + "\",\"i\"))\n||";
                }
            }
            System.out.println("autores:" + autores.get(i)[1].toString());
            if (Double.parseDouble(autores.get(i)[1].toString()) == 1) {
                i = 6;
            }
        }
        filtro = filtro.substring(0, filtro.length() - 2);
        String consulta = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                + "select distinct \n"
                // + "?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado\n"
                + "?id_tg?Titulo?Trabajo_grado\n"
                + " where\n"
                + "{\n"
                + "?Trabajo_grado po1:Es_realizado ?autor .\n"
                + "?Trabajo_grado po1:titulo?Titulo.\n"
                + "?Trabajo_grado po1:id_trabajo?id_tg.\n"
                // + "?Trabajo_grado po1:resumen?resumen.\n"
                //+ "?Trabajo_grado po1:signatura_topografica?Signatura_Topografica.\n"
                + "?autor po1:nombre_persona?nom.\n"
                + "?autor po1:apellido_persona?ape.\n"
                + "FILTER (\n"
                + filtro
                + ")\n"
                + "}\n"
                + "order by ?nom";
        this.lista = new ArrayList<Tesis>();
        prepararLista1(consulta);
        String url = "./Resultado.xhtml";
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().redirect(url);
    }

    public List<Docente> obtenerDirectorTesis(Tesis tesis) {
        List<Docente> listaDirector = new ArrayList<Docente>();
        String consulta = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                + "select distinct "
                + "?nombre_persona?apellido_persona"
                + " where{"
                + "<http://www.owl-ontologies.com/TesisGrado.owl#tg" + tesis.getIdTg() + ">po1:Es_dirigido ?director.\n"
                + "?director po1:nombre_persona?nombre_persona."
                + "?director po1:apellido_persona?apellido_persona."
                + "}order by ?nombre_persona";
        com.hp.hpl.jena.query.ResultSet results = acad.consultarAvanzada(consulta);
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Docente director = new Docente();
            director.setNombres(soln.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            director.setApellidos(soln.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            listaDirector.add(director);
        }
        return listaDirector;
    }

    public List<Docente> obtenerDirectorJurado(Tesis tesis) {
        List<Docente> listaDirector = new ArrayList<Docente>();
        String consulta = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                + "select distinct "
                + "?nombre_persona?apellido_persona"
                + " where{"
                + "<http://www.owl-ontologies.com/TesisGrado.owl#tg" + tesis.getIdTg() + ">po1:Es_calificado ?director.\n"
                + "?director po1:nombre_persona?nombre_persona."
                + "?director po1:apellido_persona?apellido_persona."
                + "}order by ?nombre_persona";
        com.hp.hpl.jena.query.ResultSet results = acad.consultarAvanzada(consulta);
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            Docente director = new Docente();
            director.setNombres(soln.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            director.setApellidos(soln.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            listaDirector.add(director);
        }
        return listaDirector;
    }

    public List<Tesis> relacionartesis(String consulta) {
        List<Tesis> listaTesis = new ArrayList<Tesis>();
        com.hp.hpl.jena.query.ResultSet results = acad.consultarAvanzada(consulta);
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            String consultar = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                    + "select distinct "
                    + "?nombre_persona?apellido_persona"
                    + " where{"
                    + "<" + soln.get("Trabajo_grado").toString() + "> po1:Es_realizado?autor."
                    + "?autor po1:nombre_persona?nombre_persona."
                    + "?autor po1:apellido_persona?apellido_persona."
                    + "}order by ?nombre_persona";
            com.hp.hpl.jena.query.ResultSet rs = acadAutor.consultar(consultar);
            List<Autor> autor = new ArrayList<Autor>();
            while (rs.hasNext()) {
                QuerySolution soln1 = rs.nextSolution();
                Autor auto = new Autor();
                auto.setNombre(soln1.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", "") + " " + soln1.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
                autor.add(auto);
            }
            Tesis tesis = new Tesis();
            tesis.setIdTg(soln.get("id_tg").toString().replace("^^http://www.w3.org/2001/XMLSchema#int", ""));
            tesis.setTitulo(soln.get("Titulo").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            tesis.setAutor(autor);
            tesis.setRanking(Integer.parseInt(soln.get("c").toString().replace("^^http://www.w3.org/2001/XMLSchema#integer", "")));
            listaTesis.add(tesis);
        }
        if (!lista.isEmpty()) {
            acad.terminar();
            acadAutor.terminar();
        }
        Collections.sort(listaTesis);
        return listaTesis;
    }

    public void busquedaContenido() throws ParseException, IOException {
        if (this.cadenaBusqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No Encontraron resultados para su busqueda", ""));
            return;
        }
        String cont = this.cadenaBusqueda;
        if (this.contenido.equals("1")) {
            cont = this.depurarContenidoTodo();
        } else {
            cont = this.depurarContenidoAlguno();
        }
        QueryParser parser = new QueryParser(Version.LUCENE_46,
                "contenido",
                new StandardAnalyzer(
                Version.LUCENE_46));
        parser.setAllowLeadingWildcard(true);
        Query query = parser.parse(cont);
        TopDocs hits = searcher.search(query, 30);
        if (hits.totalHits == 0) {
            cont = this.cadenaBusqueda;
            if (this.contenido.equals("2")) {
                cont = this.depurarContenidoAlgunoCorrec();
                query = parser.parse(cont);
                hits = searcher.search(query, 30);
            }
        }
        this.lista = new ArrayList<Tesis>();
        System.out.println("resultado:" + hits.totalHits);
        ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
        String nS = ont.getPrefijo();
        OntModel modelo = ont.getModelOnt();
        String prefijo = "^^http://www.w3.org/2001/XMLSchema#string";
        OntClass claseTesis = modelo.getOntClass(nS + "Trabajo_grado");
        ObjectProperty pAutor = modelo.getObjectProperty(nS + "Es_realizado");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            try {
                Document doc = searcher.doc(scoreDoc.doc);
                List<Autor> autor = new ArrayList<Autor>();
                Tesis tesis = new Tesis();
                this.lista.add(tesis);
                Individual tg = modelo.createIndividual(nS + "tg" + doc.get("id").replaceAll(".pdf", "").replace(".PDF", ""), claseTesis);
                tesis.setIdTg(doc.get("id").replaceAll(".pdf", "").replace(".PDF", ""));
                tesis.setTitulo(tg.getPropertyValue(modelo.getDatatypeProperty(nS + "titulo")).toString().replace(prefijo, ""));
                // tesis.setResumen(tg.getPropertyValue(modelo.getDatatypeProperty(nS + "resumen")).toString().replace(prefijo, ""));
                // tesis.setSigTopografica(tg.getPropertyValue(modelo.getDatatypeProperty(nS + "signatura_topografica")).toString().replace(prefijo, ""));
                tesis.setRanking((int) scoreDoc.score);
                tesis.setPuntuacion(scoreDoc.score);
                String consultar = "PREFIX po1:<http://www.owl-ontologies.com/TesisGrado.owl#>  "
                        + "select distinct "
                        //  + "?nombre_persona?apellido_persona?calificacion"
                        + "?nombre_persona?apellido_persona"
                        + " where{"
                        + "<" + tg.toString() + "> po1:Es_realizado?autor."
                        + "?autor po1:nombre_persona?nombre_persona."
                        + "?autor po1:apellido_persona?apellido_persona."
                        //  + "?autor po1:calificacion?calificacion."
                        + "}order by ?nombre_persona";
                com.hp.hpl.jena.query.ResultSet rs = acadAutor.consultar(consultar);
                while (rs.hasNext()) {
                    QuerySolution soln1 = rs.nextSolution();
                    Autor auto = new Autor();
                    auto.setNombre(soln1.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", "") + " " + soln1.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
                    //auto.setCalificacion(soln1.get("calificacion").toString().replace("^^http://www.w3.org/2001/XMLSchema#float", ""));
                    autor.add(auto);
                }
                tesis.setAutor(autor);
                System.out.println(doc.get("id"));
            } catch (Exception e) {
            }
        }
        String url = "./Resultado.xhtml";
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.getExternalContext().redirect(url);
    }

    public void buscar() throws IOException {
        if (this.tipoBusqueda.equals("1")) {
            try {
                this.busquedaContenido();
            } catch (ParseException ex) {
                Logger.getLogger(Buscador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (this.tipoBusqueda.equals("2")) {
            this.busquedaTitulo();
        }
        if (this.tipoBusqueda.equals("3")) {
            this.busquedaAutor();
        }
    }

    public String getJurados() {
        String jurados = "";

        for (int i = 0; i < this.tesisSelecion.getJurado().size(); i++) {
            jurados = jurados + " - " + this.tesisSelecion.getJurado().get(i).getNombres() + " "
                    + this.tesisSelecion.getJurado().get(i).getApellidos();
        }
        return jurados;
    }

    public String getDirectores() {
        String directores = "";
        for (int j = 0; j < this.tesisSelecion.getDirector().size(); j++) {
            directores = directores + " - " + this.tesisSelecion.getDirector().get(j).getNombres() + " "
                    + this.tesisSelecion.getDirector().get(j).getApellidos();
        }
        return directores;
    }

    public String getAutores() {
        String autores = "";
        for (int i = 0; i < this.tesisSelecion.getAutor().size(); i++) {
            autores = autores + " - " + this.tesisSelecion.getAutor().get(i).getNombre();
        }
        return autores;
    }

    @PreDestroy
    public void finalizar() throws IOException {
        this.acadAutor.terminar();
        this.acad.terminar();
        this.dir.close();
        System.out.println("termino session");
    }

    public void verPdf() {
        this.java = false;
        ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String ruta = (String) servContx.getRealPath("/");
        InputStream archivoI;
        try {
            archivoI = new FileInputStream(ruta + "resources/pdf/" + this.tesisSelecion.getIdTg() + ".pdf");
            ExtracPdfText texto = new ExtracPdfText(archivoI);
            this.textoPdf = texto.extraerTexto();
            texto.cerrar();
        } catch (FileNotFoundException ex) {
            this.textoPdf = "";
            Logger.getLogger(Buscador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.textoPdf = "";
            Logger.getLogger(Buscador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
