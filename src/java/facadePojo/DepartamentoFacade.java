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
import pojo.Departamento;

/**
 *
 * @author mateo
 */
@Stateless
public class DepartamentoFacade extends AbstractFacade<Departamento> {

    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartamentoFacade() {
        super(Departamento.class);
    }

    public List<Departamento> findByCodep(String codDep) {
        Query cq = getEntityManager().createNamedQuery("Departamento.findByCodDep");
        cq.setParameter("codDep", codDep);
        return cq.getResultList();
    }
    public List<Departamento> findByCodFac(String codFac){
        Query cq = getEntityManager().createNamedQuery("Departamento.findByCodFac");
        cq.setParameter("codFac", codFac);
        return cq.getResultList();
    }
}
