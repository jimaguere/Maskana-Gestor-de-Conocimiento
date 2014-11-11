/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * http://forum.primefaces.org/viewtopic.php?f=3&t=12869
 */
package ManageBean;

import Conexion.IndexarDocumento;
import clases.ExtracPdfText;
import facadePojo.DepartamentoFacade;
import facadePojo.DocenteFacade;
import facadePojo.EstudianteFacade;
import facadePojo.FacultadFacade;
import facadePojo.ProgramaFacade;
import facadePojo.TgautorFacade;
import facadePojo.TrabajosGradoFacade;
import facadePojo.VocabularioFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;
import pojo.Departamento;
import pojo.Docente;
import pojo.Estudiante;
import pojo.Facultad;
import pojo.Lineainvestigacion;
import pojo.Modalidad;
import pojo.Programa;
import pojo.Tgautor;
import pojo.TgautorPK;
import pojo.TrabajosGrado;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "trabajoBean")
@SessionScoped
public class TrabajosGradoBean {

    /**
     * Creates a new instance of TrabajosGradoBean
     */
    private TrabajosGrado nuevoTrabajoGrado;
    private TrabajosGrado trabajoGradoSeleccionado;
    private List<TrabajosGrado> listaTrabajosGrado;
    private List<Programa> listaPrograma;
    private List<Departamento> listaDepartamento;
    private List<Facultad> listaFacultad;
    @EJB
    private TrabajosGradoFacade trabajosGradoFacade;
    @EJB
    private ProgramaFacade programaFacade;
    @EJB
    private DepartamentoFacade departamentoFacade;
    @EJB
    private FacultadFacade facultadFacade;
    @EJB
    private DocenteFacade docenteFacade;
    @EJB
    private EstudianteFacade estudianteFacade;
    @EJB
    private TgautorFacade tgAutorFacade;
    @EJB
    VocabularioFacade vocabularioFacadel;
    private int li;
    private int sup;
    private int antLi;
    private int limite;
    private boolean cambio;
    private String busqueda;
    private static String clase = "Trabajo_grado";
    private TrabajosGrado tgGrado;
    private List<Estudiante> listaAutores;
    private String codFac;
    private String codDep;
    private String codProg;
    private UploadedFile tesisPdf;
    private boolean skip;
    private static Logger logger = Logger.getLogger(TrabajosGradoBean.class.getName());
    private String titulo;
    private String contenido;
    private List<Docente> listaDirectores;

    public List<Docente> getListaDirectores() {
        return listaDirectores;
    }

    public void setListaDirectores(List<Docente> listaDirectores) {
        this.listaDirectores = listaDirectores;
    }

    public List<Estudiante> getListaAutores() {
        return listaAutores;
    }

