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
import pojo.Estudiante;

/**
 *
 * @author mateo
 */
@Stateless
public class EstudianteFacade extends AbstractFacade<Estudiante> {

    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EstudianteFacade() {
        super(Estudiante.class);
    }

    public List<Estudiante> findByRango(int li, int max) {
        Query cq = getEntityManager().createNamedQuery("Estudiante.findByOrder");
        cq.setFirstResult(li);
        cq.setMaxResults(max);
        return cq.getResultList();
    }

    public Integer opbtenerSecuencia() {
        try {
            String numero = getEntityManager().
                    createNativeQuery("SELECT * FROM nextval('estudiante_id_seq')").
                    getSingleResult().toString();
            Integer in = new Integer(numero);
            return in;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Object[]> findAllJaro(String query) {
        try {
            return getEntityManager().createNativeQuery("select (nombres ||' '|| apellidos) as nom  ,"
                    + " jarowinkler((nombres ||' '|| apellidos) ,'" + query + "') as aceptacion,codestudiante"
                    + " from estudiante where"
                    + " jarowinkler((nombres ||' '|| apellidos),'" + query + "')>0.6 "
                    + " union"
                    + " select (nombres ||' '|| apellidos) as nom  ,"
                    + " jarowinkler((apellidos||' '||nombres ) ,'" + query + "') as aceptacion,codestudiante"
                    + " from estudiante where"
                    + " jarowinkler((apellidos ||' '|| nombres),'" + query + "')>0.6 "
                    + " union"
                    + " select (nombres ||' '|| apellidos) as nom  ,"
                    + " jarowinkler((nombres ) ,'" + query + "') as aceptacion,codestudiante"
                    + " from estudiante where"
                    + " jarowinkler((nombres),'" + query + "')>0.7 "
                    + " union"
                    + " select (nombres ||' '|| apellidos) as nom  ,"
                    + " jarowinkler((apellidos) ,'" + query + "') as aceptacion,codestudiante"
                    + " from estudiante where"
                    + " jarowinkler((apellidos),'" + query + "')>0.7 "
                    + " order by aceptacion desc").setMaxResults(10).getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        // return getEntityManager().createQuery(cq).getResultList();
    }
}
