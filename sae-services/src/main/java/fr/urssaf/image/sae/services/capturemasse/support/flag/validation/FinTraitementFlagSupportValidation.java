/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Validation des arguments passés en paramètre des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class FinTraitementFlagSupportValidation {

   private static final String CHECK_WRITE_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.flag.FinTraitementFlagSupport.writeFinTraitementFlag(*))"
      + " && args(ecdeDirectory)";

/**
 * permet de vérifier que l'ensemble des paramètres de la méthode
 * checkEcdeWrite possède tous les arguments renseignés
 * 
 * @param ecdeDirectory
 *           chemin absolu du fichier sommaire.xml pour un traitement de
 *           masse
 */
@Before(CHECK_WRITE_METHOD)
public final void checkWrite(File ecdeDirectory) {

   if (ecdeDirectory == null) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
            "argument.required", "ecdeDirectory"));
   }
   
}
}
