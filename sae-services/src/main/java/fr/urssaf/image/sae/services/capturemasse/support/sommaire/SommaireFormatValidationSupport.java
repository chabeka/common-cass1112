/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire;

import java.io.File;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

/**
 * Composant de validation du format du fichier sommaire.xml des traitements de
 * capture de masse
 * 
 */
public interface SommaireFormatValidationSupport {

   /**
    * validation du format du fichier sommaire.xml
    * 
    * @param sommaireFile
    *           chemin absolu du fichier sommaire.xml
    * @throws CaptureMasseSommaireFormatValidationException
    *            Le fichier sommaire.xml
    */
   void validationSommaire(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException;
}
