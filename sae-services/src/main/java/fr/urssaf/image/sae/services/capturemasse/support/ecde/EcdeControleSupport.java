/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.ecde;

import java.io.File;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;

/**
 * Composant de contrôle sur l'ECDE pour les traitements de capture en masse
 * 
 */
public interface EcdeControleSupport {

   /**
    * Service permettant de vérifier si le traitement de capture a les droits
    * d'écriture dans l'ECDE
    * 
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml
    * @throws CaptureMasseEcdeWriteFileException
    *            le répertoire de traitement dans l'ECDE n'a pas les droits
    *            d'écriture pour le traitement de masse
    */
   void checkEcdeWrite(File sommaireFile)
         throws CaptureMasseEcdeWriteFileException;

}
