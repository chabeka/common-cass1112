package fr.urssaf.image.sae.regionalisation.service.validation;

import java.io.File;

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

   private static final String METHOD = "execution(void " + CLASS
         + "launch(*,*,*))"
         + "&& args(updateDatas,firstRecord,processingCount)";

   private static final String FILE_METHOD = "execution(void " + CLASS
         + "launchWithFile(*,*))" + "&& args(updateDatas,source)";

   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.regionalisation.service.ProcessingService#launch(boolean, int, int)}
    * <br>
    * <ul>
    * <li><code>firstRecord</code> doit être >= 0</li>
    * <li><code>processingCount</code> doit être > 0</li>
    * </ul>
    * 
    * @param updateDatas
    *           flag indiquant si le traitement est réel ou tir à blanc.
    * @param firstRecord
    *           numéro du premier enregistrement à traiter
    * @param processingCount
    *           nombre d'enregistrement à traiter
    */
   @Before(METHOD)
   public final void launch(boolean updateDatas, int firstRecord,
         int processingCount) {

      if (firstRecord < 0) {
         throw new IllegalArgumentException(
               "le paramètre 'firstRecord' doit être supérieur ou égal à 0");
      }

      if (processingCount < 1) {
         throw new IllegalArgumentException(
               "le paramètre 'processingCount' doit être supérieur à 0");
      }

   }

   
   
   /**
    * Validation des méthodes de
    * {@link fr.urssaf.image.sae.regionalisation.service.ProcessingService#launchWithFile(boolean, java.io.File)
    * <br>
    * <ul>
    * <li><code>source</code> doit être non null</li>
    * </ul>
    * 
    * @param updateDatas
    *           flag indiquant si le traitement est réel ou tir à blanc.
    * @param source
    *          fichier contenant les données
    */
   @Before(FILE_METHOD)
   public final void launchWithFile(boolean updateDatas, File source) {

      if (source == null) {
         throw new IllegalArgumentException(
               "le paramètre fichier doit être renseigné");
      }

   }

}
