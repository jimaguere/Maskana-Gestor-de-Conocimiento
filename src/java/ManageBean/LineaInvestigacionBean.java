/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import facadePojo.DepartamentoFacade;
import facadePojo.LineainvestigacionFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Lineainvestigacion;

/**
 *
 * @author mateo
 */
@ManagedBean(name="lineaInvestigacionBean")
@SessionScoped
public class LineaInvestigacionBean {
    private List<Lineainvestigacion>listaLineaInvestigacion;
    private List<Lineainvestigacion>listaLineaInvestigacionSelecionada;
    private Lineainvestigacion lineaInvestigacionSelecionada;
    @EJB
    LineainvestigacionFacade lineaInvestigacionFacade;
    @EJB
    DepartamentoFacade departamentoFacade;
    private String codigoLinea;
    private String nombre;
    private String descripcion;
    private String codDep;
    private String clase="Linea_investigacion";

    public String getCodigoLinea() {
        return codigoLinea;
    }

    public void setCodigoLinea(String codigoLinea) {
        this.codigoLinea = codigoLinea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }
    

    public List<Lineainvestigacion> getListaLineaInvestigacion() {
        return listaLineaInvestigacion;
    }

    public void setListaLineaInvestigacion(List<Lineainvestigacion> listaLineaInvestigacion) {
        this.listaLineaInvestigacion = listaLineaInvestigacion;
    }

    public List<Lineainvestigacion> getListaLineaInvestigacionSelecionada() {
        return listaLineaInvestigacionSelecionada;
    }

    public void setListaLineaInvestigacionSelecionada(List<Lineainvestigacion> listaLineaInvestigacionSelecionada) {
        this.listaLineaInvestigacionSelecionada = listaLineaInvestigacionSelecionada;
    }

    public Lineainvestigacion getLineaInvestigacionSelecionada() {
        return lineaInvestigacionSelecionada;
    }

    public void setLineaInvestigacionSelecionada(Lineainvestigacion lineaInvestigacionSelecionada) {
        this.lineaInvestigacionSelecionada = lineaInvestigacionSelecionada;
    }

    public LineainvestigacionFacade getLineaInvestigacionFacade() {
        return lineaInvestigacionFacade;
    }

    public void setLineaInvestigacionFacade(LineainvestigacionFacade lineaInvestigacionFacade) {
        this.lineaInvestigacionFacade = lineaInvestigacionFacade;
    }
    
    /**
     * Creates a new instance of LineaInvestigacionBean
     */
    public LineaInvestigacionBean() {
       
    }
    @PostConstruct
    public void reset(){
        listaLineaInvestigacion=this.lineaInvestigacionFacade.findAll();
        lineaInvestigacionSelecionada=null;
        listaLineaInvestigacionSelecionada=null;
        codDep="";
        nombre="";
        codigoLinea="";
        descripcion="";  
    }
    public void nueva(){
        Lineainvestigacion lineaInvestigacion=new Lineainvestigacion();
        lineaInvestigacion.setCodigoLinea(codigoLinea);
        lineaInvestigacion.setDescripcion(descripcion.toUpperCase());
        lineaInvestigacion.setNombre(nombre.toUpperCase());
        lineaInvestigacion.setCodDep(this.departamentoFacade.findByCodep(codDep).get(0));
        try{
            this.lineaInvestigacionFacade.create(lineaInvestigacion);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseLin = modelo.getOntClass(nS + clase);
            Individual linea = modelo.createIndividual(nS + clase + codigoLinea, claseLin);
            Individual departamento = modelo.getIndividual(nS + "Departamento" + codDep);
            DatatypeProperty codigo_lin = modelo.getDatatypeProperty(nS + "codigo_linea");
            DatatypeProperty nombre_lin = modelo.getDatatypeProperty(nS + "nombre_linea");
            DatatypeProperty desc = modelo.getDatatypeProperty(nS + "descripcion");
            ObjectProperty pertenece = modelo.getObjectProperty(nS + "Pertenece_a");
            linea.setPropertyValue(codigo_lin, modelo.createTypedLiteral(lineaInvestigacion.getCodigoLinea()));
            linea.setPropertyValue(nombre_lin, modelo.createTypedLiteral(lineaInvestigacion.getNombre()));
            linea.setPropertyValue(desc, modelo.createTypedLiteral(lineaInvestigacion.getDescripcion()));
            linea.setPropertyValue(pertenece, departamento);
            departamento.addProperty(pertenece.getInverse(), linea);
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar la linea ", ""));
        }
    }
    public void modificar(){
        String codigoDepartamento="";
        boolean cambio=false;
        try{
           lineaInvestigacionSelecionada.setDescripcion(lineaInvestigacionSelecionada.getDescripcion().toUpperCase());
           lineaInvestigacionSelecionada.setNombre(lineaInvestigacionSelecionada.getNombre().toUpperCase());
           if(!lineaInvestigacionSelecionada.getCodDep().getCodDep().equals(codDep)){
               codigoDepartamento=lineaInvestigacionSelecionada.getCodDep().getCodDep();
               lineaInvestigacionSelecionada.setCodDep(this.departamentoFacade.findByCodep(codDep).get(0));
               cambio=true; 
            }
            this.lineaInvestigacionFacade.edit(lineaInvestigacionSelecionada);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();   
            Individual linea = modelo.getIndividual(nS + clase + lineaInvestigacionSelecionada.getCodigoLinea());
            DatatypeProperty codigo_lin = modelo.getDatatypeProperty(nS + "codigo_linea");
            DatatypeProperty nombre_lin = modelo.getDatatypeProperty(nS + "nombre_linea");
            DatatypeProperty desc = modelo.getDatatypeProperty(nS + "descripcion");
            if(cambio){
                ObjectProperty pertenece = modelo.getObjectProperty(nS + "Pertenece_a");
                Individual departamento = modelo.getIndividual(nS + "Departamento" + codigoDepartamento);
                linea.removeProperty(pertenece,departamento);
                departamento.removeProperty(pertenece.getInverse(), linea);
                departamento = modelo.getIndividual(nS + "Departamento" + codDep);
                linea.setPropertyValue(pertenece, departamento);
                departamento.addProperty(pertenece.getInverse(), linea);           
            }
            linea.setPropertyValue(codigo_lin, modelo.createTypedLiteral(lineaInvestigacionSelecionada.getCodigoLinea()));
            linea.setPropertyValue(nombre_lin, modelo.createTypedLiteral(lineaInvestigacionSelecionada.getNombre()));
            linea.setPropertyValue(desc, modelo.createTypedLiteral(lineaInvestigacionSelecionada.getDescripcion()));
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Linea de investigación Modificada", ""));
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar la linea ", ""));
        }
    }
    public void eliminar(){
        try{
            this.lineaInvestigacionFacade.remove(lineaInvestigacionSelecionada);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseLin = modelo.getOntClass(nS + clase);
            ObjectProperty pertenece = modelo.getObjectProperty(nS + "Pertenece_a");
            Individual departamento = modelo.getIndividual(nS + "Departamento" + lineaInvestigacionSelecionada.getCodDep().getCodDep());
            departamento.removeProperty(pertenece.getInverse(),modelo.getIndividual(nS + clase + lineaInvestigacionSelecionada.getCodigoLinea()));
            modelo.getIndividual(nS + clase + lineaInvestigacionSelecionada.getCodigoLinea()).remove();
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Linea de investigación Eliminada", ""));            
        }catch(Exception e){
             System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar la linea", ""));
        }
    }
}
