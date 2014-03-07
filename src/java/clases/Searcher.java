/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author mateo
 */
public class Searcher {

    public static void main(String[] args) throws IllegalArgumentException,
            IOException, org.apache.lucene.queryparser.classic.ParseException {
        String indexDir = "/home/mateo/Documentos/Indice";
        String q = "aplicación  ofrece  la  ayuda  correspondiente  en  cuanto  a  su  manejo  e instalación  y  además  permite  descargar  los  manuales  para  edición";
        search(indexDir, q);
    }

    public static void search(String indexDir, String q)
            throws IOException, org.apache.lucene.queryparser.classic.ParseException {
        FSDirectory dir = FSDirectory.open(new File(indexDir));
        IndexSearcher is = new IndexSearcher(IndexReader.open(dir));


        QueryParser parser = new QueryParser(Version.LUCENE_35,
                "contents",
                new StandardAnalyzer(
                Version.LUCENE_35));
        Query query = parser.parse(q);
        long start = System.currentTimeMillis();
        TopDocs hits = is.search(query, 10);
        long end = System.currentTimeMillis();
        System.err.println("Found " + hits.totalHits
                + " document(s) (in " + (end - start)
                + " milliseconds) that matched query '"
                + q + "':");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            System.out.println(doc.get("fullpath"));
        }
        
    }
}
