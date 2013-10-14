/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pojo.Keyword;

/**
 *
 * @author mateo
 */
@Stateless
public class KeywordFacade extends AbstractFacade<Keyword> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KeywordFacade() {
        super(Keyword.class);
    }
    
}
