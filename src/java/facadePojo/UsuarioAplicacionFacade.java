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
import pojo.UsuarioAplicacion;

/**
 *
 * @author mateo
 */
@Stateless
public class UsuarioAplicacionFacade extends AbstractFacade<UsuarioAplicacion> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public List<UsuarioAplicacion> findUsuarioApi(String usuario,Short codApi){
        Query cq = getEntityManager().createNamedQuery("UsuarioAplicacion.findByPk");
        cq.setParameter("nombreUsuario",usuario);
        cq.setParameter("codApi",codApi);
        return null;
    }
    public UsuarioAplicacionFacade() {
        super(UsuarioAplicacion.class);
    }
    
}
