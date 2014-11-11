/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import clases.UsuarioPermiso;
import facadePojo.AplicacionFacade;
import facadePojo.UsuarioFacade;
import java.security.MessageDigest;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pojo.Aplicacion;
import pojo.Usuario;
import pojo.UsuarioAplicacion;
import pojo.UsuarioAplicacionPK;

/**
 *
 * @author mateo
 */
@ManagedBean
@SessionScoped
public class UsuarioBean {

    /**
     * Creates a new instance of UsuarioBean
     */
    private List<Usuario> listaUsuarios;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private AplicacionFacade aplicacionFacade;
    private List<Aplicacion> listaAplicacion;
    private List<UsuarioPermiso> listaUsuarioAplicacion;
    private Usuario nuevoUsuario;
    private Usuario usuarioModificar;
    private String claveConfirmada;
    private String clave;
    private String usuario;
    private boolean consulta;

    public Usuario getUsuarioModificar() {
        return usuarioModificar;
    }

    public void setUsuarioModificar(Usuario usuarioModificar) {
        this.usuarioModificar = usuarioModificar;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Usuario getNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(Usuario nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    public String getClaveConfirmada() {
        return claveConfirmada;
    }

    public void setClaveConfirmada(String claveConfirmada) {
        this.claveConfirmada = claveConfirmada;
    }

    public List<UsuarioPermiso> getListaUsuarioAplicacion() {
        return listaUsuarioAplicacion;
    }

    public void setListaUsuarioAplicacion(List<UsuarioPermiso> listaUsuarioAplicacion) {
        this.listaUsuarioAplicacion = listaUsuarioAplicacion;
    }

    public List<Aplicacion> getListaAplicacion() {
        return listaAplicacion;
    }

    public void setListaAplicacion(List<Aplicacion> listaAplicacion) {
        this.listaAplicacion = listaAplicacion;
    }
    private Usuario usuarioNuevo;

    public Usuario getUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(Usuario usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public boolean isConsulta() {
        return consulta;
    }

    public void setConsulta(boolean consulta) {
        this.consulta = consulta;
    }
   
    public UsuarioBean() {
    }

    public List<String> completeUsuario(String query) {
        List<String> results = new ArrayList<String>();

        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getNombreUsuario().startsWith(query)) {
                results.add(listaUsuarios.get(i).getNombreUsuario());
            }
        }

        return results;
    }

    private static String md5(String clear) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(clear.getBytes());
        int size = b.length;
        StringBuffer h = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            int u = b[i] & 255;
            if (u < 16) {
                h.append("0" + Integer.toHexString(u));
            } else {
                h.append(Integer.toHexString(u));
            }
        }

        return h.toString();
    }

    public void consultarUsuario() {
        List<Usuario> usuarios = usuarioFacade.findUsuario(usuario);
        if (usuarios.isEmpty()) {
            this.consulta=false;
            FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario "+this.usuario+" no existe", ""));
        } else {
            this.usuarioModificar = usuarios.get(0);
            this.consulta=true;
        }
    }

    public void guardarUsuario() {
        try {
            // this.usuarioNuevo=new Usuario();
            if (!this.clave.equals(this.claveConfirmada)) {
                FacesContext.getCurrentInstance().
                        addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las contraseñas no son iguales", ""));
                return;
            }
            this.usuarioNuevo.setClaveUsuario(md5(clave));
            this.usuarioNuevo.setNombreUsuario(usuario);
            List<UsuarioAplicacion> usuarioAplicacionList = new ArrayList<UsuarioAplicacion>();
            for (int i = 0; i < this.listaUsuarioAplicacion.size(); i++) {
                UsuarioAplicacion usuarioApi = new UsuarioAplicacion(usuario, this.listaAplicacion.get(i).getCodApi());
                usuarioApi.setEscritura(this.listaUsuarioAplicacion.get(i).isPermiso());
                usuarioApi.setAplicacion(this.listaUsuarioAplicacion.get(i).getAplicacion());
                usuarioApi.setUsuario(this.usuarioNuevo);
                usuarioAplicacionList.add(usuarioApi);

            }
            this.usuarioNuevo.setUsuarioAplicacionList(usuarioAplicacionList);
            this.usuarioFacade.create(this.usuarioNuevo);
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario creado exitosamente", ""));
            this.reset();
        } catch (Exception e) {
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema al crear el usuario", ""));
            this.reset();
        }
    }
    
    public void modificarUsuario(){
        try{
            this.usuarioFacade.edit(this.usuarioModificar);
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario modificado exitosamente", ""));
            this.reset();
        }catch(Exception e){
            System.out.println(e.toString());
            FacesContext.getCurrentInstance().
                    addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Problema al modificar el usuario", ""));
            this.reset();
        }
    }

    @PostConstruct
    public void reset() {
        this.listaUsuarios = this.usuarioFacade.findAll();
        this.listaAplicacion = this.aplicacionFacade.findAll();
        this.listaUsuarioAplicacion = new ArrayList<UsuarioPermiso>();
        this.usuario = "";
        this.usuarioNuevo = new Usuario();
        this.usuarioModificar = new Usuario();
        this.consulta=false;
        //  this.usuarioNuevo.setNombreUsuario("");
        //  this.nuevoUsuario.setClaveUsuario("");
        this.claveConfirmada = "";

        for (Aplicacion api : this.listaAplicacion) {
            UsuarioPermiso usuarioAplicacion = new UsuarioPermiso();
            usuarioAplicacion.setAplicacion(api);
            usuarioAplicacion.setPermiso(false);
            usuarioAplicacion.setUsuario(nuevoUsuario);
            this.listaUsuarioAplicacion.add(usuarioAplicacion);
        }
    }
}
