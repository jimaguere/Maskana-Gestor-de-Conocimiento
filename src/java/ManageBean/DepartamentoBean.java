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
import facadePojo.FacultadFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Departamento;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "departamentoBean")
@SessionScoped
public class DepartamentoBean {

    private List<Departamento> listaDepartamento;
    private List<Departamento> listaSelecionada;
    private Departamento departamentoSelecionado;
    private String codDep;
    private String nomDep;
    private String codFac;
    private String clase = "Departamento";
    @EJB
    DepartamentoFacade departamentoFacade;
    @EJB
    FacultadFacade facultadFacade;

    public FacultadFacade getFacultadFacade() {
        return facultadFacade;
    }

    public void setFacultadFacade(FacultadFacade facultadFacade) {
        this.facultadFacade = facultadFacade;
    }

    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public List<Departamento> getListaSelecionada() {
        return listaSelecionada;
    }

    public void setListaSelecionada(List<Departamento> listaSelecionada) {
        this.listaSelecionada = listaSelecionada;
    }

    public Departamento getDepartamentoSelecionado() {
        return departamentoSelecionado;
    }

    public void setDepartamentoSelecionado(Departamento departamentoSelecionado) {
        this.departamentoSelecionado = departamentoSelecionado;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }

    public String getNomDep() {
        return nomDep;
    }

    public void setNomDep(String nomDep) {
        this.nomDep = nomDep;
    }

    public String getCodFac() {
        return codFac;
    }

    public void setCodFac(String codFac) {
        this.codFac = codFac;
    }

    public DepartamentoFacade getDepartamentoFacade() {
        return departamentoFacade;
    }

    public void setDepartamentoFacade(DepartamentoFacade departamentoFacade) {
        this.departamentoFacade = departamentoFacade;
    }

    public void nuevo() {
        Departamento departamentoNuevo = new Departamento();
        departamentoNuevo.setCodDep(codDep);
        departamentoNuevo.setNombre(nomDep.toUpperCase());
        departamentoNuevo.setCodFac(facultadFacade.findByCodigo(codFac).get(0));
        try {
            this.departamentoFacade.create(departamentoNuevo);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseDep = modelo.getOntClass(nS + clase);
            Individual departamento = modelo.createIndividual(nS + clase + codDep, claseDep);
            Individual facultad = modelo.getIndividual(nS + "Facultad" + codFac);
            DatatypeProperty codigo_dep = modelo.getDatatypeProperty(nS + "codigo_departamento");
            DatatypeProperty nombre_dep = modelo.getDatatypeProperty(nS + "nombre_departamento");
            DatatypeProperty codigo = modelo.getDatatypeProperty(nS + "codigo_facultad");
            DatatypeProperty nomFac = modelo.getDatatypeProperty(nS + "nombre_facultad");
            ObjectProperty pertenece = modelo.getObjectProperty(nS + "es_inscrito");
            departamento.setPropertyValue(codigo_dep, modelo.createTypedLiteral(departamentoNuevo.getCodDep()));
            departamento.setPropertyValue(nombre_dep, modelo.createTypedLiteral(departamentoNuevo.getNombre()));
            departamento.setPropertyValue(codigo, modelo.createTypedLiteral(departamentoNuevo.getCodFac().getCodFac()));
            departamento.setPropertyValue(nomFac, modelo.createTypedLiteral(departamentoNuevo.getCodFac().getNombre()));
            departamento.setPropertyValue(pertenece, facultad);
            facultad.addProperty(pertenece.getInverse(), departamento);
            ont.guardarModelo(modelo);
            codDep = "";
            nomDep = "";
            codFac = "";
            listaDepartamento = this.departamentoFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar departamento ", ""));
        }

    }

    public void eliminar() {
        try {
            this.departamentoFacade.remove(departamentoSelecionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            ObjectProperty pertenece = modelo.getObjectProperty(nS + "es_inscrito");
            Individual facultad = modelo.getIndividual(nS + "Facultad" + departamentoSelecionado.getCodFac().getCodFac());
            facultad.removeProperty(pertenece.getInverse(), modelo.getIndividual(nS + clase + departamentoSelecionado.getCodDep()));
            modelo.getIndividual(nS + clase + departamentoSelecionado.getCodDep()).remove();
            ont.guardarModelo(modelo);
            departamentoSelecionado = null;
            listaSelecionada = null;
            listaDepartamento = this.departamentoFacade.findAll();
            codFac = "";
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema al eliminar el departamento", ""));
        }
    }

    public void modificar() {
        String codigoFacultad = "";
        boolean cambio = false;
        try {
            departamentoSelecionado.setNombre(departamentoSelecionado.getNombre().toUpperCase());
            if (!departamentoSelecionado.getCodFac().getCodFac().equals(codFac)) {
                codigoFacultad = departamentoSelecionado.getCodFac().getCodFac();
                departamentoSelecionado.setCodFac(facultadFacade.findByCodigo(codFac).get(0));
                cambio = true;
            }
            departamentoFacade.edit(departamentoSelecionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseDep = modelo.getOntClass(nS + clase);
            Individual departamento = modelo.createIndividual(nS + clase + departamentoSelecionado.getCodDep(), claseDep);
            DatatypeProperty codigo_dep = modelo.getDatatypeProperty(nS + "codigo_departamento");
            DatatypeProperty nombre_dep = modelo.getDatatypeProperty(nS + "nombre_departamento");
            DatatypeProperty codigo = modelo.getDatatypeProperty(nS + "codigo_facultad");
            DatatypeProperty nomFac = modelo.getDatatypeProperty(nS + "nombre_facultad");
            ObjectProperty pertenece = modelo.getObjectProperty(nS + "es_inscrito");
            if (cambio) {
                Individual facultad = modelo.getIndividual(nS + "Facultad" + codigoFacultad);
                departamento.removeProperty(pertenece, facultad);
                facultad.removeProperty(pertenece.getInverse(), departamento);
                facultad = modelo.getIndividual(nS + "Facultad" + departamentoSelecionado.getCodFac().getCodFac());
                departamento.setPropertyValue(pertenece, facultad);
                facultad.addProperty(pertenece.getInverse(), departamento);
            }
            departamento.setPropertyValue(codigo_dep, modelo.createTypedLiteral(departamentoSelecionado.getCodDep()));
            departamento.setPropertyValue(nombre_dep, modelo.createTypedLiteral(departamentoSelecionado.getNombre()));
            departamento.setPropertyValue(codigo, modelo.createTypedLiteral(departamentoSelecionado.getCodFac().getCodFac()));
            departamento.setPropertyValue(nomFac, modelo.createTypedLiteral(departamentoSelecionado.getCodFac().getNombre()));
            ont.guardarModelo(modelo);
            departamentoSelecionado = null;
            listaSelecionada = null;
            listaDepartamento = this.departamentoFacade.findAll();
            codFac = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema al modificar el departamento", ""));
        }
    }

    /**
     * Creates a new instance of DepartamentoBean
     */
    public DepartamentoBean() {
    }

    @PostConstruct
    public void reset() {
        listaDepartamento = this.departamentoFacade.findAll();
    }
}
