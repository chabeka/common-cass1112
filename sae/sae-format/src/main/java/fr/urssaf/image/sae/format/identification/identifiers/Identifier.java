package fr.urssaf.image.sae.format.identification.identifiers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;

/**
 * Interface décrivant les opérations d’identification possibles.
 * 
 */
public interface Identifier {

   /**
    * Méthode permettant d’identifier un fichier
    * 
    * @param idFormat
    *           : Identifiant du format souhaité. C’est en générale la valeur<br>
    *           se trouvant au niveau des métadonnées « FormatFichier » <br>
    *           Paramètre obligatoire.
    * @param fichier
    *           : Le fichier à identifier - paramètre obligatoire
    * 
    * @return Objet contenant le résultat de l’identification
    * @exception IOException : exception liée au fichier
    */
   IdentificationResult identifyFile(String idFormat, File fichier)
         throws IOException;

   /**
    * Identifie un flux.
    * 
    * @param idFormat
    *           : Identifiant du format souhaité. C’est en générale la valeur<br>
    *           se trouvant au niveau des métadonnées « FormatFichier » <br>
    *           Paramètre obligatoire.
    * @param stream
    *           : Un flux correspondant au "fichier" à identifier - paramètre
    *           obligatoire
    * 
    * @return Objet contenant le résultat de l’identification
    */
   IdentificationResult identifyStream(String idFormat, InputStream stream);

}
