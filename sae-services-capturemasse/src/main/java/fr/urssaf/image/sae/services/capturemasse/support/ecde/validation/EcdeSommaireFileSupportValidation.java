/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde.validation;

import java.net.URI;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * {@link fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport}
 * . La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class EcdeSommaireFileSupportValidation {

   private static final String ARGUMENT_REQUIRED = "L'argument '%s' doit être renseigné ou être non null.";

   private static final String CONVERT_METHOD = "execution(java.io.File fr.urssaf.image.sae.services.capturemasse.support.ecde.EcdeSommaireFileSupport.convertURLtoFile(*))"
         + " && args(sommaireURL)";

   /**
    * permet de vérifier que l'ensemble des paramètres de la méthode
    * convertURLtoFile possède tous les arguments renseignés
    * 
    * @param sommaireURL
    *           URL du fichier sommaire.xml
    */
   @Before(CONVERT_METHOD)
   public final void checkConvertURLtoFile(final URI sommaireURL) {
      if (sommaireURL == null) {
         throw new IllegalArgumentException(String.format(ARGUMENT_REQUIRED,
               "sommaireURL"));
      }
   }

}
