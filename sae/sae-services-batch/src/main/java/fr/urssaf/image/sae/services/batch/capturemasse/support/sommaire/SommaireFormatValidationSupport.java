/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;

/**
 * Composant de validation du format du fichier sommaire.xml des traitements de
 * capture de masse
 */
public interface SommaireFormatValidationSupport {

  /**
   * validation du format du fichier sommaire.xml
   * 
   * @param sommaireFile
   *          chemin absolu du fichier sommaire.xml
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  void validationSommaire(File sommaireFile)
      throws CaptureMasseSommaireFormatValidationException;

  /**
   * validation que le mode de saisi est compatible avec celui passé en
   * paramètre
   * 
   * @param sommaireFile
   *          le fichier sommaire.xml
   * @param batchModes
   *          liste de mode de capture attendu
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   * @throws CaptureMasseSommaireFileNotFoundException
   *           Le fichier sommaire.xml n'existe pas
   */
  void validerModeBatch(File sommaireFile, String... batchModes)
      throws CaptureMasseSommaireFormatValidationException,
      CaptureMasseSommaireFileNotFoundException;

  /**
   * validation de l'unicité de chaque IdGed si présence dans le fichier
   * sommaire
   * 
   * @param sommaireFile
   *          le fichier sommaire.xml
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  void validerUniciteIdGed(File sommaireFile)
      throws CaptureMasseSommaireFormatValidationException;

  /**
   * Validation de la presence de la balise "baliseRequired" dans la partie
   * "document" du fichier sommaire.
   * 
   * @param sommaireFile
   *          Fichier sommaire.xml
   * @param baliseRequired
   *          Balise obligatoire
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  void validationDocumentBaliseRequisSommaire(File sommaireFile, String baliseRequired)
      throws CaptureMasseSommaireFormatValidationException;

  /**
   * validation de la balise document type multi action si présence dans le fichier
   * sommaire
   * 
   * @param sommaireFile
   *          le fichier soammire.xml
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  void validationDocumentTypeMultiActionSommaire(File sommaireFile)
      throws CaptureMasseSommaireFormatValidationException;

  /**
   * Validation de la presence de la balise "baliseRequired" et de la valeur
   * associée dans le fichier sommaire.
   * 
   * @param sommaireFile
   *          Fichier sommaire.xml
   * @param baliseRequired
   *          Balise obligatoire
   * @param valeurRequired
   *          Valeur obligatoire
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  void validationDocumentValeurBaliseRequisSommaire(File sommaireFile,
                                                    String baliseRequired, String valeurRequired)
      throws CaptureMasseSommaireFormatValidationException;

  /**
   * validation de l'unicité de la metadonnées passé en paramètre si présence dans le fichier
   * sommaire
   * 
   * @param sommaireFile
   *          le fichier sommaire.xml
   * @param nomMeta
   *          nom meta à vérifier
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  List<Integer> validerUniciteMeta(File sommaireFile, String nomMeta) throws IOException;

  /**
   * validation de l'unicité du tag passé en paramètre si présence dans le fichier
   * sommaire
   * 
   * @param sommaireFile
   *          le fichier sommaire.xml
   * @param nomTag
   *          nom du tag à vérifier
   * @throws CaptureMasseSommaireFormatValidationException
   *           Le fichier sommaire.xml est invalide
   */
  List<Integer> validerUniciteTag(File sommaireFile, String nomTag) throws IOException;

}
