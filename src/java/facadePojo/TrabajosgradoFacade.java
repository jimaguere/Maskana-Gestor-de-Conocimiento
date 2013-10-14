/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pojo.Trabajosgrado;

/**
 *
 * @author mateo
 */
@Stateless
public class TrabajosgradoFacade extends AbstractFacade<Trabajosgrado> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrabajosgradoFacade() {
        super(Trabajosgrado.class);
    }
    
}
