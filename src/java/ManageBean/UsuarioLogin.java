/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import facadePojo.UsuarioFacade;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import pojo.Usuario;
import pojo.UsuarioAplicacion;

/**
 *
 * @author mateo
 */
@ManagedBean(name = "usuarioLogin")
@SessionScoped
public class UsuarioLogin {

    @EJB
    UsuarioFacade usuarioFacade;
    private String usuario;
    private String clave;
    private boolean login;
    private String url;
    private List<UsuarioAplicacion>escrituras;

    public List<UsuarioAplicacion> getEscrituras() {
        return escrituras;
    }
    public boolean permisos(int cod){
        boolean permiso=false;
        for(int i=0;i<escrituras.size();i++){
            if(escrituras.get(i).getAplicacion().getCodApi()==cod && escrituras.get(i).getEscritura()){
                permiso=true;
                i=escrituras.size();
            }
        }
        return permiso;
    }
    public void setEscrituras(List<UsuarioAplicacion> escrituras) {
        this.escrituras = escrituras;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    public UsuarioFacade getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void cerrarSession() throws IOException {
        this.login = false;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().getSession(login);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect( url+"/faces/Login.xhtml");
    }

    public void confirmar() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        List<Usuario> usuarios = usuarioFacade.findUsuario(this.usuario);
        if (usuarios.isEmpty()) {
            context.addMessage(null, new FacesMessage("Error usuario:", this.usuario + " No existe"));
            return;
        }
        Usuario user = usuarios.get(0);
        escrituras=user.getUsuarioAplicacionList();
        if (user.getClaveUsuario().equals(md5(this.clave))) {
            this.login = true;
            context.getExternalContext().redirect("Menu.xhtml");
        } else {
            context.addMessage(null, new FacesMessage("Error", "Clave Incorrecta para el usuario:" + this.usuario));
        }
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

    /**
     * Creates a new instance of UsuarioLogin
     */
    public UsuarioLogin() {
    }
    @PostConstruct
    public void reset(){
        ServletContext servContx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        url=(String) servContx.getContextPath();
        
    }
}
