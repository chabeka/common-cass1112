package fr.urssaf.image.sae.documents.executable.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

import net.docubase.toolkit.model.document.Document;

/**
 * Service permettant de réaliser des opérations de format sur les fichiers :
 * <ul>
 * <li>identification</li>
 * <li>validation</li>
 * </ul>
 */
public interface FormatFichierService {

   /**
    * Réalise l'identification du fichier.
    * 
    * @param idFormat
    *           Code du format
    * @param file
    *           fichier à identifier
    * @param document
    *           Informations sur le document
    * @param metadonnees
    *           liste des metadonnées
    * @return Indicateur de validation du fichier :
    *         <ul>
    *         <li><b>true</b> : le fichier est identifié</li>
    *         <li><b>false</b> : il ne l'est pas</li>
    *         </ul>
    */
   boolean identifierFichier(String idFormat, File file, Document document,
         List<String> metadonnees);

   /**
    * Réalise la validation du fichier.
    * 
    * @param idFormat
    *           Code du format
    * @param file
    *           fichier à valider
    * @return {@link ValidationResult} - Résultat de la validation
    * @throws UnknownFormatException
    *            Le format de fichier demandé n'existe pas
    * @throws ValidatorInitialisationException
    *            Impossible d'instancier le validateur de format
    * @throws IOException
    *            Exception levée sur la fermeture du flux (après écriture du
    *            fichier temp)
    */
   ValidationResult validerFichier(String idFormat, File file)
         throws UnknownFormatException, ValidatorInitialisationException,
         IOException;
}
