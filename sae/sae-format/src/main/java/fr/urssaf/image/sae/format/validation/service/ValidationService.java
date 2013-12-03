package fr.urssaf.image.sae.format.validation.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * Interface décrivant les opérations de validation possibles.
 * 
 */
public interface ValidationService {

   /**
    * Méthode permettant de valider un fichier.
    * 
    * @param idFormat
    *           Identifiant du format à valider - paramètre obligatoire
    * @param file
    *           Le fichier à valider - paramètre obligatoire
    * @return Objet contenant le résultat de la validation
    * @throws UnknownFormatException
    *            : Le format n’existe pas en base
    * @throws ValidatorInitialisationException
    *            : Impossible d’initialiser le validateur
    * @throws FileNotFoundException
    *            : Le fichier passé en paramètre est introuvable.
    * 
    */
   ValidationResult validateFile(String idFormat, File file)
         throws UnknownFormatException, ValidatorInitialisationException,
         FileNotFoundException;

   /**
    * Opération de validation d’un flux.
    * 
    * @param idFormat
    *           Identifiant du format à valider - paramètre obligatoire
    * @param stream
    *           Le flux à valider - paramètre obligatoire
    * @return Objet contenant le résultat de la validation
    * @throws UnknownFormatException
    *            : Le format n’existe pas en base
    * @throws ValidatorInitialisationException
    *            : Impossible d’initialiser le validateur
    * @throws IOException : Exception levée sur la fermeture du flux (après écriture du fichier temp)
    * 
    */
   ValidationResult validateStream(String idFormat, InputStream stream)
         throws UnknownFormatException, ValidatorInitialisationException, IOException;

}
