/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class DebutTraitementFlagSupportValidation {

   private static final String CHECK_WRITE_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport.writeDebutTraitementFlag(*,*))"
         + " && args(debutTraitementFlag,ecdeDirectory)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * checkEcdeWrite possède tous les arguments renseignés
    * 
    * @param debutTraitementFlag
    *           modèle du fichier debut_traitement.flag
    * @param ecdeDirectory
    *           chemin ECDE
    */
   @Before(CHECK_WRITE_METHOD)
   public final void checkWrite(DebutTraitementFlag debutTraitementFlag,
         File ecdeDirectory) {

      if (debutTraitementFlag == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "debutTraitement"));
      }

      if (debutTraitementFlag.getHostInfo() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "debutTraitement.hostInfo"));
      }

      if (debutTraitementFlag.getIdTraitement() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "debutTraitement.idTraitement"));
      }

      if (debutTraitementFlag.getStartDate() == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "debutTraitement.startDate"));
      }

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeDirectory"));
      }

   }

}
