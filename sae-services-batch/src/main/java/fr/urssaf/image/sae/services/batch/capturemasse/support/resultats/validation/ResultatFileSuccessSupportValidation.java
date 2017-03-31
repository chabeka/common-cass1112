/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.validation;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;

/**
 * Validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport}
 * . La validation est basée sur la programmation orientée Aspect
 * 
 */
@Aspect
public class ResultatFileSuccessSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String CHECK_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport.writeResultatsFile(*,*,*,*,*))"
         + " && args(ecdeDirectory,integDocs,documentsCount,restitutionUuids,sommaireFile)";

   private static final String CHECK_VRTL_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport.writeVirtualResultatsFile(*,*,*,*,*))"
         + " && args(ecdeDirectory,integDocs,documentsCount,restitutionUuids,sommaireFile)";

   /**
    * permet de vérifier que les éléments suivants sont présents :<br>
    * <ul>
    * <li>ecdeDirectory</li>
    * <li>documentsCount</li>
    * </ul>
    * 
    * @param ecdeDirectory
    *           répertoire de traitement du traitement de masse
    * @param integDocs
    *           liste des documents intégrés
    * @param documentsCount
    *           nombre de documents intégrés
    * @param restitutionUuids
    *           Ecriture ou non l'UUID des documents intégrés dans le
    *           resultat.xml
    * @param sommaireFile
    *           Fichier sommaire.xml
    */
   @Before(CHECK_METHOD)
   public final void checkWriteResultats(final File ecdeDirectory,
         final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs,
         final int documentsCount, final boolean restitutionUuids,
         final File sommaireFile) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

      if (documentsCount < 0) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "documentsCount"));
      }

      if (restitutionUuids && sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "documentsCount"));
      }

   }

   /**
    * permet de vérifier que les éléments suivants sont présents :<br>
    * <ul>
    * <li>ecdeDirectory</li>
    * <li>documentsCount</li>
    * </ul>
    * 
    * @param ecdeDirectory
    *           répertoire de traitement du traitement de masse
    * @param integDocs
    *           liste des documents intégrés
    * @param documentsCount
    *           nombre de documents intégrés
    * @param restitutionUuids
    *           Ecriture ou non l'UUID des documents intégrés dans le
    *           resultat.xml
    * @param sommaireFile
    *           Fichier sommaire.xml
    */
   @Before(CHECK_VRTL_METHOD)
   public final void checkVirtualWriteResultats(final File ecdeDirectory,
         final ConcurrentLinkedQueue<CaptureMasseVirtualDocument> integDocs,
         final int documentsCount, final boolean restitutionUuids,
         final File sommaireFile) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

      if (restitutionUuids && CollectionUtils.isEmpty(integDocs)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "liste des documents intégrés"));
      }

      if (documentsCount < 0) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "documentsCount"));
      }

      if (restitutionUuids && sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "fichier sommaire"));
      }

   }

}
