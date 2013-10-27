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
import facadePojo.ModalidadFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Modalidad;

/**
 *
 * @author mateo
 */
@ManagedBean(name="modalidadBean")
@SessionScoped
public class ModalidadBean {
    
    private List<Modalidad>listaMadalidades;
    private List<Modalidad>listaModalidadesSeleccionda;
    private Modalidad modalidadSeleccionada;
    private String codModalidad;
    private String nombreModalidad;
    private String codDep;
 
    @EJB
    DepartamentoFacade departamentoFacade;
    @EJB
    ModalidadFacade modalidadFacade;
    private String clase="Modalidad";

    public String getNombreModalidad() {
        return nombreModalidad;
    }

    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }

    public String getCodModalidad() {
        return codModalidad;
    }

    public void setCodModalidad(String codModalidad) {
        this.codModalidad = codModalidad;
    }



    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }
    
    public List<Modalidad> getListaMadalidades() {
        return listaMadalidades;
    }

    public void setListaMadalidades(List<Modalidad> listaMadalidades) {
        this.listaMadalidades = listaMadalidades;
    }

    public List<Modalidad> getListaModalidadesSeleccionda() {
        return listaModalidadesSeleccionda;
    }

    public void setListaModalidadesSeleccionda(List<Modalidad> listaModalidadesSeleccionda) {
        this.listaModalidadesSeleccionda = listaModalidadesSeleccionda;
    }

    public Modalidad getModalidadSeleccionada() {
        return modalidadSeleccionada;
    }

    public void setModalidadSeleccionada(Modalidad modalidadSeleccionada) {
        this.modalidadSeleccionada = modalidadSeleccionada;
    }

    public DepartamentoFacade getDepartamentoFacade() {
        return departamentoFacade;
    }

    public void setDepartamentoFacade(DepartamentoFacade departamentoFacade) {
        this.departamentoFacade = departamentoFacade;
    }

    public ModalidadFacade getModalidadFacade() {
        return modalidadFacade;
    }

    public void setModalidadFacade(ModalidadFacade modalidadFacade) {
        this.modalidadFacade = modalidadFacade;
    }

    public void nuevo() {
        Modalidad modalidad = new Modalidad();
        modalidad.setCodDep(this.departamentoFacade.findByCodep(codDep).get(0));
        modalidad.setCodModalidad(codModalidad);
        modalidad.setModalidad(nombreModalidad.toUpperCase());
        try {
            this.modalidadFacade.create(modalidad);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseMod = modelo.getOntClass(nS + clase);
            Individual modalidadI = modelo.createIndividual(nS + clase + codModalidad, claseMod);
            Individual departamentoI = modelo.getIndividual(nS + "Departamento" + codDep);
            DatatypeProperty codigo_mod = modelo.getDatatypeProperty(nS + "codigo_modalidad");
            DatatypeProperty nombre_mod = modelo.getDatatypeProperty(nS + "nombre_modalidad");
            ObjectProperty pertenece = modelo.getObjectProperty(nS + "Es_del_departamento");
            modalidadI.setPropertyValue(codigo_mod, modelo.createTypedLiteral(modalidad.getCodModalidad()));
            modalidadI.setPropertyValue(nombre_mod, modelo.createTypedLiteral(modalidad.getModalidad()));
            modalidadI.setPropertyValue(pertenece, departamentoI);
            departamentoI.addProperty(pertenece.getInverse(), modalidadI);
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
        } catch (Exception e) {
             System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar la modalidad ", ""));
        }
    }

    public void modificar() {
        String codigoDepartamento = "";
        boolean cambio = false;
        try {
            modalidadSeleccionada.setModalidad(modalidadSeleccionada.getModalidad().toUpperCase());
            if(!modalidadSeleccionada.getCodDep().getCodDep().equals(codDep)){
                codigoDepartamento =modalidadSeleccionada.getCodDep().getCodDep();
                modalidadSeleccionada.setCodDep(this.departamentoFacade.findByCodep(codDep).get(0));
                cambio=true;
            }
            this.modalidadFacade.edit(modalidadSeleccionada);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            Individual modalidadI = modelo.getIndividual(nS + clase + modalidadSeleccionada.getCodModalidad());
            DatatypeProperty codigo_mod = modelo.getDatatypeProperty(nS + "codigo_modalidad");
            DatatypeProperty nombre_mod = modelo.getDatatypeProperty(nS + "nombre_modalidad");
            modalidadI.setPropertyValue(codigo_mod, modelo.createTypedLiteral(modalidadSeleccionada.getCodModalidad()));
            modalidadI.setPropertyValue(nombre_mod, modelo.createTypedLiteral(modalidadSeleccionada.getModalidad()));
            if(cambio){
                ObjectProperty pertenece = modelo.getObjectProperty(nS + "Es_del_departamento");
                Individual departamento = modelo.getIndividual(nS + "Departamento" + codigoDepartamento);
                modalidadI.removeProperty(pertenece, departamento);
                departamento.removeProperty(pertenece.getInverse(), modalidadI);
                departamento = modelo.getIndividual(nS + "Departamento" + codDep);
                modalidadI.setPropertyValue(pertenece, departamento);
                departamento.addProperty(pertenece.getInverse(), modalidadI);
                
            }
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("modalidad Modificada", ""));
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar modalidad ", ""));
        }


    }

    /**
     * Creates a new instance of ModalidadBean
     */
    public ModalidadBean() {
    }
    @PostConstruct
    public void reset(){
        listaMadalidades=this.modalidadFacade.findAll();
        this.codDep="";
        this.nombreModalidad="";
        this.codModalidad="";
        this.modalidadSeleccionada=null;
        this.listaModalidadesSeleccionda=null;
    }
}
