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
import pojo.TrabajosGrado;

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

    public List<Docente> findByRango(int li, int max) {
        Query cq = getEntityManager().createNamedQuery("Docente.findAll");
        cq.setFirstResult(li);
        cq.setMaxResults(max);
        return cq.getResultList();
    }

    public List<Object[]> findAllJaro(String query) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
            return getEntityManager().createNativeQuery("select (nombre ||' '|| apellido) as nom  ,"
                    + " jarowinkler((nombre ||' '|| apellido) ,'" + query + "') as aceptacion,codocente"
                    + " from docente where"
                    + " jarowinkler((nombre ||' '|| apellido),'" + query + "')>0.6 order by aceptacion desc").setMaxResults(10).getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }


    }

    public Integer opbtenerSecuencia() {
        try {
            String numero = getEntityManager().
                    createNativeQuery("SELECT * FROM nextval('docente_id_seq')").
                    getSingleResult().toString();
            Integer in = new Integer(numero);
            return in;
        } catch (Exception e) {
            return null;
     
        }
    }

    public DocenteFacade() {
        super(Docente.class);
    }
    public boolean crearDirector(TrabajosGrado trabajoGrado){
        try{
             for(Docente docente:trabajoGrado.getDocenteList()){
                 Query query=getEntityManager().createNativeQuery("INSERT INTO tgasesor"
                     + "("
                        + "id_tg,"
                        + "id_docente"
                     + ")VALUES(?,?)");
                    query.setParameter(1, docente.getId());
                    query.setParameter(2, trabajoGrado.getIdTg());
                    query.executeUpdate();
                    getEntityManager().flush();
             }
         }catch(Exception e){
             System.out.println("Error persist:"+e.toString());
             return false;
         }
        return true;
    }
}
