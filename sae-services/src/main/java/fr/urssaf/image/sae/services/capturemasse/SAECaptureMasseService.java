/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse;

import java.net.URI;
import java.util.UUID;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

/**
 * Service de capture en masse du SAE
 * 
 */
public interface SAECaptureMasseService {

   /**
    * Service de capture de masse
    * 
    * @param sommaireURL
    *           URL ECDE du fichier sommaire.xml
    * @param idTraitement
    *           identifiant unique du traitement
    * @throws CaptureMasseSommaireEcdeURLException
    *            l'URL ECDE du sommaire est incorrecte
    * @throws CaptureMasseSommaireFileNotFoundException
    *            Le traitement ne trouve pas le sommaire de l'ECDE
    * @throws CaptureMasseEcdeWriteFileException
    *            Le répertoire de traitement dans l'ECDE n'a pas les droits
    *            d'écriture pour le traitement de la capture de masse
    */
   void captureMasse(URI sommaireURL, UUID idTraitement)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException,
         CaptureMasseEcdeWriteFileException;

}
