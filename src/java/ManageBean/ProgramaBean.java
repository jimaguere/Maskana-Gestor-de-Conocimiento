/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import facadePojo.DepartamentoFacade;
import facadePojo.FacultadFacade;
import facadePojo.ProgramaFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Departamento;
import pojo.Programa;

/**
 *
 * @author mateo
 */
@ManagedBean(name="programaBean")
@SessionScoped
public class ProgramaBean {
    private List<Programa>listaPrograma;
    private List<Programa>listaSeleccionada;
    private Programa programaSeleccionado;
    private List<Departamento>listaDepartamentos;
    private String codProg;
    private String nombre;
    private String codDep;
    private String codFac;
    private String clase="Programa";
    @EJB
    ProgramaFacade programaFacade;
    @EJB
    FacultadFacade facultadFacade;
    @EJB
    DepartamentoFacade departamentoFacade;

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }
    
    public List<Programa> getListaPrograma() {
        return listaPrograma;
    }

    public void setListaPrograma(List<Programa> listaPrograma) {
        this.listaPrograma = listaPrograma;
    }

    public List<Programa> getListaSeleccionada() {
        return listaSeleccionada;
    }

    public void setListaSeleccionada(List<Programa> listaSeleccionada) {
        this.listaSeleccionada = listaSeleccionada;
    }

    public Programa getProgramaSeleccionado() {
        return programaSeleccionado;
    }

    public void setProgramaSeleccionado(Programa programaSeleccionado) {
        this.programaSeleccionado = programaSeleccionado;
        codFac="";
        codDep="";
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }

    public String getCodFac() {
        return codFac;
    }

    public void setCodFac(String codFac) {
        this.codFac = codFac;
    }

    public ProgramaFacade getProgramaFacade() {
        return programaFacade;
    }

    public void setProgramaFacade(ProgramaFacade programaFacade) {
        this.programaFacade = programaFacade;
    }

    public void actualizarDepartamentos(){
       // listaDepartamentos=facultadFacade.findByCodigo(codFac).get(0).getDepartamentoList();
        listaDepartamentos=departamentoFacade.findByCodFac(codFac);
    }
    public void tabla(){
        listaPrograma=this.programaFacade.findByCodDep(codDep);
    }
    public void nuevo(){
        Programa programaNuevo = new Programa();
        programaNuevo.setCodProg(codProg);
        programaNuevo.setNombre(nombre.toUpperCase());
        programaNuevo.setCodDep(this.departamentoFacade.findByCodep(codDep).get(0));
        try {
            this.programaFacade.create(programaNuevo);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseProg = modelo.getOntClass(nS + clase);
            Individual programa = modelo.createIndividual(nS + clase + codProg, claseProg);
            Individual facultad = modelo.getIndividual(nS + "Departamento" + codDep);
            DatatypeProperty codigo_prog = modelo.getDatatypeProperty(nS + "codigo_programa");
            DatatypeProperty nombre_prog = modelo.getDatatypeProperty(nS + "nombre_programa");
            DatatypeProperty codigo_dep = modelo.getDatatypeProperty(nS + "codigo_departamento");
            DatatypeProperty nombre_dep = modelo.getDatatypeProperty(nS + "nombre_departamento");
            DatatypeProperty codigo = modelo.getDatatypeProperty(nS + "codigo_facultad");
            DatatypeProperty nomFac = modelo.getDatatypeProperty(nS + "nombre_facultad");
            programa.setPropertyValue(codigo_dep, modelo.createTypedLiteral(programaNuevo.getCodDep().getCodDep()));
            programa.setPropertyValue(nombre_dep, modelo.createTypedLiteral(programaNuevo.getCodDep().getNombre()));
            programa.setPropertyValue(codigo, modelo.createTypedLiteral(programaNuevo.getCodDep().getCodFac().getCodFac()));
            programa.setPropertyValue(nomFac, modelo.createTypedLiteral(programaNuevo.getCodDep().getCodFac().getNombre()));
            programa.setPropertyValue(codigo_prog,modelo.createTypedLiteral(programaNuevo.getCodProg()));
            programa.setPropertyValue(nombre_prog,modelo.createTypedLiteral(programaNuevo.getNombre()));
            ont.guardarModelo(modelo);
            codFac="";
            codProg="";
            nombre="";
            codDep="";
            listaPrograma=this.programaFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar programa ", ""));
        }
    }
    public void modificar() {
        try {
            programaSeleccionado.setNombre(programaSeleccionado.getNombre().toUpperCase());
            programaSeleccionado.setCodDep(this.departamentoFacade.findByCodep(codDep).get(0));
            this.programaFacade.edit(programaSeleccionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseProg = modelo.getOntClass(nS + clase);
            Individual programa = modelo.createIndividual(nS + clase + programaSeleccionado.getCodProg(), claseProg);
            Individual facultad = modelo.getIndividual(nS + "Departamento" + programaSeleccionado.getCodDep().getCodDep());
            DatatypeProperty codigo_prog = modelo.getDatatypeProperty(nS + "codigo_programa");
            DatatypeProperty nombre_prog = modelo.getDatatypeProperty(nS + "nombre_programa");
            DatatypeProperty codigo_dep = modelo.getDatatypeProperty(nS + "codigo_departamento");
            DatatypeProperty nombre_dep = modelo.getDatatypeProperty(nS + "nombre_departamento");
            DatatypeProperty codigo = modelo.getDatatypeProperty(nS + "codigo_facultad");
            DatatypeProperty nomFac = modelo.getDatatypeProperty(nS + "nombre_facultad");
            programa.setPropertyValue(codigo_dep, modelo.createTypedLiteral(programaSeleccionado.getCodDep().getCodDep()));
            programa.setPropertyValue(nombre_dep, modelo.createTypedLiteral(programaSeleccionado.getCodDep().getNombre()));
            programa.setPropertyValue(codigo, modelo.createTypedLiteral(programaSeleccionado.getCodDep().getCodFac().getCodFac()));
            programa.setPropertyValue(nomFac, modelo.createTypedLiteral(programaSeleccionado.getCodDep().getCodFac().getNombre()));
            programa.setPropertyValue(codigo_prog,modelo.createTypedLiteral(programaSeleccionado.getCodProg()));
            programa.setPropertyValue(nombre_prog,modelo.createTypedLiteral(programaSeleccionado.getNombre()));
            ont.guardarModelo(modelo);
            programaSeleccionado = null;
            listaSeleccionada = null;
            listaPrograma = this.programaFacade.findAll();
            codFac = "";
            codDep ="";
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar programa ", ""));
        }

    }
    public void eliminar(){
        try{
            this.programaFacade.remove(programaSeleccionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            modelo.getIndividual(nS + clase + programaSeleccionado.getCodProg()).remove();
            ont.guardarModelo(modelo);
            programaSeleccionado = null;
            listaSeleccionada = null;
            listaPrograma = this.programaFacade.findAll();
            codFac = "";
            codDep = "";
        } catch (Exception e) {

        }
    }

    /**
     * Creates a new instance of ProgramaBean
     */
    public ProgramaBean() {
    }
    @PostConstruct
    public void reset(){
        listaPrograma=this.programaFacade.findAll();
    }
}
