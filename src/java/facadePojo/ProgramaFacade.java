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
import pojo.Programa;

/**
 *
 * @author mateo
 */
@Stateless
public class ProgramaFacade extends AbstractFacade<Programa> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgramaFacade() {
        super(Programa.class);
    }

    public List<Programa> findByCodProg(String codProg) {
        Query cq = getEntityManager().createNamedQuery("Programa.findByCodProg");
        cq.setParameter("codProg",codProg);
        return cq.getResultList();
    }

    public List<Programa> findByCodDep(String codDep) {
        Query cq = getEntityManager().createNamedQuery("Programa.findByCodDep");
        cq.setParameter("codDep", codDep);
        return cq.getResultList();
    }
}
