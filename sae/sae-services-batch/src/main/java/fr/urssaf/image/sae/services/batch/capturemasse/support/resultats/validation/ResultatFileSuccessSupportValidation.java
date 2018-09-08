/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.validation;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.batch.capturemasse.model.CaptureMasseVirtualDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.model.TraitementMasseIntegratedDocument;

/**
 * Validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport}
 * . La validation est basée sur la programmation orientée Aspect
 * 
 */
@Aspect
public class ResultatFileSuccessSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String CHECK_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport.writeResultatsFile(*,*,*,*,*,*))"
         + " && args(ecdeDirectory,integDocs,initDocCount,restitutionUuids,sommaireFile,modeBatch)";

   private static final String CHECK_VRTL_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatFileSuccessSupport.writeVirtualResultatsFile(*,*,*,*,*))"
         + " && args(ecdeDirectory,integDocs,initDocCount,restitutionUuids,sommaireFile)";

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
    * @param initDocCount
    *           nombre de documents intégrés
    * @param restitutionUuids
    *           Ecriture ou non l'UUID des documents intégrés dans le
    *           resultat.xml
    * @param sommaireFile
    *           Fichier sommaire.xml
    * 
    * @param modeBatch
    *           Mode du batch
    */
   @Before(CHECK_METHOD)
   public final void checkWriteResultats(final File ecdeDirectory,
         final ConcurrentLinkedQueue<TraitementMasseIntegratedDocument> integDocs,
         final int initDocCount, final boolean restitutionUuids,
         final File sommaireFile, final String modeBatch) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

      if (initDocCount < 0) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "initDocCount"));
      }

      if (restitutionUuids && sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "fichier sommaire"));
      }

      if (StringUtils.isBlank(modeBatch)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "modeBatch"));
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
    * @param initDocCount
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
         final int initDocCount, final boolean restitutionUuids,
         final File sommaireFile) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

      if (restitutionUuids && CollectionUtils.isEmpty(integDocs)) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "liste des documents intégrés"));
      }

      if (initDocCount < 0) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "initDocCount"));
      }

      if (restitutionUuids && sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "fichier sommaire"));
      }

   }

}
