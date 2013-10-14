/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pojo.Tgautor;

/**
 *
 * @author mateo
 */
@Stateless
public class TgautorFacade extends AbstractFacade<Tgautor> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TgautorFacade() {
        super(Tgautor.class);
    }
    
}
