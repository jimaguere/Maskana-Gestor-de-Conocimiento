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
import pojo.Docente;

/**
 *
 * @author mateo
 */
@Stateless
public class DocenteFacade extends AbstractFacade<Docente> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public List<Docente>findByRango(int li,int max){
        Query cq = getEntityManager().createNamedQuery("Docente.findAll");
        cq.setFirstResult(li);
        cq.setMaxResults(max);
        return cq.getResultList(); 
    }
    public List<Object[]>findAllJaro(String query){
         try{
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(Docente.class));
        return getEntityManager().createNativeQuery("select (nombre ||' '|| apellido) as nom  ,"
                + " jarowinkler((nombre ||' '|| apellido) ,'"+query+"') as aceptacion,codocente"
                + " from docente where"
                + " jarowinkler((nombre ||' '|| apellido),'"+query+"')>0.6 order by aceptacion desc").setMaxResults(10).getResultList();
        }catch(Exception e){
            System.out.println(e.toString());
            return null;
        }
       
        
    }
    public DocenteFacade() {
        super(Docente.class);
    }
    
}
