package fr.urssaf.image.sae.regionalisation.service.validation;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * fr.urssaf.image.sae.regionalisation.service.ProcessingService.<br>
 * La validation est basée sur la programmation aspect
 * 
 * 
 */
@Aspect
public class ProcessingServiceValidation {

   private static final String CLASS = "fr.urssaf.image.sae.regionalisation.service.ProcessingService.";

   private static final String FILE_METHOD = "execution(void " + CLASS
         + "launchWithFile(*,*, *, *, *, *))"
         + "&& args(updateDatas,source, uuid, first, last, dirPath)";

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.regionalisation.service.ProcessingService#launchWithFile(boolean, java.io.File)}
    * <br>
    * <ul>
    * <li><code>source</code> doit être non null</li>
    * </ul>
    * 
    * @param updateDatas
    *           flag indiquant si le traitement est réel ou tir à blanc.
    * @param source
    *           fichier contenant les données
    * @param uuid
    *           identifiant unique du traitement
    * @param first
    *           index du premier enregistrement à traiter
    * @param last
    *           index du dernier enregistrement à traiter
    * @param dirPath
    *           chemin du répertoire où seront créés les fichiers de suivi
    */
   @Before(FILE_METHOD)
   public final void launchWithFile(boolean updateDatas, File source,
         String uuid, int first, int last, String dirPath) {

      if (source == null) {
         throw new IllegalArgumentException(
               "le paramètre fichier doit être renseigné");
      }

      if (StringUtils.isBlank(uuid)) {
         throw new IllegalArgumentException(
               "l'identifiant unique doit être renseigné");
      }

      if (first > last) {
         throw new IllegalArgumentException(
               "l'index de départ doit être inférieur ou égal à l'index de fin");
      }

      if (StringUtils.isBlank(dirPath)) {
         throw new IllegalArgumentException(
               "le répertoire parent doit être renseigné");
      }

   }

}
