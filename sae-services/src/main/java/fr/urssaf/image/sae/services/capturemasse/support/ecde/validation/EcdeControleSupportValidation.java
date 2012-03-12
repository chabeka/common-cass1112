/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeControleSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class EcdeControleSupportValidation {

   private static final String CHECK_CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeControleSupport.checkEcdeWrite(*))"
         + " && args(sommaireFile)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * checkEcdeWrite possède tous les arguments renseignés
    * 
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml pour un traitement de
    *           masse
    */
   @Before(CHECK_CONTROLES_METHOD)
   public final void checkEcdeWrite(File sommaireFile) {

      if (sommaireFile == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "sommaireFile"));
      }

   }

}
