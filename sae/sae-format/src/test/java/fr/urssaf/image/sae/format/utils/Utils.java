package fr.urssaf.image.sae.format.utils;

import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;

/**
 * 
 * Classe utilitaire pour les tests.
 *
 */
public final class Utils {

   /**
    * genere le referentiel format du PDFA/A-1b
    * @return
    */
   public static FormatFichier genererRefFormatPdfa() {
      FormatFichier refFormat = new FormatFichier();
      refFormat.setIdFormat("fmt/354");
      refFormat.setTypeMime("application/pdf");
      refFormat.setExtension("Pdf");
      refFormat.setDescription("Fichier PDF conforme PDF/A-1b");
      refFormat.setVisualisable(true);
      refFormat.setValidator("PdfaValidatorImpl");
      refFormat.setIdentificateur("PdfaIdentifierImpl");
      return refFormat;
   }
   
   public static FormatFichier getRefFormParamObligManquant() {
      FormatFichier refFormat = new FormatFichier();
      //refFormat.setIdFormat("1");
      refFormat.setTypeMime("typeMime");
      refFormat.setExtension("extension");
      //refFormat.setDescription("desc");
      refFormat.setVisualisable(true);
      refFormat.setValidator("validator");
      refFormat.setIdentificateur("identificateur");
      return refFormat;
   }
   
   /**
    * genere le referentiel format lambda simplement pour les tests
    * @return
    */
   public static FormatFichier genererRefFormatLambda() {
      FormatFichier refFormat = new FormatFichier();
      refFormat.setIdFormat("lambda");
      refFormat.setTypeMime("application/lambda");
      refFormat.setExtension("Lambda");
      refFormat.setDescription("Lambda, simplement pour les tests");
      refFormat.setVisualisable(true);
      refFormat.setValidator("LambdaValidatorImpl");
      refFormat.setIdentificateur("LambdaIdentifierImpl");
      return refFormat;
   }
   
   
   private Utils() {
      // cette classe ne doit pas etre instancié.
   }
}
