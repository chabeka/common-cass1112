/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.ecde;

import java.io.File;
import java.net.URI;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireEcdeURLException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;

/**
 * Composant de manipulation de l'URL ECDE pour le fichier sommaire d'un
 * traitement de capture de masse.
 * 
 */
public interface EcdeSommaireFileSupport {

   /**
    * Service permettant à partir d'une URL ECDE du sommaire.xml de récupérer le
    * chemin absolu du fichier dans l'ECDE
    * 
    * @param sommaireURL
    *           URL ECDE du fichier sommaire.xml
    * @return chemin absolu du fichier sommaire.xml
    * @throws CaptureMasseSommaireEcdeURLException
    *            l'URL ECDE du sommaire est incorrecte
    * @throws CaptureMasseSommaireFileNotFoundException
    *            Le fichier sommaire.xml est introuvable dans l'ECDE
    */
   File convertURLtoFile(URI sommaireURL)
         throws CaptureMasseSommaireEcdeURLException,
         CaptureMasseSommaireFileNotFoundException;

}
