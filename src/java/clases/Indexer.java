/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author mateo
 */
public class Indexer {

    private IndexWriter writer;

    /**
     * 
     * @param args
     * @throws Exception 
     * crea el indice con los archivos en formato pdf
     */
    public static void main(String[] args) throws Exception {
        String indexDir = "/home/mateo/Documentos/Indice";
        String dataDir = "/home/mateo/Documentos/pdf";
        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed;
        try {
            numIndexed = indexer.index(dataDir, new TextFilesFilter());
        } finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();
        System.out.println("Indexing " + numIndexed + " files took "
                + (end - start) + " milliseconds");
    }
/**
 * 
 * @param indexDir
 * @throws IOException 
 * creaa lucene IndexWriter
 */
    public Indexer(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(new File(indexDir));
        writer = new IndexWriter(dir,
                new IndexWriterConfig(Version.LUCENE_46, new StandardAnalyzer(Version.LUCENE_46)));
    }
    /**
     * 
     * @throws IOException 
     * cierra lucene IndexWriter
     */
    public void close() throws IOException {
        writer.close();
    }
    /**
     * 
     * @param dataDir
     * @param filter
     * @return
     * @throws Exception 
     * retorna el numero de documentos indexados
     */
    public int index(String dataDir, FileFilter filter)
            throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File f : files) {
            if (!f.isDirectory()
                    && !f.isHidden()
                    && f.exists()
                    && f.canRead()
                    && (filter == null || filter.accept(f))) {
                indexFile(f);
            }

        }
        return writer.numDocs();
    }
    /**
     * acepta solo documentos en formato PDF
     */
    private static class TextFilesFilter implements FileFilter {

        @Override
        public boolean accept(File path) {
            return path.getName().toLowerCase().endsWith(".pdf");
        }
    }
    /**
     * 
     * @param f
     * @return
     * @throws Exception 
     * indexa el contenido del archivo
     * indexa el nombre del archivo
     * indexa la ruta
     */
    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new Field("contents",
                new FileReader(f)));
        doc.add(new Field("filename", f.getName(),
                Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("fullpath", f.getCanonicalPath(),
                Field.Store.NO, Field.Index.ANALYZED));
        return doc;
    }
    
    //adicciona el documento al indixe
    private void indexFile(File f) throws Exception {
        System.out.println("Indexing " + f.getCanonicalPath());
        Document doc = getDocument(f);
        writer.addDocument(doc);
    }
}
