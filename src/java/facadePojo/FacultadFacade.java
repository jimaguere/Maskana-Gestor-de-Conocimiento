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
import pojo.Facultad;

/**
 *
 * @author mateo
 */
@Stateless
public class FacultadFacade extends AbstractFacade<Facultad> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FacultadFacade() {
        super(Facultad.class);
    }
    public void begin(){
        getEntityManager().getTransaction().begin();
    }
    public void commit(){
        getEntityManager().getTransaction().commit();
    }
    public void rollback(){
         getEntityManager().getTransaction().rollback();
    }
    public List<Facultad> findByCodigo(String codigo){
         Query cq = getEntityManager().createNamedQuery("Facultad.findByCodFac");
         cq.setParameter("codFac", codigo);
         return cq.getResultList();
    }
}
