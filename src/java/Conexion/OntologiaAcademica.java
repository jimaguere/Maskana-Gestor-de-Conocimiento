/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;

/**
 *
 * @author mateo
 */
public class OntologiaAcademica {

    com.hp.hpl.jena.query.Query query;
    QueryExecution qexec;
    OntModel model;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public QueryExecution getQexec() {
        return qexec;
    }

    public void setQexec(QueryExecution qexec) {
        this.qexec = qexec;
    }

    public OntModel getModel() {
        return model;
    }

    public void setModel(OntModel model) {
        this.model = model;
    }

    public com.hp.hpl.jena.query.ResultSet consultar(String consulta) {
        query = QueryFactory.create(consulta);
        qexec = QueryExecutionFactory.create(query, model);
        com.hp.hpl.jena.query.ResultSet results = qexec.execSelect();
        return results;
    }

    public com.hp.hpl.jena.query.ResultSet consultarAvanzada(String consulta) {
        query = QueryFactory.create(consulta, Syntax.syntaxARQ);
        qexec = QueryExecutionFactory.create(query, model);
        com.hp.hpl.jena.query.ResultSet results = qexec.execSelect();
        return results;
    }

    public void terminar() {
        qexec.close();
    }
}
