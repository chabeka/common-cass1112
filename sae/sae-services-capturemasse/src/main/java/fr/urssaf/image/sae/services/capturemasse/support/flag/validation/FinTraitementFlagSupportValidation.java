/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class FinTraitementFlagSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String WRITE_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport.writeFinTraitementFlag(*))"
         + " && args(ecdeDirectory)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * checkEcdeWrite possède tous les arguments renseignés
    * 
    * @param ecdeDirectory
    *           chemin absolu du fichier sommaire.xml pour un traitement de
    *           masse
    */
   @Before(WRITE_METHOD)
   public final void checkWrite(final File ecdeDirectory) {

      if (ecdeDirectory == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "ecdeDirectory"));
      }

   }
}
