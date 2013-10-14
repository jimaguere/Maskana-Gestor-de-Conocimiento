/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pojo.Usuario;

/**
 *
 * @author mateo
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public List<Usuario> findUsuario(String usuario){
        Query cq = getEntityManager().createNamedQuery("Usuario.findByNombreUsuario");
        cq.setParameter("nombreUsuario",usuario);
        return cq.getResultList();
    }
    public UsuarioFacade() {
        super(Usuario.class);
    }
    
}
