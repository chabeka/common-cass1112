/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.validation;

import java.io.File;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;


/**
 * Validation des paramètres passés en arguments des implémentations de
 * {@link fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport}.
 * La validation est basée sur la programmation Aspect
 * 
 */
@Aspect
public class SommaireFormatValidationSupportValidation {

   private static final String CONTROLES_METHOD = "execution(void fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport.validationSommaire(*))"
      + " && args(sommaireFile)";

/**
 * permet de vérifier que l'ensemble des paramètres de la méthode
 * controleSAEDocument possède tous les arguments renseignés
 * 
 * @param sommaireFile
 *           chemin absolu du fichier sommaire.xml
 */
@Before(CONTROLES_METHOD)
public final void checkValidationSommaire(File sommaireFile) {

   if (sommaireFile == null) {
      throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
            "argument.required", "sommaireFile"));
   }

}

   
   
}
