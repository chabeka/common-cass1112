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
    *           le fichier sommaire.xml
    * @param batchModes
    *           liste de mode de capture attendu
    * @throws CaptureMasseSommaireFormatValidationException
    *            Le fichier sommaire.xml est invalide
    */
   void validerModeBatch(File sommaireFile, String... batchModes)
         throws CaptureMasseSommaireFormatValidationException;

   /**
    * validation de l'unicité de chaque IdGed si présence dans le fichier
    * sommaire
    * 
    * @param sommaireFile
    *           le fichier sommaire.xml
    * @throws CaptureMasseSommaireFormatValidationException
    *            Le fichier sommaire.xml est invalide
    */
   void validerUniciteIdGed(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException;

   /**
    * Validation de la presence de la balise "baliseRequired" dans la partie
    * "document" du fichier sommaire.
    * 
    * @param sommaireFile
    *           Fichier sommaire.xml
    * @param baliseRequired
    *           Balise obligatoire
    * @throws CaptureMasseSommaireFormatValidationException
    *            Le fichier sommaire.xml est invalide
    */
   void validationDocumentBaliseRequisSommaire(File sommaireFile, String baliseRequired) 
         throws CaptureMasseSommaireFormatValidationException ;

   /**
    * validation de l'unicité de chaque UUID si présence dans le fichier
    * sommaire
    * 
    * @param sommaireFile
    *           le fichier soammire.xml
    * @throws CaptureMasseSommaireFormatValidationException
    *            Le fichier sommaire.xml est invalide
    */
   void validerUniciteUuid(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException;

   void validationDocumentTypeMultiActionSommaire(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException;


}
