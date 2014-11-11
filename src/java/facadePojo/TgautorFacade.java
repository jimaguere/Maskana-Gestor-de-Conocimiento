/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facadePojo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pojo.Tgautor;
import pojo.TrabajosGrado;

/**
 *
 * @author mateo
 */
@Stateless
public class TgautorFacade extends AbstractFacade<Tgautor> {
    @PersistenceContext(unitName = "MascanaGestorConocimientoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TgautorFacade() {
        super(Tgautor.class);
    }
    
    public boolean crearAutor(TrabajosGrado trabajoGrado){
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
                    getEntityManager().flush();
                    return true;
              }
         }catch(Exception e){
             System.out.println("Error persist:"+e.toString());
             return false;
         }
        return false;
    }
    
}
