/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class DebutTraitementFlagSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String WRITE_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.flag.DebutTraitementFlagSupport.writeDebutTraitementFlag(*,*))"
         + " && args(flag,ecdeDirectory)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * checkEcdeWrite possède tous les arguments renseignés
    * 
    * @param flag
    *           modèle du fichier debut_traitement.flag
    * @param ecdeDirectory
    *           chemin ECDE
    */
   @Before(WRITE_METHOD)
   public final void checkWrite(final DebutTraitementFlag flag,
         final File ecdeDirectory) {

      if (flag == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "debutTraitement"));
      }

      if (flag.getHostInfo() == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "debutTraitement.hostInfo"));
      }

      if (flag.getIdTraitement() == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "debutTraitement.idTraitement"));
      }

      if (flag.getStartDate() == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "debutTraitement.startDate"));
      }

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

   }

}
