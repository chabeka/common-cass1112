package fr.urssaf.image.commons.padaf.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.activation.DataSource;

import fr.urssaf.image.commons.padaf.exception.FormatValidationException;

/**
 * Service de validation d'un format de fichier
 */
public interface FormatValidationService {

   /**
    * Valide un flux par rapport à la norme PDF/A par Padaf
    * 
    * @param file
    *           Le fichier à valider
    * @return La liste des erreurs de validation. La liste est vide si le
    *         fichier est valide.
    * @throws FormatValidationException
    *            en cas d'échec du moteur de validation de format
    * 
    */
   List<String> validate(File file) throws FormatValidationException;

   /**
    * Valide un flux par rapport à la norme PDF/A par PDFBox
    * 
    * @param dataSource
    *           le fichier à valider
    * @return La liste des erreurs de validation. La liste est vide si le
    *         fichier est valide.
    * @throws FormatValidationException
    *            en cas d'échec du moteur de validation de format
    */
   List<String> validate(DataSource dataSource)
         throws FormatValidationException;

   /**
    * Valide un flux par rapport à la norme PDF/A par PDFBox
    * 
    * @param inputStream
    *           le stream à valider
    * @return La liste des erreurs de validation. La liste est vide si le
    *         fichier est valide.
    * @throws FormatValidationException
    *            en cas d'échec du moteur de validation de format
    */
   List<String> validate(InputStream inputStream)
         throws FormatValidationException;

   /**
    * Valide un flux par rapport à la norme PDF/A par PDFBox
    * 
    * @param data
    *           les données à valider
    * @return La liste des erreurs de validation. La liste est vide si le
    *         fichier est valide.
    * @throws FormatValidationException
    *            en cas d'échec du moteur de validation de format
    */
   List<String> validate(byte[] data) throws FormatValidationException;

}
