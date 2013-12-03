package fr.urssaf.image.sae.format.validation.validators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fr.urssaf.image.commons.pdfbox.exception.FormatValidationException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * Interface décrivant les opérations de validation possibles.
 */
public interface Validator {

   /**
    * Méthode permettant de valider un fichier.
    * 
    * @param file
    *           à valider - paramètre obligatoire.
    * @return Objet contenant le résultat de la validation (
    *         {@link ValidationResult})
    * @throws FileNotFoundException
    *            : Le fichier passé en paramètre est introuvable.
    * @throws FormatValidationException
    *            : Le fichier passé en paramètre est introuvable.
    * 
    */
   ValidationResult validateFile(File file) throws FileNotFoundException,
         FormatValidationException;

   /**
    * Opération de validation d’un flux.
    * 
    * @param stream
    *           le flux à valider - paramètre obligatoire.
    * @return Objet contenant le résultat de la validation (
    *         {@link ValidationResult})
    * 
    * @throws FormatValidationException
    *            : Erreur dans la validation du format
    * 
    */
   ValidationResult validateStream(InputStream stream)
         throws FormatValidationException;
}
