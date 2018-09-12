/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.ecde.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.batch.capturemasse.support.ecde.EcdeControleSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class EcdeControleSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String CHECK_METHOD = "execution(void fr.urssaf.image.sae.services.batch.capturemasse.support.ecde.EcdeControleSupport.checkEcdeWrite(*))"
         + " && args(sommaireFile)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * checkEcdeWrite possède tous les arguments renseignés
    * 
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml pour un traitement de
    *           masse
    */
   @Before(CHECK_METHOD)
   public final void checkEcdeWrite(final File sommaireFile) {

      if (sommaireFile == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "sommaireFile"));
      }

   }

}
