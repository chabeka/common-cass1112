/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire;

import java.io.File;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

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
    *            Le fichier sommaire.xml est invalide
    */
   void validationSommaire(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException;

   /**
    * validation que le mode de saisi est compatible avec celui passé en
    * paramètre
    * 
    * @param sommaireFile
    *           le fichier soammire.xml
    * @param batchMode
    *           mode de capture attendue
    * @throws CaptureMasseSommaireFormatValidationException
    *            Le fichier sommaire.xml est invalide
    */
   void validerModeBatch(File sommaireFile, String batchMode)
         throws CaptureMasseSommaireFormatValidationException;
}
