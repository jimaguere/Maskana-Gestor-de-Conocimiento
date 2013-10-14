/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;


import clases.Ontologia;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;


/**
 *
 * @author mateo
 */
@ManagedBean(name="modeloBean")
@ApplicationScoped
public class ModeloBean {
    OntModel modelOnt;
    String prefijo;
    private String ruta;

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }
    
    public OntModel getModelOnt() {
        return modelOnt;
    }

    public void setModelOnt(OntModel modelOnt) {
        this.modelOnt = modelOnt;
    }
    public void guardarModelo(OntModel modelo) throws FileNotFoundException, IOException {
        File file = new File(ruta + "resources/owl/OntologiaIntancia.owl");
//Hay que capturar las Excepciones
        if (!file.exists()) {
            file.createNewFile();
        }
        modelOnt=modelo;
        modelOnt.write(new PrintWriter(file));
        System.out.println("modelo grabado");
    }
    /**
     * Creates a new instance of ModeloBean
     */
    public ModeloBean() {
    }
    
    @PostConstruct
    public void reset(){
        ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        ruta = (String) servContx.getRealPath("/");
        Ontologia ont=new Ontologia();
        modelOnt=ont.iniciar(ruta);
        prefijo="http://www.owl-ontologies.com/TesisGrado.owl#";
    }
}
