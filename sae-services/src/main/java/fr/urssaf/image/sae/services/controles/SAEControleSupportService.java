/**
 * 
 */
package fr.urssaf.image.sae.services.controles;

import java.io.File;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseEcdeWriteFileException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireTypeHashException;

/**
 * Composant de contrôle sur l'ECDE pour les traitements de capture en masse
 * 
 */
public interface SAEControleSupportService {

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
   
   /**
    * Service permettant de vérifier que le hash fourni correspond à celui du fichier passé en paramètre
    * 
    * @throws CaptureMasseSommaireHashException 
    *          Erreur survenu lors de la vérification du hash
    * @throws CaptureMasseSommaireTypeHashException
    *          Erreur survenu lors de la vérification de l'agorithme de hash
    */
   void checkHash(File sommaire, String hash, String typeHash)throws CaptureMasseSommaireHashException, CaptureMasseSommaireTypeHashException;
      
   

}
