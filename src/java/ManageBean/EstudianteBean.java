/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import facadePojo.EstudianteFacade;
import facadePojo.ProgramaFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Estudiante;

/**
 *
 * @author mateo
 */
@ManagedBean(name="estudianteBean")
@SessionScoped
public class EstudianteBean {
    private List<Estudiante>listaEstudiante;   
    private Estudiante estudianteSelecionado;
    @EJB
    EstudianteFacade estudianteFacade;
    @EJB
    ProgramaFacade programaFacade;
    private String codestudiante;
    private String nombres;
    private String codProg;
    private String apellidos;
    private String identificacion;
    private Date fechaGrado; 
    private String busqueda;
    int li;
    int sup;
    int antLi;
    int limite;
    boolean cambio;
    private static String  clase="Estudiante";
    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }
   
    
    public int getSup() {
        return sup;
    }

    public void setSup(int sup) {
        this.sup = sup;
    }

    public int getLi() {
        return li;
    }

    public void setLi(int li) {
        this.li = li;
    }
    

    public List<Estudiante> getListaEstudiante() {
        return listaEstudiante;
    }

    public void setListaEstudiante(List<Estudiante> listaEstudiante) {
        this.listaEstudiante = listaEstudiante;
    }

   

    public Estudiante getEstudianteSelecionado() {
        return estudianteSelecionado;
    }

    public void setEstudianteSelecionado(Estudiante estudianteSelecionado) {
        this.estudianteSelecionado = estudianteSelecionado;
    }

    public EstudianteFacade getEstudianteFacade() {
        return estudianteFacade;
    }

    public void setEstudianteFacade(EstudianteFacade estudianteFacade) {
        this.estudianteFacade = estudianteFacade;
    }

    public String getCodestudiante() {
        return codestudiante;
    }

    public void setCodestudiante(String codestudiante) {
        this.codestudiante = codestudiante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public Date getFechaGrado() {
        return fechaGrado;
    }

    public void setFechaGrado(Date fechaGrado) {
        this.fechaGrado = fechaGrado;
    }
    
    /**
     * Creates a new instance of EstudianteBean
     */
    public EstudianteBean() {
       
    }
    public void sig() {
        
       if(cambio){
           listaEstudiante=this.estudianteFacade.findByRango(li,10);
           antLi=li;
           li=li+10;
           limite=limite+10;
           if(li>=sup){
               li=sup;
               limite=sup;
           }
     
       }else{
           listaEstudiante=this.estudianteFacade.findByRango(li+10,10);
           li=li+20;
           limite=limite+10;
            if(li>=sup){
               li=sup;
               limite=sup;
           }
           System.out.println("cambio");
       }
        cambio = true; 
       
    }

    public void ant() {
        if (li == sup) {
            listaEstudiante = this.estudianteFacade.findByRango(antLi - 10, 10);
            li=antLi-10;
            limite=antLi;
            
        } else {
            if (!cambio) {
                listaEstudiante = this.estudianteFacade.findByRango(li - 10, 10);
                li = li - 10;
                limite = limite - 10;
            } else {
                listaEstudiante = this.estudianteFacade.findByRango(li - 20, 10);
                li = li - 20;
                limite = limite - 10;
                System.out.println("cambio");
            }
        }
        cambio = false;
    }

    @PostConstruct
    public void reset(){
        listaEstudiante=this.estudianteFacade.findByRango(0,10);
        cambio=true;
        li=10;
        limite=10;
        sup=estudianteFacade.count();
        this.codestudiante="";
        this.apellidos="";
        this.busqueda="";
        this.codProg="";
        this.identificacion="";
        this.fechaGrado=null;
        this.nombres="";
        estudianteSelecionado=null;
    }
    public void buscar(){
        if(!busqueda.isEmpty()){
            List<Object[]>lista=this.estudianteFacade.findAllJaro(busqueda);
            listaEstudiante=new ArrayList<Estudiante>();
            for(int i=0;i<lista.size();i++){
                listaEstudiante.add(this.estudianteFacade.find(lista.get(i)[2]));
            }
        }
    }
    public void nuevo(){
        Estudiante estudiante=new Estudiante();
        estudiante.setNombres(nombres.toUpperCase());
        estudiante.setApellidos(apellidos.toUpperCase());
        estudiante.setCodestudiante(codestudiante);
        estudiante.setIdentificacion(identificacion);
        estudiante.setFechaGrado(fechaGrado);
        estudiante.setPrograma(programaFacade.findByCodProg(codProg).get(0));
        try {
            this.estudianteFacade.create(estudiante);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseEst = modelo.getOntClass(nS + clase);
            Individual est = modelo.createIndividual(nS + clase + codestudiante, claseEst);
            Individual programa = modelo.getIndividual(nS + "Programa" + codProg);
            DatatypeProperty codigo_estudiante = modelo.getDatatypeProperty(nS + "codigo_estudiante");
            DatatypeProperty nombre_estudiante = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty apellido_estudiante = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty identificacion_estudiante = modelo.getDatatypeProperty(nS + "identificacion_persona");
            DatatypeProperty fecha_grado = modelo.getDatatypeProperty(nS + "fecha_grado");
            ObjectProperty estudia = modelo.getObjectProperty(nS + "Pertenece");
            est.setPropertyValue(nombre_estudiante, modelo.createTypedLiteral(estudiante.getNombres()));
            est.setPropertyValue(apellido_estudiante, modelo.createTypedLiteral(estudiante.getApellidos()));
            est.setPropertyValue(identificacion_estudiante, modelo.createTypedLiteral(estudiante.getIdentificacion()));
            est.setPropertyValue(codigo_estudiante, modelo.createTypedLiteral(estudiante.getCodestudiante()));
            est.setPropertyValue(fecha_grado, modelo.createTypedLiteral(estudiante.getFechaGrado(), XSDDatatype.XSDdate));
            est.addProperty(estudia, programa);
            programa.addProperty(estudia.getInverse(), est);
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
            
        } catch (Exception e){
             System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar estudiante ", ""));
        }
        
        
    }
    public void modificar(){
        String codigoPrograma = "";
        boolean cambio = false;
        try{
            estudianteSelecionado.setApellidos(estudianteSelecionado.getApellidos().toUpperCase());
            estudianteSelecionado.setNombres(estudianteSelecionado.getNombres().toUpperCase());
            if(!estudianteSelecionado.getPrograma().getCodProg().equals(codProg)){
                cambio=true;
                codigoPrograma=estudianteSelecionado.getPrograma().getCodProg();
                estudianteSelecionado.setPrograma(programaFacade.findByCodProg(codProg).get(0));
            }
            this.estudianteFacade.edit(estudianteSelecionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseEst = modelo.getOntClass(nS + clase);
            Individual est = modelo.createIndividual(nS + clase + estudianteSelecionado.getCodestudiante(), claseEst);
            DatatypeProperty nombre_estudiante = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty apellido_estudiante = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty identificacion_estudiante = modelo.getDatatypeProperty(nS + "identificacion_persona");
            DatatypeProperty fecha_grado = modelo.getDatatypeProperty(nS + "fecha_grado");
            est.setPropertyValue(nombre_estudiante, modelo.createTypedLiteral(estudianteSelecionado.getNombres()));
            est.setPropertyValue(apellido_estudiante, modelo.createTypedLiteral(estudianteSelecionado.getApellidos()));
            est.setPropertyValue(identificacion_estudiante, modelo.createTypedLiteral(estudianteSelecionado.getIdentificacion()));
            est.setPropertyValue(fecha_grado, modelo.createTypedLiteral(estudianteSelecionado.getFechaGrado(), XSDDatatype.XSDdate));
            if(cambio){
                 ObjectProperty estudia = modelo.getObjectProperty(nS + "Pertenece");
                 Individual programa = modelo.getIndividual(nS + "Programa" + codigoPrograma);
                 est.removeProperty(estudia, programa);
                 programa.removeProperty(estudia.getInverse(), est);
                 programa = modelo.getIndividual(nS + "Programa" + estudianteSelecionado.getPrograma().getCodProg());
                 est.setPropertyValue(estudia, programa);
                 programa.addProperty(estudia.getInverse(), est);
            }
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Estudiante Modificado Exitosamente", ""));
            
        }catch(Exception e){
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar estudiante ", ""));
        }
    }
    public void eliminar(){
        try{
            this.estudianteFacade.remove(estudianteSelecionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseEst = modelo.getOntClass(nS + clase);
            Individual est = modelo.createIndividual(nS + clase + estudianteSelecionado.getCodestudiante(), claseEst);
            Individual programa = modelo.getIndividual(nS + "Programa" + estudianteSelecionado.getPrograma().getCodProg());
            ObjectProperty estudia = modelo.getObjectProperty(nS + "Pertenece");
            programa.removeProperty(estudia.getInverse(), est);
            modelo.createIndividual(nS + clase + estudianteSelecionado.getCodestudiante(), claseEst).remove();
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage("estudiante eliminado con exito ", ""));
        }catch(Exception e){
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar estudiante ", ""));
        }
    }
}
