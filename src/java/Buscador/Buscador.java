/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Buscador;

import facadePojo.EstudianteFacade;
import facadePojo.VocabularioFacade;
import java.util.ArrayList;
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

    /**
     * Creates a new instance of Buscador
     */
    public Buscador() {
    }

    @PostConstruct
    public void iniciar() {
        this.tipoBusqueda = "1";
        this.cadenaBusqueda = "";
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
    
    public void busquedaTitulo() {
        if (this.cadenaBusqueda == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("No Encontraron resultados para su busqueda", ""));
            return;
        }
        String[] busqueda = depurarTitulo();
        if(busqueda==null){
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

    }

    public void buscar() {
    }
}
