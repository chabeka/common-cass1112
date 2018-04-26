package fr.urssaf.image.commons.itext.service;

import java.io.File;

import fr.urssaf.image.commons.itext.exception.ExtractionException;

/**
 * Service de traitement des images issues d'un pdf.
 *
 */
public interface TraitementImageService {

   /**
    * Methode permettant d'extraire les images d'un fichier pdf.
    * 
    * @param fichier
    *           fichier pdf
    * @param repExtraction
    *           repertoire d'extraction
    * @throws ExtractionException
    *            Une erreur s'est produite lors de l'extraction des images
    */
   void extractImages(File fichier, String repExtraction)
         throws ExtractionException;

   /**
    * Methode permettant d'extraire les images d'un fichier pdf.
    * 
    * @param contenuFichier
    *           contenu du fichier pdf
    * @param repExtraction
    *           repertoire d'extraction
    * @throws ExtractionException
    *            Une erreur s'est produite lors de l'extraction des images
    */
   void extractImages(byte[] contenuFichier, String repExtraction)
         throws ExtractionException;

   /**
    * Methode permettant de verifier que le fichier pdf ne comporte que des
    * images.
    * 
    * @param fichier
    *           fichier pdf
    * @return boolean, vrai si le fichier pdf ne contient que des images
    */
   boolean isFullImagePdf(File fichier);

   /**
    * Methode permettant de verifier que le fichier pdf ne comporte que des
    * images.
    * 
    * @param contenuFichier
    *           contenu du fichier pdf
    * @return boolean, vrai si le fichier pdf ne contient que des images
    */
   boolean isFullImagePdf(byte[] contenuFichier);
}
