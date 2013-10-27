/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import facadePojo.DepartamentoFacade;
import facadePojo.ModalidadFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
    @EJB
    DepartamentoFacade departamentoFacade;
    @EJB
    ModalidadFacade modalidadFacade;
    private String clase="Modalidad";

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
    public void nuevo(){
        
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
