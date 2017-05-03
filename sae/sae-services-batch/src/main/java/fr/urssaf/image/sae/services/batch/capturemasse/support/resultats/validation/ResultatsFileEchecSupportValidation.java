/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.validation;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;

/**
 * Validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class ResultatsFileEchecSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String WRITE_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport.writeResultatsFile(*,*,*,*,*,*,*))"
         + " && args(ecdeDirectory,sommaireFile,erreur,nombreDocsTotal,nbDocumentsIntegres,batchModeTraitement,listIntDocs)";

   private static final String WRITE_VRTL_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport.writeVirtualResultatsFile(*,*,*,*,*,*,*))"
         + " && args(ecdeDirectory,sommaireFile,erreur, nombreDocsTotal,nbDocumentsIntegres,batchModeTraitement,listIntDocs)";

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
    * @param nombreDocsTotal
    *           nombre total de documents
    * @param nbDocumentsIntegres
    *           nombre de documents intégréslors du traitement
    * @param batchModeTraitement
    *           Mode de traitement du batch
    * @param listIntDocs
    *           Liste des documents intégrés
    * 
    */
   @Before(WRITE_METHOD)
   public final void checkWriteResultats(final File ecdeDirectory,
         final File sommaireFile, final CaptureMasseErreur erreur,
         final int nombreDocsTotal, final int nbDocumentsIntegres,
         final String batchModeTraitement,
         final ConcurrentLinkedQueue<?> listIntDocs) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

      if (sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "sommaireFile"));
      }

      if (erreur == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "erreur"));
      }

      if (StringUtils.isBlank(batchModeTraitement)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "batchModeTraitement"));
      }
   }

   /**
    * Vérifie que tous les arguments de la méthodes sont bien présents pour la
    * méthode writeVirtualResultats
    * 
    * @param ecdeDirectory
    *           répertoire ecde de traitement pour une capture de masse
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml d'une capture de masse
    * @param erreur
    *           erreur mère
    * @param nombreDocsTotal
    *           nombre total de documents
    * @param nbDocumentsIntegres
    *           nombre de documents intégréslors du traitement
    * @param batchModeTraitement
    *           Mode de traitement du batch
    * @param listIntDocs
    *           Liste des documents intégrés
    */
   @Before(WRITE_VRTL_METHOD)
   public final void checkWriteVirtualResultats(final File ecdeDirectory,
         final File sommaireFile, final CaptureMasseErreur erreur,
         final int nombreDocsTotal, final int nbDocumentsIntegres,
         final String batchModeTraitement,
         final ConcurrentLinkedQueue<?> listIntDocs) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

      if (sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "sommaireFile"));
      }

      if (erreur == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "erreur"));
      }

      if (StringUtils.isBlank(batchModeTraitement)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "batchModeTraitement"));
      }

   }

}
