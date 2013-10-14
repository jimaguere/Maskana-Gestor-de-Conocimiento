/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pojo.Significado;

/**
 *
 * @author mateo
 */
@Stateless
public class SignificadoFacade extends AbstractFacade<Significado> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SignificadoFacade() {
        super(Significado.class);
    }
    
}
