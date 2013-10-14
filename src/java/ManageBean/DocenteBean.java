/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import facadePojo.DocenteFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Docente;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "docenteBean")
@SessionScoped
public class DocenteBean {

    private List<Docente> listaDocente;
    private Docente docenteSeleccionado;
    private String codocente;
    private String nombre;
    private String apellido;
    private Character tipo;
    private String busqueda;
    @EJB
    DocenteFacade docenteFacade;
    int li;
    int sup;
    int antLi;
    int limite;
    boolean cambio;
    String clase = "Docente";

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<Docente> getListaDocente() {
        return listaDocente;
    }

    public void setListaDocente(List<Docente> listaDocente) {
        this.listaDocente = listaDocente;
    }

    public Docente getDocenteSeleccionado() {
        return docenteSeleccionado;
    }

    public void setDocenteSeleccionado(Docente docenteSeleccionado) {
        this.docenteSeleccionado = docenteSeleccionado;
    }

    public String getCodocente() {
        return codocente;
    }

    public void setCodocente(String codocente) {
        this.codocente = codocente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombres) {
        this.nombre = nombres;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public DocenteFacade getDocenteFacade() {
        return docenteFacade;
    }

    public void setDocenteFacade(DocenteFacade docenteFacade) {
        this.docenteFacade = docenteFacade;
    }

    public int getLi() {
        return li;
    }

    public void setLi(int li) {
        this.li = li;
    }

    public int getSup() {
        return sup;
    }

    public void setSup(int sup) {
        this.sup = sup;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    @PostConstruct
    public void reset() {
        listaDocente = this.docenteFacade.findByRango(0, 10);
        cambio = true;
        li = 10;
        limite = 10;
        sup = docenteFacade.count();
        docenteSeleccionado = null;
        codocente = "";
        nombre = "";
        apellido = "";
        tipo = null;
    }

    public void buscar() {
        if (!busqueda.isEmpty()) {
            List<Object[]> lista = this.docenteFacade.findAllJaro(busqueda);
            listaDocente = new ArrayList<Docente>();
            for (int i = 0; i < lista.size(); i++) {
                listaDocente.add(this.docenteFacade.find(lista.get(i)[2]));
            }
        }

    }

    public void sig() {
        if (cambio) {
            listaDocente = this.docenteFacade.findByRango(li, 10);
            antLi = li;
            li = li + 10;
            limite = limite + 10;
            if (li >= sup) {
                li = sup;
                limite = sup;
            }
        } else {
            listaDocente = this.docenteFacade.findByRango(li + 10, 10);
            li = li + 20;
            limite = limite + 10;
            if (li >= sup) {
                li = sup;
                limite = sup;
            }
        }
        cambio = true;
    }

    public void ant() {
        if (li == sup) {
            listaDocente = this.docenteFacade.findByRango(antLi - 10, 10);
            li = antLi - 10;
            limite = antLi;
        } else {
            if (!cambio) {
                listaDocente = this.docenteFacade.findByRango(li - 10, 10);
                li = li - 10;
                limite = limite - 10;
            } else {
                listaDocente = this.docenteFacade.findByRango(li - 20, 10);
                li = li - 20;
                limite = limite - 10;
            }
        }
        cambio = false;
    }

    public void nuevo() {
        Docente doc = new Docente();
        doc.setApellido(apellido.toUpperCase());
        doc.setNombre(nombre.toUpperCase());
        doc.setCodocente(codocente);
        doc.setTipo(tipo);
        try {
            this.docenteFacade.create(doc);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseDoc = modelo.getOntClass(nS + clase);
            Individual docente = modelo.createIndividual(nS + clase + codocente, claseDoc);
            DatatypeProperty codigo_docente = modelo.getDatatypeProperty(nS + "identificacion_persona");
            DatatypeProperty apellido_docente = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty nombre_docente = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty tip = modelo.getDatatypeProperty(nS + "tipo_vinculacion");
            docente.setPropertyValue(nombre_docente, modelo.createTypedLiteral(doc.getNombre()));
            docente.setPropertyValue(apellido_docente, modelo.createTypedLiteral(doc.getApellido()));
            docente.setPropertyValue(codigo_docente, modelo.createTypedLiteral(doc.getCodocente()));
            int vinculacion = 0;
            if (doc.getTipo() == 'I') {
                vinculacion = 1;
            }
            System.out.println(vinculacion);
            docente.setPropertyValue(tip, modelo.createTypedLiteral(vinculacion, XSDDatatype.XSDint));
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar docente ", ""));
        }

    }

    public void modificar() {
        try {
            docenteSeleccionado.setNombre(docenteSeleccionado.getNombre().toUpperCase());
            docenteSeleccionado.setApellido(docenteSeleccionado.getApellido().toUpperCase());
            docenteSeleccionado.setTipo(tipo);
            this.docenteFacade.edit(docenteSeleccionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();
            OntClass claseDoc = modelo.getOntClass(nS + clase);
            Individual docente = modelo.createIndividual(nS + clase + docenteSeleccionado.getCodocente(), claseDoc);
            DatatypeProperty codigo_docente = modelo.getDatatypeProperty(nS + "identificacion_persona");
            DatatypeProperty apellido_docente = modelo.getDatatypeProperty(nS + "apellido_persona");
            DatatypeProperty nombre_docente = modelo.getDatatypeProperty(nS + "nombre_persona");
            DatatypeProperty tip = modelo.getDatatypeProperty(nS + "tipo_vinculacion");
            docente.setPropertyValue(nombre_docente, modelo.createTypedLiteral(docenteSeleccionado.getNombre()));
            docente.setPropertyValue(apellido_docente, modelo.createTypedLiteral(docenteSeleccionado.getApellido()));
            docente.setPropertyValue(codigo_docente, modelo.createTypedLiteral(docenteSeleccionado.getCodocente()));
            int vinculacion = 0;
            if (docenteSeleccionado.getTipo() == 'I') {
                vinculacion = 1;
            }
            System.out.println(vinculacion);
            docente.setPropertyValue(tip, modelo.createTypedLiteral(vinculacion, XSDDatatype.XSDint));
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Docente Modificado Exitosamente", ""));
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar docente ", ""));
        }
    }

    public void eliminar() {
        try {
            this.docenteFacade.remove(docenteSeleccionado);
            ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
            String nS = ont.getPrefijo();
            OntModel modelo = ont.getModelOnt();          
            modelo.getIndividual(nS + clase + docenteSeleccionado.getCodocente()).remove();
            ont.guardarModelo(modelo);
            reset();
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage("docente eliminado con exito ", ""));
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar docente ", ""));
        }
    }

    /**
     * Creates a new instance of DocenteBean
     */
    public DocenteBean() {
    }
}
