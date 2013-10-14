/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pojo.Vocabulario;

/**
 *
 * @author mateo
 */
@Stateless
public class VocabularioFacade extends AbstractFacade<Vocabulario> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VocabularioFacade() {
        super(Vocabulario.class);
    }
    
}
