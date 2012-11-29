package fr.urssaf.image.commons.padaf.service;

import java.io.File;
import java.util.List;

import fr.urssaf.image.commons.padaf.exception.FormatValidationException;

/**
 * Service de validation d'un format de fichier
 */
public interface FormatValidationService {

   /**
    * Valide le format d'un fichier.
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

}
