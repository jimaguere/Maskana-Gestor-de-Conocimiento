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
    public List<Object[]> findAllJaroWordsComplet(String query) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulario.class));
            return getEntityManager().createNativeQuery("select distinct palabra,"
                    + " jarowinkler((palabra) ,'" + query + "') as aceptacion"
                    + " from vocabulario"
                    + " where"
                    + " jarowinkler(palabra,'" + query + "')>0.6 order by aceptacion desc").getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    public List<Object[]> findAllJaroWords(String query) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulario.class));
            return getEntityManager().createNativeQuery("select distinct palabra,"
                    + " jarowinkler((palabra) ,'" + query + "') as aceptacion"
                    + " from vocabulario"
                    + " where"
                    + " jarowinkler(palabra,'" + query + "')>0.9 order by aceptacion desc").getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
    public List<Object[]>findAllJaraWordsContent(String query){
        try{
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulario.class));
            return getEntityManager().createNativeQuery("select distinct palabra,"
                    + " jarowinkler((palabra) ,'" + query + "') as aceptacion"
                    + " from vocabulario"
                    + " where"
                    + " jarowinkler(palabra,'" + query + "')>0.9 "
                    + " union"
                    + " select distinct meaning as palabra,"
                    +" jarowinkler((meaning) ,'" + query + "') as aceptacion"
                    + " from significado"
                    + " where"
                    + " jarowinkler(meaning,'" + query + "')>0.9"
                    + "order by aceptacion desc").getResultList();
        }catch(Exception e){
            return null;
        }
    }
    
    public Object findAllStopWords(String query) {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vocabulario.class));
            return getEntityManager().createNativeQuery("SELECT ts_lexize('public.simple_dict',"
                    + "'" + query + "');").getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public void crearVocabulario(String palabra){
        try{
            Query query=getEntityManager().createNativeQuery("Insert into vocabulario(palabra)values(?)");
            query.setParameter(1,palabra);
            query.executeUpdate();
        }catch(Exception e){
            System.out.println("error vabulario:"+e.toString());
        }
    }
    
    public String findLexema(String palabra){
        try{
            Query query=getEntityManager().
                    createNativeQuery("SELECT lexemes as palabra FROM ts_debug('spanish',?)");
            query.setParameter(1,palabra);
            Object[] objeto=(Object[]) query.getSingleResult();
            return objeto[0].toString();
        }catch(Exception e){
            return null;
        }
    }
    public VocabularioFacade() {
        super(Vocabulario.class);
    }
    
}
