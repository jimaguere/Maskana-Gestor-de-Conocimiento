/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 *
 * @author mateo
 */
public class Ontologia {

    com.hp.hpl.jena.query.Query query;
    QueryExecution qexec;
    OntModel model;

    public OntModel getModel() {
        return model;
    }

    public void setModel(OntModel model) {
        this.model = model;
    }
    
    public OntModel iniciar(String ruta) {
        model = null;
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        java.io.InputStream in = FileManager.get().open(ruta + "resources/owl/OntologiaIntancia.owl");
        if (in == null) {
            throw new IllegalArgumentException("Archivo no encontrado");
        }
        model.read(in, "");
        return model;
    }
    
    public void Onotologia(){     
    }
}
