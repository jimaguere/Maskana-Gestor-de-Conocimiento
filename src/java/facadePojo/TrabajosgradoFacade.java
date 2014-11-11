/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pojo.Docente;
import pojo.Tgautor;
import pojo.TrabajosGrado;

/**
 *
 * @author mateo
 */
@Stateless
public class TrabajosGradoFacade extends AbstractFacade<TrabajosGrado> {

    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TrabajosGradoFacade() {
        super(TrabajosGrado.class);
    }

    public List<TrabajosGrado> findByRango(int li, int max) {
        Query cq = getEntityManager().createNamedQuery("Trabajosgrado.findByRango");
        cq.setFirstResult(li);
        cq.setMaxResults(max);
        return cq.getResultList();
    }
    
     public List<Object[]> findAllJaro(String query) {
        try {
            return getEntityManager().createNativeQuery("select id_tg ,"
                    + " jarowinkler(titulo ,'" + query + "') as aceptacion"
                    + " from trabajosgrado where"
                    + " jarowinkler(titulo,'" + query + "')>0.4 "
                    + " order by aceptacion desc").setMaxResults(10).getResultList();
        } catch (Exception e) {
            return new ArrayList<Object[]>();
        }
    }
     
     public Integer opbtenerSecuencia(){
         try{
              String numero=getEntityManager().
                      createNativeQuery("SELECT * FROM nextval('trabajosgrado_id_tg_seq')").
                      getSingleResult().toString();
              Integer in=new Integer(numero);
              return in;
         }catch(Exception e){
             return  null;
         
         }
     }
     
     public List<TrabajosGrado> findById(String id){
         try{
             Query query=getEntityManager().createNamedQuery("TrabajosGrado.findByIdTg");
             query.setParameter("idTg", Integer.parseInt(id));
             return query.getResultList();
         }catch(Exception e){
             System.out.println("error:"+e.toString());
         }
         return null;
     }
     
     public boolean crearTrabajoGrado(TrabajosGrado trabajoGrado){
         this.getEntityManager().setFlushMode(FlushModeType.COMMIT);
         try{
             Query query=getEntityManager().createNativeQuery("INSERT INTO trabajosgrado"
                     + "("
                        + "id_tg,"
                        + "titulo,"
                        + "codigo_linea,"
                        + "cod_modalidad,"
                        + "visitas,"
                        + "fechasustentacion"
                     + ")VALUES(?,?,?,?,?,(select current_date))");
             query.setParameter(1, trabajoGrado.getIdTg());
             query.setParameter(2, trabajoGrado.getTitulo());
             query.setParameter(3, trabajoGrado.getCodigoLinea().getCodigoLinea()); 
             query.setParameter(4, trabajoGrado.getCodModalidad().getCodModalidad());
             query.setParameter(5, new Integer("0"));
             query.executeUpdate();
             this.getEntityManager().flush();
         }catch(Exception e){
             System.out.println("Error persist:"+e.toString());
             return false;
         }
         try{
              for(Tgautor autor:trabajoGrado.getTgautorList()){
                    Query query=getEntityManager().createNativeQuery("INSERT INTO tgautor"
                     + "("
                        + "id_tg,"
                        + "id_estudiante"
                     + ")VALUES(?,?)");
                    query.setParameter(1, autor.getTrabajosGrado().getIdTg());
                    query.setParameter(2, autor.getEstudiante().getId());
                    query.executeUpdate();
                    this.getEntityManager().flush();
              }
         }catch(Exception e){
             System.out.println("Error persist:"+e.toString());
             return false;
         }
         try{
             for(Docente docente:trabajoGrado.getDocenteList()){
                 Query query=getEntityManager().createNativeQuery("INSERT INTO tgasesor"
                     + "("
                        +"id_docente,"
                        + "id_tg"
                     + ")VALUES(?,?)");
                    query.setParameter(1, docente.getId());
                    query.setParameter(2, trabajoGrado.getIdTg());
                    query.executeUpdate();
                    this.getEntityManager().flush();
             }
         }catch(Exception e){
             System.out.println("Error persist:"+e.toString());
             return false;
         }
          return true;
     }
}
