package fr.urssaf.image.commons;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.activation.FileDataSource;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.exception.SyntaxValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;

public class ValidateFile {

   /**
    * Parcours tous les fichiers PDF du répertoire donnée pour vérihier leur
    * validité.
    * 
    * @param args
    *           le premier paramètre fourni doit être le répertoire contenant
    *           les fichiers à analyser. Le deuxième paramètre est le type
    *           d'analyseur PDFBOX ou PADAF.
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public static void main(String[] args) throws Exception {
      ValidateFile validate = new ValidateFile();
      boolean showDetails = false;
      String root = null;
      
      if (args.length>1  && args[0] != null) {
         root = args[0];
      } else {
         System.out
               .println("Veuillez preciser comme premier argument le répertoire contenant les fichiers à analyser et en deuxième le type de l'analyseur PDFBOX ou PADAF");
         System.exit(0);
      }
      if(args.length>2 && args[2]!=null && args[2].equals("True")){
         showDetails = true;
      }
      Collection<File> col = FileUtils.listFiles(new File(root),
            new String[] { "pdf" }, true);
      for (File f : col) {

         if (args[1] != null && args[1].equals("PDFBOX")) {
            validate.identifyFilePdfBox(f, showDetails);
         } else if (args[1] != null && args[1].equals("PADAF")) {
            validate.identifyFilePadaf(f);
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

   private void identifyFilePadaf(File f) throws Exception {
      /**
       * @param f fichier à analyser
       */
 /*     PdfA1bValidator validator = new PdfA1bValidator(PdfAValidatorFactory
            .getStandardPDFA1BConfiguration());
      FileDataSource fd = new FileDataSource(f.getAbsolutePath());
      net.padaf.preflight.ValidationResult result = validator.validate(fd);
      if (result.isValid()) {
         result.closePdf();
         System.out.println("The file " + f.getName()
               + " is a valid PDF/A-1b file");
      } else {
         System.out.println("The file" + f.getName()
               + " is not valid, error(s) :");
         for (net.padaf.preflight.ValidationResult.ValidationError error : result
               .getErrorsList()) {
            System.out.println(error.getErrorCode() + " : "
                  + error.getDetails());
         }
         result.closePdf();
      }*/

   }

   private void identifyFilePdfBox(File f, boolean showDetails) throws IOException {
      /**
       * @param f fichier à analyser
       */
      ValidationResult result = null;

      FileDataSource fd = new FileDataSource(f.getAbsolutePath());
      PreflightParser parser = new PreflightParser(fd);
      try {

         /*
          * Parse the PDF file with PreflightParser that inherits from the
          * NonSequentialParser. Some additional controls are present to check a
          * set of PDF/A requirements. (Stream length consistency, EOL after
          * some Keyword...)
          */
         parser.parse();

         /*
          * Once the syntax validation is done, the parser can provide a
          * PreflightDocument (that inherits from PDDocument) This document
          * process the end of PDF/A validation.
          */
         PreflightDocument document = parser.getPreflightDocument();
         document.validate();

         // Get validation result
         result = document.getResult();
         document.close();

      } catch (SyntaxValidationException e) {
         /*
          * the parse method can throw a SyntaxValidationExceptionif the PDF
          * file can't be parsed.
          */
         // In this case, the exception contains an instance of ValidationResult
         result = e.getResult();
      }

      // display validation result
      if (result.isValid()) {
         System.out.println("The file " + f.getName()
               + " is a valid PDF/A-1b file");
      } else {
         System.out.println("The file " + f.getName()
               + " is not valid, error(s) :");
        if(showDetails){ 
         for (ValidationError error : result.getErrorsList()) {
            System.out.println(error.getErrorCode() + " : "
                  + error.getDetails());
         }
        }
      }

   }

}
