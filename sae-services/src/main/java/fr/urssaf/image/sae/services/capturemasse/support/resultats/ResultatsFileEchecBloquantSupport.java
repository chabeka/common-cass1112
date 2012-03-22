/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats;

import java.io.File;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

/**
 * Support pour l'écriture des fichiers resultats.xml en cas d'échec bloquant
 * lors du traitement de capture de masse
 * 
 */
public interface ResultatsFileEchecBloquantSupport {

   /**
    * Service permettant d'écrire un fihier resultats.xml dans l'ECDE pour les
    * traitements de capture de masse ayant échoué à cause d'une erreur
    * bloquante
    * 
    * @param ecdeDirectory
    *           Répertoire ECDE de traitement pour une capture de masse
    * @param erreur
    *           Exception bloquante
    */
   void writeResultatsFile(File ecdeDirectory,
         CaptureMasseSommaireFormatValidationException erreur);

}
