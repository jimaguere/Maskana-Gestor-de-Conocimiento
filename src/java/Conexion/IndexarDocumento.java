/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import ManageBean.ModeloBean;
import ManageBean.ModeloBean;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import facadePojo.VocabularioFacade;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import pojo.Estudiante;
import pojo.Tgautor;
import pojo.TrabajosGrado;

/**
 *
 * @author mateo
 */
public class IndexarDocumento {

    private String titulo;
    private String contenido;
    private TrabajosGrado trabajoGrado;
    private ModeloBean ont;
    private final String clase = "Trabajo_grado";
    private VocabularioFacade vocabularioFacadel;
    private Directory directory;

    public VocabularioFacade getVocabularioFacadel() {
        return vocabularioFacadel;
    }

    public void setVocabularioFacadel(VocabularioFacade vocabularioFacadel) {
        this.vocabularioFacadel = vocabularioFacadel;
    }

    public ModeloBean getOnt() {
        return ont;
    }

    public void setOnt(ModeloBean ont) {
        this.ont = ont;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public TrabajosGrado getTrabajoGrado() {
        return trabajoGrado;
    }

    public void setTrabajoGrado(TrabajosGrado trabajoGrado) {
        this.trabajoGrado = trabajoGrado;
    }

    public void crearDocuemntoOntologia() throws FileNotFoundException, IOException {
        java.util.Date fecha = new Date(System.currentTimeMillis());
        String nS = ont.getPrefijo();
        OntModel modelo = ont.getModelOnt();
        OntClass tGrado = modelo.getOntClass(nS + clase);
        //trabajo grado
        String vocabularioPersonas = "";
        Individual tg = modelo.createIndividual(nS + "tg" + trabajoGrado.getIdTg(), tGrado);
        DatatypeProperty idTg = modelo.getDatatypeProperty(nS + "id_trabajo");
        DatatypeProperty tituloT = modelo.getDatatypeProperty(nS + "titulo");
        DatatypeProperty fechaD = modelo.getDatatypeProperty(nS + "fecha_sustentacion");
        tg.setPropertyValue(idTg, modelo.createTypedLiteral(trabajoGrado.getIdTg().toString(), XSDDatatype.XSDint));
        tg.setPropertyValue(tituloT, modelo.createTypedLiteral(trabajoGrado.getTitulo()));
        tg.setPropertyValue(fechaD, modelo.createTypedLiteral(fecha, XSDDatatype.XSDdate));
        OntClass claseprog = modelo.getOntClass(nS + "Programa");
        Individual prog = modelo.createIndividual(nS + "Programa" + trabajoGrado.getTgautorList().get(0)
                .getEstudiante().getPrograma().getCodProg(), claseprog);
        //autores
        List<Tgautor> autores = trabajoGrado.getTgautorList();
        for (Tgautor autorTg : autores) {
            Estudiante autor = autorTg.getEstudiante();
            OntClass estudiante = modelo.getOntClass(nS + "Estudiante");
            Individual est = modelo.createIndividual(nS + "Estudiante" + autor.getId(), estudiante);
            DatatypeProperty nombre_e = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty apellido_e = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty ide_e = modelo.getDatatypeProperty(nS + "identificacion_persona");
            DatatypeProperty codigo_e = modelo.getDatatypeProperty(nS + "codigo_estudiante");
            DatatypeProperty grado_e = modelo.getDatatypeProperty(nS + "fecha_grado");
            ObjectProperty realiza = modelo.getObjectProperty(nS + "Realiza");
            ObjectProperty pert = modelo.getObjectProperty(nS + "Pertenece");
            est.setPropertyValue(nombre_e, modelo.createTypedLiteral(autor.getNombres()));
            est.setPropertyValue(apellido_e, modelo.createTypedLiteral(autor.getApellidos()));
            est.setPropertyValue(ide_e, modelo.createTypedLiteral(""+autor.getId()));
            est.setPropertyValue(codigo_e, modelo.createTypedLiteral(""+autor.getId()));
            est.addProperty(realiza, tg);
            tg.addProperty(realiza.getInverse(), est);
            est.addProperty(pert, prog);
            prog.addProperty(pert.getInverse(), est);
            vocabularioPersonas = vocabularioPersonas + autor.getNombres() + " " + autor.getApellidos() + " ";
        }
        //acesores
        List<pojo.Docente> asesores = trabajoGrado.getDocenteList();
        for (pojo.Docente asesor : asesores) {
            OntClass docente = modelo.getOntClass(nS + "Docente");
            Individual doc = modelo.createIndividual(nS + "Docente" + asesor.getId(), docente);
            DatatypeProperty nombre_d = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty apellido_d = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty ide_d = modelo.getDatatypeProperty(nS + "identificacion_persona");
            ObjectProperty dirige = modelo.getObjectProperty(nS + "Dirige");
            doc.setPropertyValue(ide_d, modelo.createTypedLiteral(""+asesor.getId()));
            doc.setPropertyValue(nombre_d, modelo.createTypedLiteral(asesor.getNombre()));
            doc.setPropertyValue(apellido_d, modelo.createTypedLiteral(asesor.getApellido()));
            doc.addProperty(dirige, tg);
            tg.addProperty(dirige.getInverse(), doc);
            vocabularioPersonas = vocabularioPersonas + " " + asesor.getNombre() + " " + asesor.getApellido() + " ";
        }
        //palabras clave
        String[] keywords = trabajoGrado.getTitulo().trim().split(" ");
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i] == null) {
                continue;
            }
            if (keywords[i].trim().length() == 0) {
                continue;
            }
            if (vocabularioFacadel.findAllStopWords(keywords[i].trim()) == null) {
                vocabularioFacadel.crearVocabulario(keywords[i].toLowerCase());
                OntClass palabra = modelo.getOntClass(nS + "Palabra_clave");
                String cad = depurarTexto(keywords[i].toLowerCase());
                cad = vocabularioFacadel.findLexema(cad.trim());
                Individual pal;
                if (cad != null) {
                    pal = modelo.createIndividual(nS + cad, palabra);
                    DatatypeProperty sinonimo = modelo.getDatatypeProperty(nS + "sinonimo");
                    DatatypeProperty significado = modelo.getDatatypeProperty(nS + "significado");
                    ObjectProperty desc = modelo.getObjectProperty(nS + "describen");
                    pal.addProperty(sinonimo, keywords[i].toLowerCase());
                    pal.setPropertyValue(significado, modelo.createTypedLiteral(keywords[i].toLowerCase()));
                    pal.addProperty(desc, tg);
                    tg.addProperty(desc.getInverse(), pal);
                }
            }
        }
        String vocabularioArray[] = vocabularioPersonas.trim().split(" ");
        for (int i = 0; i < vocabularioArray.length; i++) {
            if (vocabularioArray[i].trim().length() == 0) {
                continue;
            }
            vocabularioFacadel.crearVocabulario(vocabularioArray[i].toLowerCase());
        }
        ont.guardarModelo(modelo);
    }
    
    public void modificarDocumento() throws FileNotFoundException, IOException{
        java.util.Date fecha = new Date(System.currentTimeMillis());
        String nS = ont.getPrefijo();
        OntModel modelo = ont.getModelOnt();
        OntClass tGrado = modelo.getOntClass(nS + clase);
        //trabajo grado
        String vocabularioPersonas = "";
        Individual tg = modelo.createIndividual(nS + "tg" + trabajoGrado.getIdTg(), tGrado);
        DatatypeProperty idTg = modelo.getDatatypeProperty(nS + "id_trabajo");
        DatatypeProperty tituloT = modelo.getDatatypeProperty(nS + "titulo");
        DatatypeProperty fechaD = modelo.getDatatypeProperty(nS + "fecha_sustentacion");
        tg.setPropertyValue(idTg, modelo.createTypedLiteral(trabajoGrado.getIdTg().toString(), XSDDatatype.XSDint));
        tg.setPropertyValue(tituloT, modelo.createTypedLiteral(trabajoGrado.getTitulo()));
        tg.setPropertyValue(fechaD, modelo.createTypedLiteral(fecha, XSDDatatype.XSDdate));
        OntClass claseprog = modelo.getOntClass(nS + "Programa");
        Individual prog = modelo.createIndividual(nS + "Programa" + trabajoGrado.getTgautorList().get(0)
                .getEstudiante().getPrograma().getCodProg(), claseprog);
        //autores
       ObjectProperty sonAutores = modelo.getObjectProperty(nS + "Es_realizado");
       tg.removeAll(sonAutores);
       
       ObjectProperty sonAsesores = modelo.getObjectProperty(nS + "Es_dirigido");
       tg.removeAll(sonAsesores);
        List<Tgautor> autores = trabajoGrado.getTgautorList();
        for (Tgautor autorTg : autores) {
            Estudiante autor = autorTg.getEstudiante();
            OntClass estudiante = modelo.getOntClass(nS + "Estudiante");
            Individual est = modelo.createIndividual(nS + "Estudiante" + autor.getId(), estudiante);
            DatatypeProperty nombre_e = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty apellido_e = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty ide_e = modelo.getDatatypeProperty(nS + "identificacion_persona");
            DatatypeProperty codigo_e = modelo.getDatatypeProperty(nS + "codigo_estudiante");
            DatatypeProperty grado_e = modelo.getDatatypeProperty(nS + "fecha_grado");
            ObjectProperty realiza = modelo.getObjectProperty(nS + "Realiza");
            ObjectProperty pert = modelo.getObjectProperty(nS + "Pertenece");
            est.setPropertyValue(nombre_e, modelo.createTypedLiteral(autor.getNombres()));
            est.setPropertyValue(apellido_e, modelo.createTypedLiteral(autor.getApellidos()));
            est.setPropertyValue(ide_e, modelo.createTypedLiteral(""+autor.getId()));
            est.setPropertyValue(codigo_e, modelo.createTypedLiteral(""+autor.getId()));
            est.addProperty(realiza, tg);
            tg.addProperty(realiza.getInverse(), est);
            est.addProperty(pert, prog);
            prog.addProperty(pert.getInverse(), est);
            vocabularioPersonas = vocabularioPersonas + autor.getNombres() + " " + autor.getApellidos() + " ";
        }
        //acesores
        List<pojo.Docente> asesores = trabajoGrado.getDocenteList1();
        for (pojo.Docente asesor : asesores) {
            OntClass docente = modelo.getOntClass(nS + "Docente");
            Individual doc = modelo.createIndividual(nS + "Docente" + asesor.getId(), docente);
            DatatypeProperty nombre_d = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty apellido_d = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty ide_d = modelo.getDatatypeProperty(nS + "identificacion_persona");
            ObjectProperty dirige = modelo.getObjectProperty(nS + "Dirige");
            doc.setPropertyValue(ide_d, modelo.createTypedLiteral(""+asesor.getId()));
            doc.setPropertyValue(nombre_d, modelo.createTypedLiteral(asesor.getNombre()));
            doc.setPropertyValue(apellido_d, modelo.createTypedLiteral(asesor.getApellido()));
            doc.addProperty(dirige, tg);
            tg.addProperty(dirige.getInverse(), doc);
            vocabularioPersonas = vocabularioPersonas + " " + asesor.getNombre() + " " + asesor.getApellido() + " ";
        }
        //palabras clave
       ObjectProperty describian = modelo.getObjectProperty(nS + "tiene");
       tg.removeAll(describian);
       describian.removeAll(idTg);
       String[] keywords = trabajoGrado.getTitulo().trim().split(" ");
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i] == null) {
                continue;
            }
            if (keywords[i].trim().length() == 0) {
                continue;
            }
      
            if (vocabularioFacadel.findAllStopWords(keywords[i].trim()) == null) {
                vocabularioFacadel.crearVocabulario(keywords[i].toLowerCase());
                OntClass palabra = modelo.getOntClass(nS + "Palabra_clave");
                String cad = depurarTexto(keywords[i].toLowerCase());
                cad = vocabularioFacadel.findLexema(cad.trim());
                Individual pal;
                if (cad != null) {
                    pal = modelo.createIndividual(nS + cad, palabra);
                    DatatypeProperty sinonimo = modelo.getDatatypeProperty(nS + "sinonimo");
                    DatatypeProperty significado = modelo.getDatatypeProperty(nS + "significado");
                    ObjectProperty desc = modelo.getObjectProperty(nS + "describen");
                    pal.addProperty(sinonimo, keywords[i].toLowerCase());
                    pal.setPropertyValue(significado, modelo.createTypedLiteral(keywords[i].toLowerCase()));
                    pal.addProperty(desc, tg);
                    tg.addProperty(desc.getInverse(), pal);
                }
            }
        }
        String vocabularioArray[] = vocabularioPersonas.trim().split(" ");
        for (int i = 0; i < vocabularioArray.length; i++) {
            if (vocabularioArray[i].trim().length() == 0) {
                continue;
            }
            vocabularioFacadel.crearVocabulario(vocabularioArray[i].toLowerCase());
        }
        ont.guardarModelo(modelo);
    }

    public String depurarTexto(String texto) {
        String nuevo = texto.replace("\\n", " ").replace("“", "").replace("–", "").replace("—", "").replace("Œ", "").replace("∗", "")
                .replace(".", "").replace(",", "").replace(";", "").replace("*", "").replace("‑", "").replace("Ò", "")
                .replace("\"", "").replace("®", "").replace("Œ", "").replace("\\r", "");
        return nuevo;
    }

    public void indexarTexto(String ruta) throws IOException {
        //directory = FSDirectory.open(new File(ruta+"resources/Indice"));
        directory = FSDirectory.open(new File(ruta+"resources/Indice"));
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new Field("id", ""+trabajoGrado.getIdTg(), 
                Field.Store.YES, 
                Field.Index.NOT_ANALYZED));
        doc.add(new Field("contenido", ""+this.contenido, 
                Field.Store.NO, 
                Field.Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();
        directory.close();
        System.out.println("indexo");
    }
    public void modificarIndice(String ruta) throws IOException{
        directory = FSDirectory.open(new File(ruta+"resources/Indice"));
        IndexWriter writer = getWriter();
        Document doc = new Document();
        doc.add(new Field("id", ""+trabajoGrado.getIdTg(), 
                Field.Store.YES, 
                Field.Index.NOT_ANALYZED));
        doc.add(new Field("contenido", ""+this.contenido, 
                Field.Store.NO, 
                Field.Index.ANALYZED));
        writer.updateDocument(new Term("id",""+trabajoGrado.getIdTg()),doc);
        writer.close();
        directory.close();
    }

    private IndexWriter getWriter() throws IOException {
        IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_46,new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter indice=new IndexWriter(directory, config);
        return indice;
    }
}
