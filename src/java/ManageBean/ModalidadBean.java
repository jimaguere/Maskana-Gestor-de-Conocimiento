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
import javax.faces.context.FacesContext;
import pojo.Departamento;
import pojo.Modalidad;

/**
 *
 * @author mateo
 */
@ManagedBean(name="modalidadBean")
@RequestScoped
public class ModalidadBean {
    
    private List<Modalidad>listaMadalidades;
    private List<Modalidad>listaModalidadesSeleccionda;
    private Modalidad modalidadSeleccionada;
    private String codModalidad;
    private String modalidad;
    private String codDep;
    private Departamento departamento;
    @EJB
    DepartamentoFacade departamentoFacade;
    @EJB
    ModalidadFacade modalidadFacade;
    private String clase="Modalidad";

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    

    public String getCodModalidad() {
        return codModalidad;
    }

    public void setCodModalidad(String codModalidad) {
        this.codModalidad = codModalidad;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
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
        modalidad.setCodDep(departamento);
        modalidad.setCodModalidad(codModalidad);
        modalidad.setModalidad(codModalidad);
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
            modalidadI.setPropertyValue(nombre_mod, modelo.createTypedLiteral(modalidad.getCodModalidad()));
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

    /**
     * Creates a new instance of ModalidadBean
     */
    public ModalidadBean() {
    }
    @PostConstruct
    public void reset(){
        listaMadalidades=this.modalidadFacade.findAll();
    }
}
