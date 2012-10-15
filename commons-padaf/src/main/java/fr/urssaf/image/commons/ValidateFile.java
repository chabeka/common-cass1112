package fr.urssaf.image.commons;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import javax.activation.FileDataSource;

import net.padaf.preflight.PdfA1bValidator;
import net.padaf.preflight.PdfAValidatorFactory;
import net.padaf.preflight.ValidationResult;
import net.padaf.preflight.ValidationResult.ValidationError;
import net.padaf.preflight.javacc.PDFParser;

import org.apache.commons.io.FileUtils;

public class ValidateFile {

   /**
    * @param args
    */
   @SuppressWarnings("unchecked")
   public static void main(String[] args) throws Exception {
      ValidateFile validate = new ValidateFile();
      boolean showDetails = false;
      String root = null;
      if (args.length > 1 && args[0] != null) {
         root = args[0];
      } else {
         System.out
               .println("Veuillez preciser comme premier argument le répertoire contenant les fichiers à analyser et en deuxième le type de l'analyseur PDFBOX ou PADAF");
         System.exit(0);
      }
      if (args.length > 2 && args[2] != null && args[2].equals("True")) {
         showDetails = true;
      }
      
      Collection<File> col = FileUtils.listFiles(new File(root),
            new String[] { "pdf" }, true);
      for (File f : col) {

         if (args[1] != null && args[1].equals("PDFBOX")) {
            // validate.identifyFilePdfBox(f);
         } else if (args[1] != null && args[1].equals("PADAF")) {
            //System.out.println("traitement de " + f.getName());
            validate.identifyFilePadaf(f, showDetails);
         } else {
            System.out
                  .println("Veuillez préciser l'outil d'analyse souhaité en argument PBFBOX ou PADAF");
         }
         try {
         } catch (Exception e) {
            System.out.println(e.getMessage());
         }
      }

   }

   private void identifyFilePadaf(File f, boolean showDetails) throws Exception {
      /**
       * @param f
       *           fichier à analyser
       */
      PdfA1bValidator validator = new PdfA1bValidator(PdfAValidatorFactory
            .getStandardPDFA1BConfiguration());
      FileDataSource fd = new FileDataSource(f.getAbsolutePath());
      ValidationResult result = validator.validate(fd);
      if (result.isValid()) {
         result.closePdf();
         System.out.println("The file " + f.getName()
               + " is a valid PDF/A-1b file");
      } else {
         System.out.println("The file " + f.getName()
               + " is not valid, error(s) :");
         if (showDetails) {
            for (ValidationError error : result.getErrorsList()) {
               System.out.println(error.getErrorCode() + " : "
                     + error.getDetails());
            }
         }
         result.closePdf();
      }

   }
   
   
   private void identifyFilePadaf2(File f, boolean showDetails) throws Exception {
      /**
       * @param f
       *           fichier à analyser
       */

      boolean result = PDFParser.parse(new FileInputStream(f));
      if (result) {
         System.out.println("The file " + f.getName()
               + " is a valid PDF/A-1b file");
      } else {
         System.out.println("The file " + f.getName()
               + " is not valid, error(s) :");
         if (showDetails) {
          /*  for (ValidationError error : result.getErrorsList()) {
               System.out.println(error.getErrorCode() + " : "
                     + error.getDetails());
            }*/
         }
      }

   }
   

}
