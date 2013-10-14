/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pojo.Aplicacion;

/**
 *
 * @author mateo
 */
@Stateless
public class AplicacionFacade extends AbstractFacade<Aplicacion> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AplicacionFacade() {
        super(Aplicacion.class);
    }
    
}
