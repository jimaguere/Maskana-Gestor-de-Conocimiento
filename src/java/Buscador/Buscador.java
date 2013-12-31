/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Buscador;

import ManageBean.ModeloBean;
import clases.Autor;
import clases.OntologiaAcademica;
import clases.Tesis;
import com.hp.hpl.jena.query.QuerySolution;
import facadePojo.EstudianteFacade;
import facadePojo.VocabularioFacade;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "buscador")
@ApplicationScoped
public class Buscador {

    @EJB
    VocabularioFacade vocabularioFacadel;
    @EJB
    EstudianteFacade estudianteFacade;
    private String tipoBusqueda;
    private String cadenaBusqueda;
    private String palabra;
    private String resultados;
    private List<Tesis> lista;
    private OntologiaAcademica acad;
    private OntologiaAcademica acadAutor;

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

    /**
     * Creates a new instance of Buscador
     */
    public Buscador() {
    }

    @PostConstruct
    public void iniciar() {
        this.tipoBusqueda = "1";
        this.cadenaBusqueda = "";
        acad = new OntologiaAcademica();
        acadAutor = new OntologiaAcademica();
        ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
        acad.setModel(ont.getModelOnt());
        acadAutor.setModel(ont.getModelOnt());
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
        String[] busqueda = this.palabra.split(" ");
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
                    + "?nombre_persona?apellido_persona?calificacion"
                    + " where{"
                    + "<" + soln.get("Trabajo_grado").toString() + "> po1:Es_realizado?autor."
                    + "?autor po1:nombre_persona?nombre_persona."
                    + "?autor po1:apellido_persona?apellido_persona."
                    + "?autor po1:calificacion?calificacion."
                    + "}order by ?nombre_persona";
            System.out.println("count:" + soln.get("c"));
            com.hp.hpl.jena.query.ResultSet rs = acadAutor.consultar(consultar);
            List<Autor> autor = new ArrayList<Autor>();
            while (rs.hasNext()) {
                QuerySolution soln1 = rs.nextSolution();
                Autor auto = new Autor();
                auto.setNombre(soln1.get("nombre_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", "") + " " + soln1.get("apellido_persona").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
                auto.setCalificacion(soln1.get("calificacion").toString().replace("^^http://www.w3.org/2001/XMLSchema#float", ""));
                autor.add(auto);
            }
            Tesis tesis = new Tesis();
            tesis.setIdTg(soln.get("id_tg").toString().replace("^^http://www.w3.org/2001/XMLSchema#int", ""));
            tesis.setTitulo(soln.get("Titulo").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            tesis.setSigTopografica(soln.get("Signatura_Topografica").toString().replace("^^http://www.w3.org/2001/XMLSchema#string", ""));
            tesis.setAutor(autor);
            tesis.setRanking(Integer.parseInt(soln.get("c").toString().replace("^^http://www.w3.org/2001/XMLSchema#integer", "")));
            tesis.setResumen(soln.get("resumen").toString().replace("@es", ""));
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
                + "?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado (count(?id_tg)as ?c)\n"
                + " where{\n"
                + "?Trabajo_grado po1:tiene ?keyword .\n"
                + "?Trabajo_grado po1:titulo?Titulo.\n"
                + "?Trabajo_grado po1:id_trabajo?id_tg.\n"
                + "?Trabajo_grado po1:resumen?resumen.\n"
                + "?Trabajo_grado po1:signatura_topografica?Signatura_Topografica.\n"
                + "?keyword po1:sinonimo ?sin.\n"
                + "FILTER (\n"
                + filtro
                + ")\n"
                + "}\n"
                + "group by ?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado";
        this.lista = new ArrayList<Tesis>();
        prepararLista(consulta);
        //String url = "Resultado.xhtml";
        //FacesContext fc = FacesContext.getCurrentInstance();
        //fc.getExternalContext().redirect(url);

    }
    public void busquedaAutor() throws IOException {
        if (this.cadenaBusqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("por favor Seleccione un autor para su busqueda", ""));
            return;
        }
        String filtro = "";
        List<Object[]> autores = depurarAutor();
        for (int i = 0; i < 5; i++) {
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
                + "?id_tg?Titulo?Signatura_Topografica?resumen?Trabajo_grado\n"
                + " where\n"
                + "{\n"
                + "?Trabajo_grado po1:Es_realizado ?autor .\n"
                + "?Trabajo_grado po1:titulo?Titulo.\n"
                + "?Trabajo_grado po1:id_trabajo?id_tg.\n"
                + "?Trabajo_grado po1:resumen?resumen.\n"
                + "?Trabajo_grado po1:signatura_topografica?Signatura_Topografica.\n"
                + "?autor po1:nombre_persona?nom.\n"
                + "?autor po1:apellido_persona?ape.\n"
                + "FILTER (\n"
                + filtro
                + ")\n"
                + "}\n"
                + "order by ?nom";
        this.lista = new ArrayList<Tesis>();
        prepararLista(consulta);
        //String url = "Resultado.xhtml";
        //FacesContext fc = FacesContext.getCurrentInstance();
        //fc.getExternalContext().redirect(url);    
    }

    public void buscar() throws IOException {
        if(this.tipoBusqueda.equals("2")){
            this.busquedaTitulo();
        }
        if(this.tipoBusqueda.equals("3")){
            this.busquedaAutor();
        }
    }
    
}
