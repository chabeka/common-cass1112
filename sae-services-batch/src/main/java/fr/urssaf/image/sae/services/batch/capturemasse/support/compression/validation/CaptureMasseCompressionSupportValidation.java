package fr.urssaf.image.sae.services.batch.capturemasse.support.compression.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.compression.CaptureMasseCompressionSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class CaptureMasseCompressionSupportValidation {
   
   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String CHECK_METHOD = "execution(boolean fr.urssaf.image.sae.services.batch.capturemasse.support.compression.CaptureMasseCompressionSupport.isDocumentToBeCompress(*,*))"
         + " && args(document,ecdeDirectory)";
   
   private static final String COMPRESS_METHOD = "execution(fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument fr.urssaf.image.sae.services.batch.capturemasse.support.compression.CaptureMasseCompressionSupport.compresserDocument(*,*))"
         + " && args(document,ecdeDirectory)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * isDocumentToBeCompress possède tous les arguments renseignés
    * 
    * @param document
    *           UntypedDocument
    * @param ecdeDirectory
    *    chemin absolu du répertoire de traitement de l'ECDE
    */
   @Before(CHECK_METHOD)
   public final void isDocumentToBeCompress(final UntypedDocument document, final File ecdeDirectory) {

      if (document == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "document"));
      }
      
      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }
   }
   
   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * compresserDocument possède tous les arguments renseignés
    * 
    * @param document
    *           UntypedDocument
    * @param ecdeDirectory
    *    chemin absolu du répertoire de traitement de l'ECDE
    */
   @Before(COMPRESS_METHOD)
   public final void compresserDocument(final UntypedDocument document, final File ecdeDirectory) {
      
      if (document == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "document"));
      }
      
      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }
   }
}
