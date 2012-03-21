/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireDocumentException;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class ResultatsFileEchecSupportValidation {

   private static final String WRITE_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport.writeResultatsFile(*,*,*))"
         + " && args(ecdeDirectory,sommaireFile,erreur)";

   /**
    * Vérifie que tous les arguments de la méthodes sont bien présents pour la
    * méthode writeResultats
    * 
    * @param ecdeDirectory
    *           répertoire ecde de traitement pour une capture de masse
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml d'une capture de masse
    * @param erreur
    *           erreur mère
    */
   @Before(WRITE_CONTROLES_METHOD)
   public final void checkWriteResultats(File ecdeDirectory, File sommaireFile,
         CaptureMasseSommaireDocumentException erreur) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeDirectory"));
      }

      if (sommaireFile == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "sommaireFile"));
      }

      if (erreur == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "erreur"));
      }

   }

}
