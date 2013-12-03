package fr.urssaf.image.sae.format.identification.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;

/**
 * Interface décrivant les opérations d’identification possibles.
 * 
 */
public interface IdentificationService {

   /**
    * Méthode permettant d’identifier un fichier.
    * 
    * @param idFormat
    *           Identifiant du format souhaité. <br>
    *           C’est en générale la valeur se trouvant au niveau des
    *           métadonnées « FormatFichier »<br>
    *           Paramètre obligatoire.
    * @param fichier
    *           Le fichier à identifier Paramètre obligatoire.
    * @return Objet contenant le résultat de l’identification
    * @throws UnknownFormatException
    *            : Le format demandé n’existe pas en base
    * @throws IdentifierInitialisationException
    *            : Impossible d’instancier l’identificateur
    * @throws IOException
    *            : Une exception s’est produite au niveau de l’outil
    *            d’identification           
    */
   IdentificationResult identifyFile(String idFormat, File fichier)
   throws IdentifierInitialisationException, UnknownFormatException, IOException;

   /**
    * Identifie un flux.
    * 
    * @param idFormat
    *           Identifiant du format souhaité. <br>
    *           C’est en générale la valeur se trouvant au niveau des
    *           métadonnées « FormatFichier »<br>
    *           Paramètre obligatoire.
    * @param stream
    *           Un flux correspondant au « fichier » à identifier -Paramètre
    *           obligatoire.
    * @return Objet contenant le résultat de l’identification
    * @throws UnknownFormatException
    *            : Le format demandé n’existe pas en base<br>
    * @throws IdentifierInitialisationException
    *            : Impossible d’instancier l’identificateur<br>
    */
   IdentificationResult identifyStream(String idFormat, InputStream stream)
         throws UnknownFormatException, IdentifierInitialisationException;

}