    public void setListaAutores(List<Estudiante> listaAutores) {
        this.listaAutores = listaAutores;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UploadedFile getTesisPdf() {
        return tesisPdf;
    }

    public void setTesisPdf(UploadedFile tesisPdf) {
        this.tesisPdf = tesisPdf;
    }

    public List<Facultad> getListaFacultad() {
        return listaFacultad;
    }

    public void setListaFacultad(List<Facultad> listaFacultad) {
        this.listaFacultad = listaFacultad;
    }

    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public List<Programa> getListaPrograma() {
        return listaPrograma;
    }

    public void setListaPrograma(List<Programa> listaPrograma) {
        this.listaPrograma = listaPrograma;
    }

    public String getCodFac() {
        return codFac;
    }

    public void setCodFac(String codFac) {
        this.codFac = codFac;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
    }

    public TrabajosGrado getTgGrado() {
        return tgGrado;
    }

    public void setTgGrado(TrabajosGrado tgGrado) {
        this.tgGrado = tgGrado;
    }

    public TrabajosGrado getNuevoTrabajoGrado() {
        return nuevoTrabajoGrado;
    }

    public void setNuevoTrabajoGrado(TrabajosGrado nuevoTrabajoGrado) {
        this.nuevoTrabajoGrado = nuevoTrabajoGrado;
    }

    public TrabajosGrado getTrabajoGradoSeleccionado() {
        return trabajoGradoSeleccionado;
    }

    public void setTrabajoGradoSeleccionado(TrabajosGrado trabajoGradoSeleccionado) {
        this.trabajoGradoSeleccionado = trabajoGradoSeleccionado;
    }

    public List<TrabajosGrado> getListaTrabajosGrado() {
        return listaTrabajosGrado;
    }

    public void setListaTrabajosGrado(List<TrabajosGrado> listaTrabajosGrado) {
        this.listaTrabajosGrado = listaTrabajosGrado;
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

    public int getAntLi() {
        return antLi;
    }

    public void setAntLi(int antLi) {
        this.antLi = antLi;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public boolean isCambio() {
        return cambio;
    }

    public void setCambio(boolean cambio) {
        this.cambio = cambio;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public void sig() {
        if (cambio) {
            listaTrabajosGrado = this.trabajosGradoFacade.findByRango(li, 10);
            antLi = li;
            li = li + 10;
            limite = limite + 10;
            if (li >= sup) {
                li = sup;
                limite = sup;
            }

        } else {
            listaTrabajosGrado = this.trabajosGradoFacade.findByRango(li + 10, 10);
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
            listaTrabajosGrado = this.trabajosGradoFacade.findByRango(antLi - 10, 10);
            li = antLi - 10;
            limite = antLi;

        } else {
            if (!cambio) {
                listaTrabajosGrado = this.trabajosGradoFacade.findByRango(li - 10, 10);
                li = li - 10;
                limite = limite - 10;
            } else {
                listaTrabajosGrado = this.trabajosGradoFacade.findByRango(li - 20, 10);
                li = li - 20;
                limite = limite - 10;
            }
        }
        cambio = false;
    }

    public void buscar() {
        if (!busqueda.isEmpty()) {
            List<Object[]> lista = this.trabajosGradoFacade.findAllJaro(busqueda);
            listaTrabajosGrado = new ArrayList<TrabajosGrado>();
            System.out.println(lista.size());
            for (int i = 0; i < lista.size(); i++) {
                listaTrabajosGrado.add(this.trabajosGradoFacade.find(lista.get(i)[0]));
            }
        }
    }

    public String obtenerAutores(TrabajosGrado t) {
        String autores = "";
        for (int i = 0; i < t.getTgautorList().size(); i++) {
            autores = autores + " - " + t.getTgautorList().get(i).getEstudiante().getNombres() + " "
                    + "" + t.getTgautorList().get(i).getEstudiante().getApellidos();
        }
        return autores;
    }

    public void actualizarProgramas() {
        this.listaPrograma = programaFacade.findByCodDep(codDep);
    }

    public void actualizarDepartamentos() {
        this.listaDepartamento = departamentoFacade.findByCodFac(codFac);
        this.listaPrograma = new ArrayList<Programa>();
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        try{
            this.tesisPdf = event.getFile();
            ExtracPdfText texto = new ExtracPdfText(this.tesisPdf.getInputstream());
            this.titulo = texto.extraerTitulo();
            this.contenido = texto.extraerTexto();
            texto.cerrar();
            FacesMessage msg = new FacesMessage("Archivo " + event.getFile().getFileName() + "","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } catch (FileNotFoundException ex) {
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }catch(java.lang.NoClassDefFoundError ex){
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);           
        }
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error En Cargar " + event.getFile().getFileName() + " Favor Intentar Con Otro Archivo","");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        //http://diegosoler-lotus.blogspot.com/2008/04/analizando-un-archivo-adjunto-con.html  
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        logger.info("Current wizard step:" + event.getOldStep());
        logger.info("Next step:" + event.getNewStep());

        if (skip) {
            skip = false;	//reset in case user goes back
            return "confirm";
        } else {
            System.out.println("e:" + event.getNewStep());
            return event.getNewStep();
        }
    }

    public void nuevoAutor() {
        Estudiante est = new Estudiante();
        listaAutores.add(est);
    }

    public void nuevoAutorT() {
        Tgautor autor = new Tgautor();
        autor.setEstudiante(new Estudiante());
        if (trabajoGradoSeleccionado.getTgautorList() == null) {
            trabajoGradoSeleccionado.setTgautorList(new ArrayList<Tgautor>());
        }

        autor.setTrabajosGrado(trabajoGradoSeleccionado);
        trabajoGradoSeleccionado.getTgautorList().add(autor);
    }

    public void removerAutor(int i) {
        listaAutores.remove(i);
    }

    public void removerAutorT(int i) {
        this.trabajoGradoSeleccionado.getTgautorList().remove(i);
    }

    public void removerDirectorT(int i) {
        this.trabajoGradoSeleccionado.getDocenteList1().remove(i);
    }

    public void nuevoDirector() {
        try {
            Docente docente = new Docente();
            docente.setCodocente("11111");
            listaDirectores.add(docente);
        } catch (Exception e) {
            System.out.println("error:" + e.toString());
        }

    }

    public void nuevoDirectorT() {
        try {
            Docente docente = new Docente();
            docente.setCodocente("11111");
            if (trabajoGradoSeleccionado.getDocenteList1() == null) {
                trabajoGradoSeleccionado.setDocenteList(new ArrayList<Docente>());
            }
            trabajoGradoSeleccionado.getDocenteList1().add(docente);


        } catch (Exception e) {
            System.out.println("error:" + e.toString());
        }

    }

    public void removerDirector(int j) {
        listaDirectores.remove(j);
    }

    public boolean guardarArchivo(Integer nombre) {
        try {
            if (this.tesisPdf == null) {
                return true;
            }
            ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String ruta = (String) servContx.getRealPath("/");
            //File documento=new File(ruta+"resources/pdf/"+nombre);
            OutputStream documento = new FileOutputStream(new File(ruta + "resources/pdf/" + nombre + ".pdf"));
            InputStream archivoIn = this.tesisPdf.getInputstream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = archivoIn.read(bytes)) != -1) {
                documento.write(bytes, 0, read);
            }
            archivoIn.close();
            documento.flush();
            documento.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validarAutoresT(List<Tgautor> autores, List<Docente> docentes) {
        if (autores == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
            return false;
        }
        if (autores.isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
            return false;
        }

        for (int i = 0; i < autores.size(); i++) {
            if (autores.get(i).getEstudiante().getNombres() == null || autores.get(i).getEstudiante().getApellidos() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
                return false;
            }
            if (autores.get(i).getEstudiante().getNombres().trim().length() == 0 || autores.get(i).getEstudiante().getApellidos().trim().length() == 0) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
                return false;
            }
        }
        if (docentes == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
            return false;
        }
        if (docentes.isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
            return false;
        }
        for (int i = 0; i < docentes.size(); i++) {
            if (docentes.get(i).getNombre() == null || docentes.get(i).getApellido() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
                return false;
            }
            if (docentes.get(i).getNombre().trim().length() == 0 || docentes.get(i).getApellido().trim().length() == 0) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
                return false;
            }
        }
        return true;
    }

    public boolean validarAutores(List<Estudiante> estudiantes, List<Docente> docentes) {
        if (estudiantes == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
            return false;
        }
        if (estudiantes.isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
            return false;
        }

        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getNombres() == null || estudiantes.get(i).getApellidos() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
                return false;
            }
            if (estudiantes.get(i).getNombres().trim().length() == 0 || estudiantes.get(i).getApellidos().trim().length() == 0) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de autores incompleta ", ""));
                return false;
            }
        }
        if (docentes == null) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
            return false;
        }
        if (docentes.isEmpty()) {
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
            return false;
        }
        for (int i = 0; i < docentes.size(); i++) {
            if (docentes.get(i).getNombre() == null || docentes.get(i).getApellido() == null) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
                return false;
            }
            if (docentes.get(i).getNombre().trim().length() == 0 || docentes.get(i).getApellido().trim().length() == 0) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Información de docentes incompleta ", ""));
                return false;
            }
        }
        return true;
    }

    public void crearDocumento() {
        try {
            if (!validarAutores(listaAutores, listaDirectores)) {
                return;
            }
            TrabajosGrado trabajoGrado = new TrabajosGrado();
            trabajoGrado.setIdTg(trabajosGradoFacade.opbtenerSecuencia());
            if (guardarArchivo(trabajoGrado.getIdTg())) {
                System.out.println("grabo");
                this.titulo = this.titulo.replace("\\n", " ").replace("“", "").replace("–", "").replace("—", "").replace("Œ", "").replace("∗", "")
                        .replace(".", "").replace(",", "").replace(";", "").replace("*", "").replace("‑", "").replace("Ò", "")
                        .replace("\"", "").replace("®", "").replace("Œ", "").replace("\\r", " ");
                trabajoGrado.setTitulo(this.titulo.trim().toUpperCase());
                Modalidad modalidad = new Modalidad("1");
                Lineainvestigacion linea = new Lineainvestigacion("6");
                trabajoGrado.setCodModalidad(modalidad);
                trabajoGrado.setCodigoLinea(linea);
                for (Docente docente : listaDirectores) {
                    docente.setId(docenteFacade.opbtenerSecuencia());
                    docente.setApellido(docente.getApellido().trim().toUpperCase());
                    docente.setNombre(docente.getNombre().trim().toUpperCase());
                    docenteFacade.create(docente);
                }
                trabajoGrado.setDocenteList(listaDirectores);
                trabajoGrado.setDocenteList1(listaDirectores);
                List<Tgautor> listaTgAutor = new ArrayList<Tgautor>();
                for (Estudiante estudiante : listaAutores) {
                    Programa prog = new Programa(codProg);
                    estudiante.setPrograma(prog);
                    estudiante.setId(estudianteFacade.opbtenerSecuencia());
                    estudiante.setNombres(estudiante.getNombres().toUpperCase().trim());
                    estudiante.setApellidos(estudiante.getApellidos().toUpperCase().trim());
                    estudianteFacade.create(estudiante);
                    Tgautor autor = new Tgautor(trabajoGrado.getIdTg(), estudiante.getId());
                    autor.setEstudiante(estudiante);
                    autor.setTrabajosGrado(trabajoGrado);
                    listaTgAutor.add(autor);

                }
                trabajoGrado.setTgautorList(listaTgAutor);
                if (this.trabajosGradoFacade.crearTrabajoGrado(trabajoGrado)) {
                    IndexarDocumento documentoIndex = new IndexarDocumento();
                    ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
                    documentoIndex.setOnt(ont);
                    documentoIndex.setVocabularioFacadel(vocabularioFacadel);
                    documentoIndex.setTitulo(titulo);
                    documentoIndex.setContenido(contenido);
                    documentoIndex.setTrabajoGrado(trabajoGrado);
                    documentoIndex.crearDocuemntoOntologia();
                    ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                    String ruta = (String) servContx.getRealPath("/");
                    documentoIndex.indexarTexto(ruta);
                    this.reset();
                    FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
                }
            }
        } catch (Exception e) {
            System.out.println("error:" + e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar documento ", ""));
        }
    }

    public void modificarDocumento() {
        try {
            if (!validarAutoresT(this.trabajoGradoSeleccionado.getTgautorList(), trabajoGradoSeleccionado.getDocenteList1())) {
                return;
            }
            if (guardarArchivo(this.trabajoGradoSeleccionado.getIdTg())) {
                System.out.println("grabo");
                this.trabajoGradoSeleccionado.setTitulo(this.trabajoGradoSeleccionado.getTitulo().replace("\\n", " ").replace("“", "").replace("–", "").replace("—", "").replace("Œ", "").replace("∗", "")
                        .replace(".", "").replace(",", "").replace(";", "").replace("*", "").replace("‑", "").replace("Ò", "")
                        .replace("\"", "").replace("®", "").replace("Œ", "").replace("\\r", " ").trim().toUpperCase());
                List<Docente> listaDocenteModificada = new ArrayList<Docente>();
                for (Docente docente : this.trabajoGradoSeleccionado.getDocenteList1()) {
                    docente.setApellido(docente.getApellido().trim().toUpperCase());
                    docente.setNombre(docente.getNombre().trim().toUpperCase());
                    if (docente.getId() == null) {
                        docente.setTrabajosGradoList1(new ArrayList<TrabajosGrado>());
                        docente.setTrabajosGradoList(new ArrayList<TrabajosGrado>());
                        docente.setId(docenteFacade.opbtenerSecuencia());
                        docente.getTrabajosGradoList().add(trabajoGradoSeleccionado);
                        docente.getTrabajosGradoList1().add(trabajoGradoSeleccionado);
                        listaDocenteModificada.add(docente);
                        docenteFacade.create(docente);
                        continue;
                    }
                    listaDocenteModificada.add(docente);
                    this.docenteFacade.edit(docente);
                    docente.getTrabajosGradoList1().add(trabajoGradoSeleccionado);
                }
                this.trabajoGradoSeleccionado.setDocenteList1(listaDocenteModificada);
                for (Tgautor estudiante : this.trabajoGradoSeleccionado.getTgautorList()) {
                    Programa prog = new Programa(codProg);
                    estudiante.getEstudiante().setPrograma(prog);
                    estudiante.getEstudiante().setNombres(estudiante.getEstudiante().getNombres().toUpperCase().trim());
                    estudiante.getEstudiante().setApellidos(estudiante.getEstudiante().getApellidos().toUpperCase().trim());
                    if (estudiante.getEstudiante().getId() == null) {
                        estudiante.getEstudiante().setId(estudianteFacade.opbtenerSecuencia());
                        this.estudianteFacade.create(estudiante.getEstudiante());
                        estudiante.setTgautorPK(new TgautorPK(this.trabajoGradoSeleccionado.getIdTg(), estudiante.getEstudiante().getId()));
                        continue;
                    }
                    this.estudianteFacade.edit(estudiante.getEstudiante());
                }
                this.trabajosGradoFacade.edit(this.trabajoGradoSeleccionado);
                IndexarDocumento documentoIndex = new IndexarDocumento();
                ModeloBean ont = (ModeloBean) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("modeloBean");
                documentoIndex.setOnt(ont);
                documentoIndex.setVocabularioFacadel(vocabularioFacadel);
                documentoIndex.setTitulo(this.trabajoGradoSeleccionado.getTitulo());
                documentoIndex.setContenido(this.contenido);
                documentoIndex.setTrabajoGrado(this.trabajoGradoSeleccionado);
                documentoIndex.modificarDocumento();
                ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String ruta = (String) servContx.getRealPath("/");
                documentoIndex.modificarIndice(ruta);
                this.reset();
                FacesContext.getCurrentInstance().getExternalContext().redirect("Lista.xhtml");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Registro Exitoso", ""));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al modificar documento ", ""));
        }
    }

    public TrabajosGradoBean() {
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Car Edited", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String asignarTesis(TrabajosGrado trabajo) {
        this.trabajoGradoSeleccionado = trabajo;
        try {
            this.codFac = this.trabajoGradoSeleccionado.getTgautorList().get(0).getEstudiante().getPrograma().getCodDep().getCodFac().getCodFac();
            this.codDep = this.trabajoGradoSeleccionado.getTgautorList().get(0).getEstudiante().getPrograma().getCodDep().getCodDep();
            this.codProg = this.trabajoGradoSeleccionado.getTgautorList().get(0).getEstudiante().getPrograma().getCodProg();
            this.actualizarDepartamentos();
            this.actualizarProgramas();
        } catch (Exception e) {
            this.codFac = "";
            this.codProg = "";
            this.codDep = "";
        }
        ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String ruta = (String) servContx.getRealPath("/");
        InputStream archivoI;
        try {
            archivoI = new FileInputStream(ruta + "resources/pdf/" + this.trabajoGradoSeleccionado.getIdTg() + ".pdf");
            ExtracPdfText texto = new ExtracPdfText(archivoI);
            this.contenido = texto.extraerTexto();
            texto.cerrar();
        } catch (FileNotFoundException ex) {
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }catch(java.lang.NoClassDefFoundError ex){
            this.contenido="";
            Logger.getLogger(TrabajosGradoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "ModificarDocumento.xhtml";
    }

    @PostConstruct
    public void reset() {
        nuevoTrabajoGrado = new TrabajosGrado();
        listaAutores = new ArrayList<Estudiante>();
        listaDirectores = new ArrayList<Docente>();
        trabajoGradoSeleccionado = new TrabajosGrado();
        tgGrado = new TrabajosGrado();
        listaTrabajosGrado = this.trabajosGradoFacade.findByRango(0, 10);
        cambio = true;
        li = 10;
        limite = 10;
        sup = this.trabajosGradoFacade.count();
        busqueda = "";
        listaPrograma = new ArrayList<Programa>();
        listaDepartamento = new ArrayList<Departamento>();
        listaFacultad = facultadFacade.findAll();
    }
}
