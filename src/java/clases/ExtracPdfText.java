/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mateo
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


/**
 *
 * @author mateo
 */
public class ExtracPdfText {

    private File archivoPdf;
    //private PDFText pdfText;
    private final PDDocument doc;

    public ExtracPdfText(InputStream archivo) throws IOException{
        doc = PDDocument.load (archivo); 
        //pdfText = new PDFText(archivo, null);
    }

    public File getArchivoPdf() {
        return archivoPdf;
    }

    public void setArchivoPdf(File archivoPdf) {
        this.archivoPdf = archivoPdf;
    }


    public String extraerTexto() throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1);
        stripper.setEndPage(stripper.getEndPage());
        stripper.setAddMoreFormatting(true);
        String docText = stripper.getText(doc);
        return docText.replaceAll("","").replaceAll("~","").replaceAll("","").replaceAll(" ","").replaceAll("`","").replaceAll("", "").replaceAll("","").replaceAll(" ","").replaceAll("","").replaceAll("","").replaceAll("", "");
    }

    public void cerrar() throws IOException {
        doc.close();
    }
    
    public void extarerUnaPagina() throws IOException{
             PDFTextStripper stripper = new PDFTextStripper();
             stripper.setStartPage(3);
             stripper.setEndPage(3);
             System.out.println(stripper.getText(doc));
               
    }

    public String extraerTitulo() throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1);
        stripper.setEndPage(stripper.getEndPage());
        stripper.setAddMoreFormatting(true);
     
        String titulo = stripper.getText(doc);
        String[] resultado = titulo.split("\n");
        String nuevoTitulo = "";
        boolean b = false;
        int contador = 0;
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i].trim().length() > 0) {
                b = true;
            }
            if (resultado[i].trim().length() == 0 && b) {
                contador++;
                if(contador>1){
                    break;
                }
            } else {
                nuevoTitulo=nuevoTitulo+" "+resultado[i];
            }
        }
        return nuevoTitulo.trim().replaceAll("","").replaceAll("~","").replaceAll("","").replaceAll(" ","").replaceAll("`","").replaceAll("", "").replaceAll("","").replaceAll("","").replaceAll("","").replaceAll("", "");
    }

    public String extraerPalabrasClave() throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        return stripper.getWordSeparator();
    }


   

    public static void main(String arg[]) throws  IOException {
        InputStream archivoI = new FileInputStream("/home/mateo/Electiva2/Documento final/ontologias.pdf");
        ExtracPdfText texto = new ExtracPdfText(archivoI);
       // System.out.println("titulo:" + texto.extraerTitulo());
        //texto.palabras();
        //  System.out.println("palabras clave:"+texto.extraerPalabrasClave());
        //   System.out.println("autor:"+texto.extraerAutor());
           //System.out.println("contenido:"+texto.extraerTexto());
           texto.extarerUnaPagina();
        //texto.extrarPagina();
    }
}