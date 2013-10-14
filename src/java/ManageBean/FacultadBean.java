package ManageBean;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import facadePojo.FacultadFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Facultad;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "facultadBean")
@SessionScoped
public class FacultadBean {

    private List<Facultad> listaFacultad;
    private List<Facultad> listaSelecionada;
    private Facultad facultadSelecionada;
    private String codFac;
    private String nombre;
    private String clase = "Facultad";
    @EJB
    FacultadFacade facultadFacade;
    
    public List<Facultad> getListaFacultad() {
        return listaFacultad;
    }

    public void setListaFacultad(List<Facultad> listaFacultad) {
        this.listaFacultad = listaFacultad;
    }

    public List<Facultad> getListaSelecionada() {
        return listaSelecionada;
    }

    public void setListaSelecionada(List<Facultad> listaSelecionada) {
        this.listaSelecionada = listaSelecionada;
    }

    public Facultad getFacultadSelecionada() {
        return facultadSelecionada;
    }

    public void setFacultadSelecionada(Facultad facultadSelecionada) {
        this.facultadSelecionada = facultadSelecionada;
    }

    public String getCodFac() {
        return codFac;
    }

    public void setCodFac(String codFac) {
        this.codFac = codFac;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public FacultadFacade getFacultadFacade() {
        return facultadFacade;
    }

    public void setFacultadFacade(FacultadFacade facultadFacade) {
        this.facultadFacade = facultadFacade;
    }

    public void nueva() {
        Facultad facultadNueva = new Facultad();
        facultadNueva.setCodFac(codFac);
        facultadNueva.setNombre(nombre.toUpperCase());
//        this.facultadFacade.begin();
        try {
            this.facultadFacade.create(facultadNueva);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseFac = modelo.getOntClass(nS + clase);
            Individual facultad = modelo.createIndividual(nS +clase+codFac, claseFac);
            DatatypeProperty codigo = modelo.getDatatypeProperty(nS + "codigo_facultad");
            DatatypeProperty nomFac = modelo.getDatatypeProperty(nS + "nombre_facultad");
            facultad.setPropertyValue(codigo, ont.getModelOnt().createTypedLiteral(codFac));
            facultad.setPropertyValue(nomFac, ont.getModelOnt().createTypedLiteral(nombre.toUpperCase()));
            ont.guardarModelo(modelo);
            //          this.facultadFacade.commit();
            nombre = "";
            codFac = "";
            listaFacultad = this.facultadFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            //          this.facultadFacade.rollback();
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al registrar facultad ", ""));
        }
    }

    public void modificar() {
        try {
            facultadSelecionada.setNombre(facultadSelecionada.getNombre().toUpperCase());
            this.facultadFacade.edit(facultadSelecionada);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseFac = modelo.getOntClass(nS + clase);
            
            Individual facultad = modelo.createIndividual(nS +clase+facultadSelecionada.getCodFac(), claseFac);           
            DatatypeProperty nomFac = modelo.getDatatypeProperty(nS + "nombre_facultad");
            facultad.setPropertyValue(nomFac, ont.getModelOnt().createTypedLiteral(facultadSelecionada.getNombre()));
            ont.guardarModelo(modelo);
            facultadSelecionada=null;
            listaSelecionada=null;
            listaFacultad = this.facultadFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Problema al modificar la Facultad", ""));
        }
    }

    public void eliminar() {
        try {
            this.facultadFacade.remove(facultadSelecionada);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            modelo.getIndividual(nS+clase+facultadSelecionada.getCodFac()).remove();
            ont.guardarModelo(modelo);
            facultadSelecionada=null;
            listaSelecionada=null;
            listaFacultad = this.facultadFacade.findAll();
            FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Problema al eliminar la Facultad", ""));
        }
    }

    /**
     * Creates a new instance of FacultadBean
     */
    public FacultadBean() {
    }

    @PostConstruct
    public void reset() {
        listaFacultad = this.facultadFacade.findAll();       
    }
}
