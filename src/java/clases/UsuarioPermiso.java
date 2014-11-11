/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import pojo.Aplicacion;
import pojo.Usuario;

/**
 *
 * @author mateo
 */
public class UsuarioPermiso {
    
    private boolean permiso;
    private Usuario usuario;
    private Aplicacion aplicacion;
    
    public UsuarioPermiso(){
        
    }
     
    public boolean isPermiso() {
        return permiso;
    }

    public void setPermiso(boolean permiso) {
        this.permiso = permiso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }
            
    
}
